package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class IslamicCircleClockWidget1 : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            AppWidgetUpdateHelper.updateCircleClock1(context, appWidgetManager, appWidgetId)
        }
    }
}
