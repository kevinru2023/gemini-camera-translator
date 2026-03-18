package com.example.final_project_cs492_team27.data.repository

import com.example.final_project_cs492_team27.data.db.TranslationHistoryDao
import com.example.final_project_cs492_team27.data.model.TranslationHistory
import kotlinx.coroutines.flow.Flow

class TranslationHistoryRepository(private val dao: TranslationHistoryDao) {

    val allHistory: Flow<List<TranslationHistory>> = dao.getAllHistory()

    suspend fun insert(history: TranslationHistory) {
        dao.insert(history)
    }

    suspend fun delete(history: TranslationHistory) {
        dao.delete(history)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
