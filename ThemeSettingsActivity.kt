package com.dsx.virtualkeyboard

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ThemeSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        val listView = findViewById<ListView>(R.id.themeListView)
        val themes = ThemeRepository.getThemes()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            themes.map { "${it.name} (${it.category})" }
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTheme = themes[position]
            
            val prefs = getSharedPreferences("DSXPrefs", Context.MODE_PRIVATE)
            prefs.edit().putInt("selected_theme_id", selectedTheme.id).apply()

            Toast.makeText(
                this, 
                "Applied Theme: ${selectedTheme.name}", 
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
