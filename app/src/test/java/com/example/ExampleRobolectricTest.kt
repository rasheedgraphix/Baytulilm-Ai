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
    val rv1 = android.widget.RemoteViews(context.packageName, R.layout.widget_circle_clock1)
    val v1 = rv1.apply(context, root)
    org.junit.Assert.assertNotNull(v1)

    val rv2 = android.widget.RemoteViews(context.packageName, R.layout.widget_circle_clock2)
    val v2 = rv2.apply(context, root)
    org.junit.Assert.assertNotNull(v2)

    val rvSimple = android.widget.RemoteViews(context.packageName, R.layout.widget_simple_clock)
    val vSimple = rvSimple.apply(context, root)
    org.junit.Assert.assertNotNull(vSimple)

    val rvPrayer = android.widget.RemoteViews(context.packageName, R.layout.widget_prayer_times)
    val vPrayer = rvPrayer.apply(context, root)
    org.junit.Assert.assertNotNull(vPrayer)

    val bitmap1 = com.example.widget.ClockBitmapHelper.renderKaabaClockBitmap(context, 400)
    org.junit.Assert.assertNotNull(bitmap1)

    val bitmap2 = com.example.widget.ClockBitmapHelper.renderNeonTacticalClockBitmap(400)
    org.junit.Assert.assertNotNull(bitmap2)
  }
}
