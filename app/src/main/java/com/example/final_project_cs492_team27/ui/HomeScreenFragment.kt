package com.example.final_project_cs492_team27.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.final_project_cs492_team27.R
import com.example.final_project_cs492_team27.data.db.AppDatabase
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.graphics.createBitmap
import java.nio.ByteBuffer

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private val homeViewModel: HomeViewModel by viewModels()
    private val translationHistoryViewModel: TranslationHistoryViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private var cameraPV: PreviewView? = null
    private lateinit var cameraExecutor: ExecutorService

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraPV = view.findViewById<PreviewView>(R.id.viewFinder)
        val imageCaptureButton = view.findViewById<Button>(R.id.image_capture_button)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo capture button
        imageCaptureButton.setOnClickListener {
            takePhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        homeViewModel.translationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is TranslationState.Loading -> {
                    Toast.makeText(requireContext(), getString(R.string.translating), Toast.LENGTH_SHORT).show()
                }
                is TranslationState.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.translation_success), Toast.LENGTH_SHORT).show()
                    val history = state.translation
                    
                    viewLifecycleOwner.lifecycleScope.launch {
                        AppDatabase.getDatabase(requireContext()).translationHistoryDao().insert(history)
                    }
                    
                    homeViewModel.resetState()
                }
                is TranslationState.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.error_translation, state.message), Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Gemini Error: ${state.message}")
                    homeViewModel.resetState()
                }
                TranslationState.Idle -> { }
            }
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        cameraPV = null
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(cameraExecutor, object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                //get bitmap from image
                val bitmap = imageProxyToBitmap(image)
                image.close()
                homeViewModel.translateImage(bitmap, "Spanish")
                Log.d(TAG, "Bitmap: ${bitmap.width}x${bitmap.height}, config=${bitmap.config}")
                super.onCaptureSuccess(image)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }

        })

//        val dummyBitmap = createBitmap(100, 100)
//        dummyBitmap.eraseColor(Color.WHITE)
//
//        homeViewModel.translateImage(dummyBitmap, "Spanish")
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPV?.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    /**
    *  convert image proxy to bitmap
    *  @param image
    */
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    companion object {
        private const val TAG = "Camera App" // const val doesn't take in R.string val
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}