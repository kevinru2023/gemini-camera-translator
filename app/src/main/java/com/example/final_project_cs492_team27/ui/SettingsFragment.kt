package com.example.final_project_cs492_team27.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.final_project_cs492_team27.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}