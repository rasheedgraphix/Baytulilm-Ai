package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.ui.screens.islamic.HijriHelper
import com.example.util.CityLocation
import com.example.util.PrayerTimeCalculator
import com.example.util.PrayerTimeData
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone

object AppWidgetUpdateHelper {

    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        // 1. Simple Clock
        val idsSimple = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicSimpleClockWidget::class.java)
        )
        if (idsSimple.isNotEmpty()) {
            for (id in idsSimple) {
                updateSimpleClock(context, appWidgetManager, id)
            }
        }

        // 4. Prayer Times Widget
        val idsPrayer = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicPrayerTimesWidget::class.java)
        )
        if (idsPrayer.isNotEmpty()) {
            for (id in idsPrayer) {
                updatePrayerTimesWidget(context, appWidgetManager, id)
            }
        }

        // 5. Islamic Clock Widget
        val idsIslamicClock = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicClockWidget::class.java)
        )
        if (idsIslamicClock.isNotEmpty()) {
            for (id in idsIslamicClock) {
                IslamicClockWidget.updateWidget(context, appWidgetManager, id)
            }
        }

        // 6. Islamic Clock Card Widget (Modern Card Style)
        val idsIslamicClockCard = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicClockCardWidget::class.java)
        )
        if (idsIslamicClockCard.isNotEmpty()) {
            for (id in idsIslamicClockCard) {
                IslamicClockCardWidget.updateWidget(context, appWidgetManager, id)
            }
        }
    }

    private fun getLaunchPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getSavedCity(context: Context): CityLocation {
        val prefs = context.getSharedPreferences("prayer_city_prefs", Context.MODE_PRIVATE)
        val cityNameEng = prefs.getString("city_name_eng", null)
        val lat = prefs.getFloat("city_lat", -999f).toDouble()
        val lng = prefs.getFloat("city_lng", -999f).toDouble()

        if (cityNameEng != null && !cityNameEng.equals("Ukiah", ignoreCase = true)) {
            val found = PrayerTimeCalculator.defaultCities.find { it.nameEnglish.equals(cityNameEng, ignoreCase = true) }
            if (found != null && lat == -999.0) return found
        }

        if (lat != -999.0 && lng != -999.0) {
            val nameUrdu = prefs.getString("city_name_urdu", "راولپنڈی") ?: "راولپنڈی"
            val tz = when {
                lat in 23.0..37.5 && lng in 60.0..78.0 -> "Asia/Karachi"
                lat in 16.0..32.0 && lng in 34.0..55.0 -> "Asia/Riyadh"
                lat in 8.0..37.0 && lng in 68.0..97.0 -> "Asia/Kolkata"
                lat in 20.0..27.0 && lng in 88.0..93.0 -> "Asia/Dhaka"
                lat in 24.0..26.5 && lng in 51.0..56.5 -> "Asia/Dubai"
                else -> prefs.getString("city_tz", "Asia/Karachi") ?: "Asia/Karachi"
            }
            return CityLocation(nameUrdu, cityNameEng ?: "Rawalpindi", lat, lng, tz)
        }

        val defaultCity = PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Rawalpindi" }
        prefs.edit()
            .putString("city_name_eng", defaultCity.nameEnglish)
            .putString("city_name_urdu", defaultCity.nameUrdu)
            .putFloat("city_lat", defaultCity.lat.toFloat())
            .putFloat("city_lng", defaultCity.lng.toFloat())
            .putString("city_tz", defaultCity.timeZoneId)
            .apply()
        return defaultCity
    }

    private fun getTodayPrayerTimes(city: CityLocation): List<PrayerTimeData> {
        val timeZone = TimeZone.getTimeZone(city.timeZoneId)
        return PrayerTimeCalculator.calculatePrayerTimes(
            lat = city.lat,
            lng = city.lng,
            date = Date(),
            overrideTimeZone = timeZone
        )
    }


    fun updateSimpleClock(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_simple_clock)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_simple_root, pendingIntent)

        val city = getSavedCity(context)
        val hijriDetails = HijriHelper.getHijriDetails(LocalDate.now(), "ur")
        views.setTextViewText(R.id.tv_simple_hijri, hijriDetails.formattedFull)
        views.setTextViewText(R.id.tv_simple_city, "📍 ${city.nameUrdu}")

        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val isDaytime = currentHour in 6..18
        val weatherIcon = if (isDaytime) "☀️" else "🌙"
        val estimatedTemp = when (currentHour) {
            in 0..5 -> 19
            in 6..9 -> 21
            in 10..15 -> 25
            in 16..19 -> 22
            else -> 20
        }
        views.setTextViewText(R.id.tv_simple_temp, "$estimatedTemp°C $weatherIcon")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateMinimalClock(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_minimal_clock)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_clean_root, pendingIntent)

        val city = getSavedCity(context)
        val hijriDetails = HijriHelper.getHijriDetails(LocalDate.now(), "ur")
        views.setTextViewText(R.id.tv_clean_hijri, hijriDetails.formattedFull)
        views.setTextViewText(R.id.tv_clean_city, "📍 ${city.nameUrdu}")

        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val isDaytime = currentHour in 6..18
        val weatherIcon = if (isDaytime) "☀️" else "🌙"
        val estimatedTemp = when (currentHour) {
            in 0..5 -> 19
            in 6..9 -> 21
            in 10..15 -> 25
            in 16..19 -> 22
            else -> 20
        }
        views.setTextViewText(R.id.tv_clean_temp, "$estimatedTemp°C $weatherIcon")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updatePrayerTimesWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_prayer_times)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_prayer_root, pendingIntent)

        val city = getSavedCity(context)
        val prayers = getTodayPrayerTimes(city)
        val hijriDetails = HijriHelper.getHijriDetails(LocalDate.now(), "ur")

        views.setTextViewText(R.id.tv_prayer_city, "📍 ${city.nameUrdu} (${city.nameEnglish})")
        views.setTextViewText(R.id.tv_prayer_hijri_date, hijriDetails.formattedFull)

        val nextPrayer = prayers.firstOrNull { it.isNext } ?: prayers.firstOrNull { it.id == "fajr" }
        if (nextPrayer != null) {
            views.setTextViewText(R.id.tv_prayer_next_label, "اگلی نماز: ${nextPrayer.nameUrdu}")
            views.setTextViewText(R.id.tv_prayer_next_time, nextPrayer.timeFormatted)
        } else {
            views.setTextViewText(R.id.tv_prayer_next_label, "اگلی نماز: فجر")
            views.setTextViewText(R.id.tv_prayer_next_time, "04:36 AM")
        }

        val fajrData = prayers.firstOrNull { it.id == "fajr" }
        val sunriseData = prayers.firstOrNull { it.id == "sunrise" }
        val dhuhrData = prayers.firstOrNull { it.id == "dhuhr" }
        val asrData = prayers.firstOrNull { it.id == "asr" }
        val maghribData = prayers.firstOrNull { it.id == "maghrib" }
        val ishaData = prayers.firstOrNull { it.id == "isha" }

        fajrData?.let { views.setTextViewText(R.id.tv_fajr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        sunriseData?.let { views.setTextViewText(R.id.tv_sunrise_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        dhuhrData?.let { views.setTextViewText(R.id.tv_dhuhr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        asrData?.let { views.setTextViewText(R.id.tv_asr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        maghribData?.let { views.setTextViewText(R.id.tv_maghrib_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        ishaData?.let { views.setTextViewText(R.id.tv_isha_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateSimplePrayerTimesWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_simple_prayer_times)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_simple_prayer_root, pendingIntent)

        val city = getSavedCity(context)
        val prayers = getTodayPrayerTimes(city)
        val hijriDetails = HijriHelper.getHijriDetails(LocalDate.now(), "ur")

        views.setTextViewText(R.id.tv_sp_city, "📍 ${city.nameUrdu}")
        views.setTextViewText(R.id.tv_sp_hijri_date, hijriDetails.formattedFull)

        val nextPrayer = prayers.firstOrNull { it.isNext } ?: prayers.firstOrNull { it.id == "fajr" }
        if (nextPrayer != null) {
            views.setTextViewText(R.id.tv_sp_next_label, "· اگلی نماز: ${nextPrayer.nameUrdu}")
            views.setTextViewText(R.id.tv_sp_next_time, nextPrayer.timeFormatted)
        } else {
            views.setTextViewText(R.id.tv_sp_next_label, "· اگلی نماز: فجر")
            views.setTextViewText(R.id.tv_sp_next_time, "04:36 AM")
        }

        val activeId = nextPrayer?.id ?: "fajr"
        val prayerBoxes = listOf(
            "fajr" to R.id.ll_sp_fajr,
            "sunrise" to R.id.ll_sp_sunrise,
            "dhuhr" to R.id.ll_sp_zuhr,
            "asr" to R.id.ll_sp_asr,
            "maghrib" to R.id.ll_sp_maghrib,
            "isha" to R.id.ll_sp_isha
        )
        for ((id, viewId) in prayerBoxes) {
            if (id == activeId) {
                views.setInt(viewId, "setBackgroundResource", R.drawable.bg_clean_prayer_box_active)
            } else {
                views.setInt(viewId, "setBackgroundResource", R.drawable.bg_clean_prayer_box)
            }
        }

        val fajrData = prayers.firstOrNull { it.id == "fajr" }
        val sunriseData = prayers.firstOrNull { it.id == "sunrise" }
        val dhuhrData = prayers.firstOrNull { it.id == "dhuhr" }
        val asrData = prayers.firstOrNull { it.id == "asr" }
        val maghribData = prayers.firstOrNull { it.id == "maghrib" }
        val ishaData = prayers.firstOrNull { it.id == "isha" }

        fajrData?.let { views.setTextViewText(R.id.tv_sp_fajr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        sunriseData?.let { views.setTextViewText(R.id.tv_sp_sunrise_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        dhuhrData?.let { views.setTextViewText(R.id.tv_sp_zuhr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        asrData?.let { views.setTextViewText(R.id.tv_sp_asr_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        maghribData?.let { views.setTextViewText(R.id.tv_sp_maghrib_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }
        ishaData?.let { views.setTextViewText(R.id.tv_sp_isha_time, it.timeFormatted.replace(" AM", "").replace(" PM", "")) }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
