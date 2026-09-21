package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.ui.screens.islamic.HijriHelper
import com.example.util.LocationHelper
import com.example.util.PrayerTimeCalculator
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class IslamicClockCardWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE ||
            action == Intent.ACTION_TIME_TICK ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_BOOT_COMPLETED
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, IslamicClockCardWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (id in appWidgetIds) {
                updateWidget(context, appWidgetManager, id)
            }
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_islamic_clock_card)

                // Click opens MainActivity
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_luxury_clock_root, pendingIntent)

                // 1. Current Time (hh:mm a)
                try {
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                    views.setTextViewText(R.id.tv_lux_time, timeFormat.format(Date()))
                } catch (_: Throwable) {}

                // 2. Gregorian Date (dd MMM yyyy, EEEE)
                try {
                    val gregFormat = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
                    views.setTextViewText(R.id.tv_lux_gregorian, gregFormat.format(Date()))
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tv_lux_gregorian, "Today")
                }

                // 3. Hijri Date via HijriHelper in golden accent
                try {
                    val hijriDate = getHijriDate()
                    views.setTextViewText(R.id.tv_lux_hijri, hijriDate)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tv_lux_hijri, "١٤٤٨ هـ")
                }

                // 4. Location Name via LocationHelper
                try {
                    val locationName = LocationHelper.getCityName(context)
                    views.setTextViewText(R.id.tv_lux_location, locationName)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tv_lux_location, "Rawalpindi")
                }

                // 5. Next Prayer Name & Time from PrayerTimeCalculator
                try {
                    val nextPrayer = PrayerTimeCalculator.getNextPrayer(context)
                    views.setTextViewText(R.id.tv_lux_next_prayer_label, "Next: ${nextPrayer.name}")
                    views.setTextViewText(R.id.tv_lux_next_prayer_time, nextPrayer.time)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tv_lux_next_prayer_label, "Next: Fajr")
                    views.setTextViewText(R.id.tv_lux_next_prayer_time, "04:36 AM")
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        private fun getHijriDate(): String {
            return try {
                val h = HijriHelper.getHijriDetails(LocalDate.now(), "ar")
                h.formattedFull
            } catch (e: Throwable) {
                "١٤٤٨ هـ"
            }
        }
    }
}
