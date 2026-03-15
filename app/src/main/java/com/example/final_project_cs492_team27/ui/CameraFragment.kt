package com.example.final_project_cs492_team27.ui

import android.os.Bundle
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import com.example.final_project_cs492_team27.R
import java.util.concurrent.ExecutorService


class CameraFragment : Fragment(R.layout.main_frag) {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}