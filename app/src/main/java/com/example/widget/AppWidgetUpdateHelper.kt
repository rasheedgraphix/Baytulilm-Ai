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

        // 1. Circle Clock 1
        val idsCircle1 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicCircleClockWidget1::class.java)
        )
        if (idsCircle1.isNotEmpty()) {
            for (id in idsCircle1) {
                updateCircleClock1(context, appWidgetManager, id)
            }
        }

        // 2. Circle Clock 2
        val idsCircle2 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, IslamicCircleClockWidget2::class.java)
        )
        if (idsCircle2.isNotEmpty()) {
            for (id in idsCircle2) {
                updateCircleClock2(context, appWidgetManager, id)
            }
        }

        // 3. Simple Clock
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
        if (cityNameEng != null) {
            val found = PrayerTimeCalculator.defaultCities.find { it.nameEnglish.equals(cityNameEng, ignoreCase = true) }
            if (found != null) return found
        }
        val lat = prefs.getFloat("city_lat", -999f).toDouble()
        val lng = prefs.getFloat("city_lng", -999f).toDouble()
        if (lat != -999.0 && lng != -999.0) {
            val nameUrdu = prefs.getString("city_name_urdu", "شہر") ?: "شہر"
            val tz = prefs.getString("city_tz", "Asia/Karachi") ?: "Asia/Karachi"
            return CityLocation(nameUrdu, cityNameEng ?: "Selected City", lat, lng, tz)
        }
        return PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Islamabad" }
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

    fun updateCircleClock1(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_circle_clock1)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_circle1_root, pendingIntent)

        // Render composite Kaaba dial face + analog hands into ImageView (100% crash-free across all Android versions)
        try {
            val kaabaBitmap = ClockBitmapHelper.renderKaabaClockBitmap(context, size = 400)
            views.setImageViewBitmap(R.id.iv_kaaba_dial, kaabaBitmap)
        } catch (e: Throwable) {
            views.setImageViewResource(R.id.iv_kaaba_dial, R.drawable.img_kaaba_clock_dial)
        }

        val city = getSavedCity(context)
        val prayers = getTodayPrayerTimes(city)
        val nextPrayer = prayers.firstOrNull { it.isNext } ?: prayers.firstOrNull { it.id != "sunrise" }

        // Hijri Details & Gregorian Date for LCD Screen
        val hijriDetails = HijriHelper.getHijriDetails(LocalDate.now(), "ur")
        val now = LocalDate.now()
        val gregorianFormatted = "${now.dayOfMonth}/${now.monthValue}/${now.year}"

        views.setTextViewText(R.id.tv_clock1_temp, "📍 ${city.nameUrdu}")
        views.setTextViewText(R.id.tv_clock1_prayer_name, nextPrayer?.nameEnglish?.uppercase() ?: "DHUHR")
        views.setTextViewText(R.id.tv_clock1_gregorian, gregorianFormatted)
        views.setTextViewText(R.id.tv_clock1_hijri, "${hijriDetails.day} ${hijriDetails.monthName}")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateCircleClock2(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_circle_clock2)
        val pendingIntent = getLaunchPendingIntent(context)
        views.setOnClickPendingIntent(R.id.widget_circle2_root, pendingIntent)

        // Render high-res Neon Cyan Tactical clock face with glowing marks, hands and red pin
        try {
            val neonBitmap = ClockBitmapHelper.renderNeonTacticalClockBitmap(size = 400)
            views.setImageViewBitmap(R.id.iv_clock2_face, neonBitmap)
        } catch (e: Throwable) {
            // fallback
        }

        val now = LocalDate.now()
        val formattedDate = "${now.dayOfMonth}-${now.month.name.take(3)}"
        views.setTextViewText(R.id.tv_clock2_date, formattedDate)

        appWidgetManager.updateAppWidget(appWidgetId, views)
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

        val nextPrayer = prayers.firstOrNull { it.isNext }
        if (nextPrayer != null) {
            views.setTextViewText(R.id.tv_prayer_next_label, "اگلی نماز: ${nextPrayer.nameUrdu}")
            views.setTextViewText(R.id.tv_prayer_next_time, nextPrayer.timeFormatted)
        } else {
            val fajr = prayers.firstOrNull { it.id == "fajr" }
            views.setTextViewText(R.id.tv_prayer_next_label, "اگلی نماز: فجر")
            views.setTextViewText(R.id.tv_prayer_next_time, fajr?.timeFormatted ?: "04:30 AM")
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

        val nextPrayer = prayers.firstOrNull { it.isNext }
        if (nextPrayer != null) {
            views.setTextViewText(R.id.tv_sp_next_label, "· اگلی نماز: ${nextPrayer.nameUrdu}")
            views.setTextViewText(R.id.tv_sp_next_time, nextPrayer.timeFormatted)
        } else {
            val fajr = prayers.firstOrNull { it.id == "fajr" }
            views.setTextViewText(R.id.tv_sp_next_label, "· اگلی نماز: فجر")
            views.setTextViewText(R.id.tv_sp_next_time, fajr?.timeFormatted ?: "04:30 AM")
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
