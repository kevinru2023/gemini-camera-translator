package com.example.final_project_cs492_team27.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.final_project_cs492_team27.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val fontSizePreference = findPreference<EditTextPreference>("font_size")
        fontSizePreference?.setOnBindEditTextListener { editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER }
    }
}