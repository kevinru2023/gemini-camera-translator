package com.example.final_project_cs492_team27.ui

import android.graphics.Bitmap
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_project_cs492_team27.BuildConfig
import com.example.final_project_cs492_team27.R
import com.example.final_project_cs492_team27.data.model.TranslationHistory
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed class TranslationState {
    object Idle : TranslationState()
    object Loading : TranslationState()
    data class Success(val translation: TranslationHistory) : TranslationState()
    data class Error(val message: String) : TranslationState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _translationState = MutableLiveData<TranslationState>(TranslationState.Idle)
    val translationState: LiveData<TranslationState> = _translationState

    private val apiKey = BuildConfig.GEMINI_API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    fun translateImage(bitmap: Bitmap, targetLanguage: String) {
        viewModelScope.launch {
            try {
                _translationState.value = TranslationState.Loading

                val prompt = """
                    Translate the text in this image to $targetLanguage.
                    Return ONLY a valid JSON object with the following exactly matching keys:
                    - "originalLanguage": The language you detected.
                    - "translatedLanguage": The language requested ($targetLanguage).
                    - "originalText": The exact text extracted from the image.
                    - "translatedText": The translated text.
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )

                response.text?.let { jsonString ->
                    val json = JSONObject(jsonString)
                    val history = TranslationHistory(
                        originalLanguage = json.getString("originalLanguage"),
                        translatedLanguage = json.getString("translatedLanguage"),
                        originalText = json.getString("originalText"),
                        translatedText = json.getString("translatedText")
                    )
                    _translationState.value = TranslationState.Success(history)
                } ?: run {
                    _translationState.value = TranslationState.Error(getApplication<Application>().getString(R.string.error_empty_response))
                }

            } catch (e: Exception) {
                _translationState.value = TranslationState.Error(e.localizedMessage ?: getApplication<Application>().getString(R.string.error_translation_failed))
            }
        }
    }
    
    fun resetState() {
        _translationState.value = TranslationState.Idle
    }
}
