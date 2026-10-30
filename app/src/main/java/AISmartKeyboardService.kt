package com.dsx.virtualkeyboard

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class AISmartKeyboardService : InputMethodService() {

    private lateinit var keyboardView: View
    private lateinit var aiSuggestChip: TextView
    private var isShifted = false
    private var currentMode = 0 // 0: Letters, 1: Numbers/Symbols

    private val alphaKeys = arrayOf(
        arrayOf("q","w","e","r","t","y","u","i","o","p"),
        arrayOf("a","s","d","f","g","h","j","k","l"),
        arrayOf("z","x","c","v","b","n","m")
    )

    private val numKeys = arrayOf(
        arrayOf("1","2","3","4","5","6","7","8","9","0"),
        arrayOf("@","#","$","%","&","-","+","(",")"),
        arrayOf("*","\"","'",":",";","!","?")
    )

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null)
        setupKeyboard(keyboardView)
        applySavedTheme(keyboardView)
        return keyboardView
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        if (::keyboardView.isInitialized) {
            applySavedTheme(keyboardView)
        }
    }

    private fun setupKeyboard(view: View) {
        aiSuggestChip = view.findViewById(R.id.aiSuggestChip)

        // Clipboard Actions
        view.findViewById<Button>(R.id.btnCopy)?.setOnClickListener {
            val ic = currentInputConnection ?: return@setOnClickListener
            val selectedText = ic.getSelectedText(0)
            if (!selectedText.isNull_orEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = android.content.ClipData.newPlainText("DSX_Copy", selectedText)
                clipboard.setPrimaryClip(clip)
            }
        }

        view.findViewById<Button>(R.id.btnPaste)?.setOnClickListener {
            val ic = currentInputConnection ?: return@setOnClickListener
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).text
                if (!text.isNull_orEmpty()) {
                    ic.commitText(text, 1)
                }
            }
        }

        view.findViewById<Button>(R.id.btnSelectAll)?.setOnClickListener {
            val ic = currentInputConnection ?: return@setOnClickListener
            ic.performContextMenuAction(android.R.id.selectAll)
        }

        bindAllButtons(view)
    }

    private fun bindAllButtons(view: View) {
        if (view is Button) {
            view.setOnClickListener { handleKeyPress(view) }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                bindAllButtons(view.getChildAt(i))
            }
        }
    }

    private fun handleKeyPress(button: Button) {
        val ic = currentInputConnection ?: return
        val text = button.text.toString()

        when (text) {
            "⇧" -> toggleShift()
            "⌫" -> ic.deleteSurroundingText(1, 0)
            "Space" -> ic.commitText(" ", 1)
            "↵" -> ic.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_DONE)
            "?123" -> toggleLayoutMode(1)
            "ABC" -> toggleLayoutMode(0)
            "COPY", "PASTE", "ALL" -> { /* Handled separately */ }
            else -> {
                ic.commitText(text, 1)
                if (isShifted) {
                    toggleShift()
                }
            }
        }
    }

    private fun toggleShift() {
        isShifted = !isShifted
        val rows = arrayOf(
            arrayOf(R.id.key_q, R.id.key_w, R.id.key_e, R.id.key_r, R.id.key_t, R.id.key_y, R.id.key_u, R.id.key_i, R.id.key_o, R.id.key_p),
            arrayOf(R.id.key_a, R.id.key_s, R.id.key_d, R.id.key_f, R.id.key_g, R.id.key_h, R.id.key_j, R.id.key_k, R.id.key_l),
            arrayOf(R.id.key_z, R.id.key_x, R.id.key_c, R.id.key_v, R.id.key_b, R.id.key_n, R.id.key_m)
        )

        for (r in rows.indices) {
            for (c in rows[r].indices) {
                val btn = keyboardView.findViewById<Button>(rows[r][c]) ?: continue
                val base = alphaKeys[r][c]
                btn.text = if (isShifted) base.uppercase() else base.lowercase()
            }
        }
    }

    private fun toggleLayoutMode(mode: Int) {
        currentMode = mode
        val modeBtn = keyboardView.findViewById<Button>(R.id.key_mode)
        modeBtn?.text = if (mode == 1) "ABC" else "?123"

        val targetSet = if (mode == 1) numKeys else alphaKeys

        val rows = arrayOf(
            arrayOf(R.id.key_q, R.id.key_w, R.id.key_e, R.id.key_r, R.id.key_t, R.id.key_y, R.id.key_u, R.id.key_i, R.id.key_o, R.id.key_p),
            arrayOf(R.id.key_a, R.id.key_s, R.id.key_d, R.id.key_f, R.id.key_g, R.id.key_h, R.id.key_j, R.id.key_k, R.id.key_l),
            arrayOf(R.id.key_z, R.id.key_x, R.id.key_c, R.id.key_v, R.id.key_b, R.id.key_n, R.id.key_m)
        )

        for (r in rows.indices) {
            for (c in rows[r].indices) {
                val btn = keyboardView.findViewById<Button>(rows[r][c]) ?: continue
                if (c < targetSet[r].size) {
                    btn.text = targetSet[r][c]
                    btn.visibility = View.VISIBLE
                } else {
                    btn.visibility = View.GONE
                }
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
