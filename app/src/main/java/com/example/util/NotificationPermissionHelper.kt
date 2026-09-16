package com.example.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationPermissionHelper {
    private const val PREFS_NAME = "notification_prefs"
    private const val KEY_LAST_PROMPT = "last_prompt_timestamp"
    private const val KEY_PROMPT_COUNT = "prompt_count"
    private const val KEY_WAS_ENABLED = "notifications_enabled_previously"

    fun isPermissionGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun shouldPrompt(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isGranted = isPermissionGranted(context)
        val wasEnabled = prefs.getBoolean(KEY_WAS_ENABLED, false)

        // If they just turned it off
        if (wasEnabled && !isGranted) {
            return true
        }

        // If never granted, check time
        if (!isGranted) {
            val lastPrompt = prefs.getLong(KEY_LAST_PROMPT, 0L)
            val currentTime = System.currentTimeMillis()
            val twoDaysInMs = 2 * 24 * 60 * 60 * 1000L
            return (currentTime - lastPrompt) > twoDaysInMs
        }

        // Keep track of enabled state
        if (isGranted) {
            prefs.edit().putBoolean(KEY_WAS_ENABLED, true).apply()
        }

        return false
    }

    fun recordPrompt(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_LAST_PROMPT, System.currentTimeMillis())
            .apply()
    }
}
