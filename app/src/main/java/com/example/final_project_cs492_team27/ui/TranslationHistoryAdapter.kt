package com.example.final_project_cs492_team27.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_cs492_team27.R
import com.example.final_project_cs492_team27.data.model.TranslationHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        holder.bind(current)
    }


    override fun getItemCount(): Int = historyList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textLanguages: TextView = itemView.findViewById(R.id.text_languages)
        private val textOriginal: TextView = itemView.findViewById(R.id.text_original)
        private val textTranslated: TextView = itemView.findViewById(R.id.text_translated)
        private val textTimestamp: TextView = itemView.findViewById(R.id.text_timestamp)
        private val buttonShare: ImageButton = itemView.findViewById(R.id.button_share)

        fun bind(history: TranslationHistory) {
            textLanguages.text = itemView.context.getString(
                R.string.translation_language_format, 
                history.originalLanguage, 
                history.translatedLanguage
            )
            textOriginal.text = history.originalText
            textTranslated.text = history.translatedText

            val formatter = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault())
            textTimestamp.text = formatter.format(Date(history.timestamp))

            //button listener for the share feature
            buttonShare.setOnClickListener{
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Original: ${history.originalText}\nTranslation: ${history.translatedText}")
                }
                itemView.context.startActivity(Intent.createChooser(shareIntent, "share translation"))
            }
        }
    }
}
