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

class IslamicClockWidget : AppWidgetProvider() {

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
            val thisWidget = ComponentName(context, IslamicClockWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (id in appWidgetIds) {
                updateWidget(context, appWidgetManager, id)
            }
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_islamic_clock)

                // Click on widget opens MainActivity
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_islamic_clock_root, pendingIntent)

                // 1. Time (HH:mm a)
                try {
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                    views.setTextViewText(R.id.tvTime, timeFormat.format(Date()))
                } catch (_: Throwable) {}

                // 2. Gregorian Date
                try {
                    val gregFormat = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
                    views.setTextViewText(R.id.tvGregorian, gregFormat.format(Date()))
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tvGregorian, "Today")
                }

                // 3. Hijri Date
                try {
                    val hijriDate = getHijriDate()
                    views.setTextViewText(R.id.tvHijri, hijriDate)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tvHijri, "١٤٤٨ هـ")
                }

                // 4. Location
                try {
                    val locationName = LocationHelper.getCityName(context)
                    views.setTextViewText(R.id.tvLocation, locationName)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tvLocation, "Rawalpindi")
                }

                // 5. Next Prayer
                try {
                    val nextPrayer = PrayerTimeCalculator.getNextPrayer(context)
                    views.setTextViewText(R.id.tvNextPrayerLabel, "Next: ${nextPrayer.name}")
                    views.setTextViewText(R.id.tvNextPrayerTime, nextPrayer.time)
                } catch (_: Throwable) {
                    views.setTextViewText(R.id.tvNextPrayerLabel, "Next Prayer")
                    views.setTextViewText(R.id.tvNextPrayerTime, "--:--")
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
