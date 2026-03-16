package com.example.final_project_cs492_team27.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.final_project_cs492_team27.data.db.AppDatabase
import com.example.final_project_cs492_team27.data.repository.TranslationHistoryRepository

class TranslationHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TranslationHistoryRepository(
        AppDatabase.getDatabase(application).translationHistoryDao()
    )

    val allHistory = repository.allHistory.asLiveData()

}
