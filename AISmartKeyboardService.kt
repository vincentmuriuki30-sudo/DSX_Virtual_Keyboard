package com.dsx.virtualkeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.TextView

class AISmartKeyboardService : InputMethodService() {

    private lateinit var keyboardView: View

    override fun onCreateInputView(): View {
        // Inflate the keyboard interface
        keyboardView = layoutInflater.inflate(R.layout.keyboard_layout, null)
        
        // Load user's selected theme locally (fully offline)
        applyLocalTheme()
        setupKeyListeners(keyboardView)
        
        return keyboardView
    }

    private fun applyLocalTheme() {
        val prefs = getSharedPreferences("DSXPrefs", Context.MODE_PRIVATE)
        val themeId = prefs.getInt("selected_theme_id", 1)
        // You can query ThemeRepository.getThemes() here to style key backgrounds offline based on themeId
    }

    private fun setupKeyListeners(view: View) {
        val ic: InputConnection? = currentInputConnection

        // Standard key simulation example
        val btnA = view.findViewById<Button>(R.id.key_a)
        btnA?.setOnClickListener {
            ic?.commitText("a", 1)
        }

        val btnSpace = view.findViewById<Button>(R.id.key_space)
        btnSpace?.setOnClickListener {
            ic?.commitText(" ", 1)
        }

        val btnBackspace = view.findViewById<Button>(R.id.key_backspace)
        btnBackspace?.setOnClickListener {
            ic?.deleteSurroundingText(1, 0)
        }

        // Offline Smart Contextual AI Suggestion Button
        val btnAISuggest = view.findViewById<Button>(R.id.btn_ai_suggest)
        btnAISuggest?.setOnClickListener {
            val screenText = ScreenReaderService.latestScreenText
            val offlineSuggestion = getOfflineAISuggestion(screenText)
            ic?.commitText(offlineSuggestion, 1)
        }
    }

    private fun getOfflineAISuggestion(contextText: String): String {
        // Built-in offline smart response fallback rules
        return if (contextText.contains("price", true) || contextText.contains("how much", true)) {
            "Let me check and get back to you shortly."
        } else if (contextText.contains("meeting", true) || contextText.contains("time", true)) {
            "That works for me. Let's lock it in."
        } else {
            "Sounds good! 👍"
        }
    }
}
