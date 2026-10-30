package com.dsx.virtualkeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEnableKey = findViewById<Button>(R.id.btnEnableKeyboard)
        val btnSelectDefault = findViewById<Button>(R.id.btnSelectDefault)
        val btnAccessibility = findViewById<Button>(R.id.btnAccessibility)
        val btnThemes = findViewById<Button>(R.id.btnOpenThemes)

        // Step 1: Enable Keyboard in System Settings
        btnEnableKey.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }

        // Step 2: Set Keyboard as Default Input Method
        btnSelectDefault.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        // Step 3: Grant Screen Reader Permission (Accessibility)
        btnAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        // Step 4: Open 50+ Themes & Wallpapers Manager
        btnThemes.setOnClickListener {
            startActivity(Intent(this, ThemeSettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateSetupStatus()
    }

    private fun updateSetupStatus() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledMethods = imm.enabledInputMethodList
        val isEnabled = enabledMethods.any { it.packageName == packageName }

        val statusText = findViewById<TextView>(R.id.txtStatus)
        if (isEnabled) {
            statusText.text = "Status: DSX Virtual Keyboard is Enabled! Ready to use."
        } else {
            statusText.text = "Status: Keyboard NOT Enabled yet. Please complete steps above."
        }
    }
}
