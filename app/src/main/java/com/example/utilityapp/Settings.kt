package com.example.utilityapp

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {
    private lateinit var lightModeSwitch: Switch
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        lightModeSwitch = findViewById(R.id.lightModeSwitch)
        saveButton = findViewById(R.id.saveButton)

        // Load current theme setting
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        lightModeSwitch.isChecked = !isDarkMode

        saveButton.setOnClickListener {
            val newMode = if (lightModeSwitch.isChecked) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }
}