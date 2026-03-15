package com.example.final_project_cs492_team27.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalLanguage: String,
    val translatedLanguage: String,
    val originalText: String,
    val translatedText: String,
    val timestamp: Long = System.currentTimeMillis()
)
