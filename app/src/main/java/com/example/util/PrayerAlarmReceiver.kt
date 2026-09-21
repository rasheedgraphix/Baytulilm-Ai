package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == ACTION_STOP_AZAN) {
            Log.d("PrayerAlarmReceiver", "Received request to stop/dismiss azan audio")
            PrayerAudioNotifier.dismissAlert(context)
            return
        }

        // On device reboot or timezone change, reschedule alarms for today & tomorrow
        if (action == Intent.ACTION_BOOT_COMPLETED || 
            action == Intent.ACTION_TIMEZONE_CHANGED || 
            action == Intent.ACTION_TIME_CHANGED) {
            Log.d("PrayerAlarmReceiver", "System event ($action) received, rescheduling prayer alarms.")
            rescheduleFromSavedCity(context)
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
        
        // Trigger notification and sound
        PrayerAudioNotifier.triggerPrayerAlert(context, prayerNameUrdu, prayerNameEnglish, prayerId)

        // Reschedule next prayer alarms to ensure continuous tracking
        rescheduleFromSavedCity(context)
    }

    private fun rescheduleFromSavedCity(context: Context) {
        try {
            val prefs = context.getSharedPreferences("prayer_city_prefs", Context.MODE_PRIVATE)
            val cityNameEng = prefs.getString("city_name_eng", null)
            var city: CityLocation? = null
            if (cityNameEng != null) {
                city = PrayerTimeCalculator.defaultCities.find { it.nameEnglish.equals(cityNameEng, ignoreCase = true) }
            }
            if (city == null) {
                val lat = prefs.getFloat("city_lat", -999f).toDouble()
                val lng = prefs.getFloat("city_lng", -999f).toDouble()
                if (lat != -999.0 && lng != -999.0) {
                    val nameUrdu = prefs.getString("city_name_urdu", "شہر") ?: "شہر"
                    val tz = prefs.getString("city_tz", "Asia/Karachi") ?: "Asia/Karachi"
                    city = CityLocation(nameUrdu, cityNameEng ?: "Selected City", lat, lng, tz)
                }
            }
            if (city == null) {
                city = PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Islamabad" }
            }

            val timeZone = java.util.TimeZone.getTimeZone(city.timeZoneId)
            val todayTimes = PrayerTimeCalculator.calculatePrayerTimes(
                lat = city.lat,
                lng = city.lng,
                date = java.util.Date(),
                overrideTimeZone = timeZone
            )
            PrayerAudioNotifier.schedulePrayerAlarms(context, todayTimes)
        } catch (e: Exception) {
            Log.e("PrayerAlarmReceiver", "Failed to reschedule alarms: ${e.message}")
        }
    }

    companion object {
        const val ACTION_STOP_AZAN = "com.example.ACTION_STOP_AZAN"
    }
}
