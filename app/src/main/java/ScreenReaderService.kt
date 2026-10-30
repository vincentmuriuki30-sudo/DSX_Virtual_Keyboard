package com.dsx.virtualkeyboard

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScreenReaderService : AccessibilityService() {

    companion object {
        var latestScreenText: String = ""
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return
        val sb = StringBuilder()
        extractText(rootNode, sb)
        latestScreenText = sb.toString()
    }

    private fun extractText(node: AccessibilityNodeInfo?, sb: StringBuilder) {
        if (node == null) return
        if (node.text != null && node.text.isNotEmpty()) {
            sb.append(node.text).append(" ")
        }
        for (i in 0 until node.childCount) {
            extractText(node.getChild(i), sb)
        }
    }

    override fun onInterrupt() {}
}
