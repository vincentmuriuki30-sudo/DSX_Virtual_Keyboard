package com.dsx.virtualkeyboard

import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class AISmartKeyboardService : InputMethodService() {

    private lateinit var keyboardView: View
    private lateinit var aiSuggestChip: TextView

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null)
        setupKeys(keyboardView)
        applySavedTheme(keyboardView)
        return keyboardView
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        if (::keyboardView.isInitialized) {
            applySavedTheme(keyboardView)
        }
    }

    private fun setupKeys(view: View) {
        aiSuggestChip = view.findViewById(R.id.aiSuggestChip)

        val listener = View.OnClickListener { v ->
            val ic = currentInputConnection ?: return@OnClickListener
            if (v is Button) {
                val text = v.text.toString()
                when (text) {
                    "DEL" -> ic.deleteSurroundingText(1, 0)
                    "SPACE" -> ic.commitText(" ", 1)
                    "ENTER" -> ic.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_DONE)
                    else -> ic.commitText(text, 1)
                }
            }
        }

        // Attach listener to all keyboard buttons recursively
        bindAllButtons(view, listener)
    }

    private fun bindAllButtons(view: View, listener: View.OnClickListener) {
        if (view is Button) {
            view.setOnClickListener(listener)
        } else if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                bindAllButtons(view.getChildAt(i), listener)
            }
        }
    }

    private fun applySavedTheme(view: View) {
        val prefs = getSharedPreferences("dsx_prefs", Context.MODE_PRIVATE)
        val theme = prefs.getString("selected_theme", "dark")
        val mainContainer = view.findViewById<LinearLayout>(R.id.keyboardMainContainer) ?: return

        when (theme) {
            "neon_blue" -> mainContainer.setBackgroundColor(Color.parseColor("#0A192F"))
            "amoled" -> mainContainer.setBackgroundColor(Color.parseColor("#000000"))
            else -> mainContainer.setBackgroundColor(Color.parseColor("#121212"))
        }
    }
}
