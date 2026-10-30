package com.dsx.virtualkeyboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ThemeSettingsActivity : AppCompatActivity() {

    private lateinit var previewContainer: LinearLayout
    private lateinit var previewText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_settings)

        previewContainer = findViewById(R.id.previewContainer)
        previewText = findViewById(R.id.previewText)

        val prefs = getSharedPreferences("dsx_prefs", Context.MODE_PRIVATE)
        val savedTheme = prefs.getString("selected_theme", "dark")
        updatePreview(savedTheme ?: "dark")

        findViewById<Button>(R.id.btnThemeDark).setOnClickListener {
            applyTheme("dark", "#1E1E1E", "#FFFFFF", prefs)
        }

        findViewById<Button>(R.id.btnThemeBlue).setOnClickListener {
            applyTheme("neon_blue", "#0A192F", "#00E5FF", prefs)
        }

        findViewById<Button>(R.id.btnThemeAMOLED).setOnClickListener {
            applyTheme("amoled", "#000000", "#FFFFFF", prefs)
        }
    }

    private fun applyTheme(themeKey: String, bgColor: String, textColor: String, prefs: android.content.SharedPreferences) {
        prefs.edit().putString("selected_theme", themeKey).apply()
        previewContainer.setBackgroundColor(Color.parseColor(bgColor))
        previewText.setTextColor(Color.parseColor(textColor))
        Toast.makeText(this, "Theme applied successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePreview(themeKey: String) {
        when (themeKey) {
            "neon_blue" -> {
                previewContainer.setBackgroundColor(Color.parseColor("#0A192F"))
                previewText.setTextColor(Color.parseColor("#00E5FF"))
            }
            "amoled" -> {
                previewContainer.setBackgroundColor(Color.parseColor("#000000"))
                previewText.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                previewContainer.setBackgroundColor(Color.parseColor("#1E1E1E"))
                previewText.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
    }
}
