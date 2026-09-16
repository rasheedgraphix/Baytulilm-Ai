package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class IslamicCircleClockWidget2 : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            AppWidgetUpdateHelper.updateCircleClock2(context, appWidgetManager, appWidgetId)
        }
    }
}
