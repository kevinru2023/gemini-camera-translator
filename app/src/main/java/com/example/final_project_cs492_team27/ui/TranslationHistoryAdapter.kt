package com.example.final_project_cs492_team27.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Size
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_cs492_team27.R
import com.example.final_project_cs492_team27.data.model.TranslationHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.preference.PreferenceManager

class TranslationHistoryAdapter : RecyclerView.Adapter<TranslationHistoryAdapter.ViewHolder>() {

    private var historyList: List<TranslationHistory> = emptyList()

    fun updateHistoryList(newList: List<TranslationHistory>) {
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = historyList[position]

        val prefs = PreferenceManager.getDefaultSharedPreferences(holder.itemView.context)
        val fontSize = prefs.getString("font_size", "18")?.toFloatOrNull() ?: 18f

        holder.bind(current, fontSize)
    }

    override fun getItemCount(): Int = historyList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textLanguages: TextView = itemView.findViewById(R.id.text_languages)
        private val textOriginal: TextView = itemView.findViewById(R.id.text_original)
        private val textTranslated: TextView = itemView.findViewById(R.id.text_translated)
        private val textTimestamp: TextView = itemView.findViewById(R.id.text_timestamp)

        fun bind(history: TranslationHistory, fontSize: Float) {
            textLanguages.text = itemView.context.getString(
                R.string.translation_language_format, 
                history.originalLanguage, 
                history.translatedLanguage
            )
            textOriginal.text = history.originalText
            textTranslated.text = history.translatedText

            textOriginal.textSize = fontSize
            textTranslated.textSize = fontSize

            val formatter = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault())
            textTimestamp.text = formatter.format(Date(history.timestamp))
        }
    }
}
