package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Baytul Ilm AI", appName)
  }

  @Test
  fun testWidgetInflation() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val root = android.widget.FrameLayout(context)
    val rvSimple = android.widget.RemoteViews(context.packageName, R.layout.widget_simple_clock)
    val vSimple = rvSimple.apply(context, root)
    org.junit.Assert.assertNotNull(vSimple)

    val rvPrayer = android.widget.RemoteViews(context.packageName, R.layout.widget_prayer_times)
    val vPrayer = rvPrayer.apply(context, root)
    org.junit.Assert.assertNotNull(vPrayer)

    val rvSimplePrayer = android.widget.RemoteViews(context.packageName, R.layout.widget_simple_prayer_times)
    val vSimplePrayer = rvSimplePrayer.apply(context, root)
    org.junit.Assert.assertNotNull(vSimplePrayer)

    val rvIslamicClock = android.widget.RemoteViews(context.packageName, R.layout.widget_islamic_clock)
    val vIslamicClock = rvIslamicClock.apply(context, root)
    org.junit.Assert.assertNotNull(vIslamicClock)

    val rvIslamicClockCard = android.widget.RemoteViews(context.packageName, R.layout.widget_islamic_clock_card)
    val vIslamicClockCard = rvIslamicClockCard.apply(context, root)
    org.junit.Assert.assertNotNull(vIslamicClockCard)
  }

  @Test
  fun testPrayerCalculation() {
    val city = com.example.util.PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Rawalpindi" }
    val times = com.example.util.PrayerTimeCalculator.calculatePrayerTimes(
      lat = city.lat,
      lng = city.lng,
      date = java.util.Date(),
      overrideTimeZone = java.util.TimeZone.getTimeZone(city.timeZoneId)
    )
    for (p in times) {
      println("TEST_PRAYER: ${p.id} -> ${p.timeFormatted}")
    }
  }
}
