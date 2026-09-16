package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_STOP_AZAN) {
            Log.d("PrayerAlarmReceiver", "Received request to stop/dismiss azan audio")
            PrayerAudioNotifier.dismissAlert(context)
            return
        }

        if (!PrayerAudioNotifier.isPrayerSoundEnabled(context)) {
            Log.d("PrayerAlarmReceiver", "Prayer sound is disabled by user, skipping alert.")
            return
        }

        val prayerNameUrdu = intent.getStringExtra("prayer_name_urdu") ?: "نماز"
        val prayerNameEnglish = intent.getStringExtra("prayer_name_english") ?: "Prayer"
        val prayerId = intent.getStringExtra("prayer_id") ?: ""
        Log.d("PrayerAlarmReceiver", "Received prayer alarm: $prayerNameUrdu ($prayerNameEnglish) id=$prayerId")
        PrayerAudioNotifier.triggerPrayerAlert(context, prayerNameUrdu, prayerNameEnglish, prayerId)
    }

    companion object {
        const val ACTION_STOP_AZAN = "com.example.ACTION_STOP_AZAN"
    }
}
