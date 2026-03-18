package com.example.final_project_cs492_team27.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.final_project_cs492_team27.data.model.TranslationHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: TranslationHistory)

    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<TranslationHistory>>

    @Delete
    suspend fun delete(history: TranslationHistory)

    @Query("DELETE FROM translation_history")
    suspend fun clearAll()
}
