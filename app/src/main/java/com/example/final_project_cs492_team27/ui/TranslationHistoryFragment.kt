package com.example.final_project_cs492_team27.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_cs492_team27.R

class TranslationHistoryFragment : Fragment(R.layout.fragment_translation_history) {

    private val viewModel: TranslationHistoryViewModel by viewModels()
    private lateinit var adapter: TranslationHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TranslationHistoryAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            adapter.updateHistoryList(historyList)
        }
    }
}