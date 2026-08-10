package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

data class CityLocation(
    val nameUrdu: String,
    val nameEnglish: String,
    val lat: Double,
    val lng: Double,
    val timeZoneId: String = "Asia/Karachi"
)

data class PrayerTimeData(
    val id: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val nameArabic: String,
    val timeFormatted: String,
    val dateObj: Date,
    val isNext: Boolean = false
)

object PrayerTimeCalculator {

    val defaultCities = listOf(
        // Pakistan Major Cities
        CityLocation("اسلام آباد", "Islamabad", 33.6844, 73.0479, "Asia/Karachi"),
        CityLocation("لاہور", "Lahore", 31.5204, 74.3587, "Asia/Karachi"),
        CityLocation("کراچی", "Karachi", 24.8607, 67.0011, "Asia/Karachi"),
        CityLocation("پشاور", "Peshawar", 34.0151, 71.5249, "Asia/Karachi"),
        CityLocation("راولپنڈی", "Rawalpindi", 33.5651, 73.0169, "Asia/Karachi"),
        CityLocation("کوئٹہ", "Quetta", 30.1798, 66.9750, "Asia/Karachi"),
        CityLocation("ملتان", "Multan", 30.1575, 71.5249, "Asia/Karachi"),
        CityLocation("فیصل آباد", "Faisalabad", 31.4504, 73.1350, "Asia/Karachi"),
        CityLocation("گجرانوالہ", "Gujranwala", 32.1877, 74.1945, "Asia/Karachi"),
        CityLocation("حیدرآباد", "Hyderabad", 25.3960, 68.3578, "Asia/Karachi"),
        CityLocation("سیالکوٹ", "Sialkot", 32.4945, 74.5229, "Asia/Karachi"),
        CityLocation("بہاولپور", "Bahawalpur", 29.3544, 71.6911, "Asia/Karachi"),
        CityLocation("سکھر", "Sukkur", 27.7052, 68.8574, "Asia/Karachi"),
        CityLocation("مردان", "Mardan", 34.1986, 72.0404, "Asia/Karachi"),
        CityLocation("ایبٹ آباد", "Abbottabad", 34.1688, 73.2215, "Asia/Karachi"),
        CityLocation("سوات / مینگورہ", "Swat / Mingora", 34.7717, 72.3602, "Asia/Karachi"),
        CityLocation("گلگت", "Gilgit", 35.9208, 74.3089, "Asia/Karachi"),
        CityLocation("مظفرآباد", "Muzaffarabad", 34.3700, 73.4711, "Asia/Karachi"),

        // International Cities
        CityLocation("مکہ مکرمہ", "Makkah", 21.4225, 39.8262, "Asia/Riyadh"),
        CityLocation("مدینہ منورہ", "Madinah", 24.5247, 39.5692, "Asia/Riyadh"),
        CityLocation("ریاض", "Riyadh", 24.7136, 46.6753, "Asia/Riyadh"),
        CityLocation("جدہ", "Jeddah", 21.4858, 39.1925, "Asia/Riyadh"),
        CityLocation("دبئی", "Dubai", 25.2048, 55.2708, "Asia/Dubai"),
        CityLocation("ابوظہبی", "Abu Dhabi", 24.4539, 54.3773, "Asia/Dubai"),
        CityLocation("استنبول", "Istanbul", 41.0082, 28.9784, "Europe/Istanbul"),
        CityLocation("قاہرہ", "Cairo", 30.0444, 31.2357, "Africa/Cairo"),
        CityLocation("نئی دہلی", "New Delhi", 28.6139, 77.2090, "Asia/Kolkata"),
        CityLocation("ممبئی", "Mumbai", 19.0760, 72.8777, "Asia/Kolkata"),
        CityLocation("ڈھاکہ", "Dhaka", 23.8103, 90.4125, "Asia/Dhaka"),
        CityLocation("کولالمپور", "Kuala Lumpur", 3.1390, 101.6869, "Asia/Kuala_Lumpur"),
        CityLocation("جکارتہ", "Jakarta", -6.2088, 106.8456, "Asia/Jakarta"),
        CityLocation("لندن", "London", 51.5074, -0.1278, "Europe/London"),
        CityLocation("نیویارک", "New York", 40.7128, -74.0060, "America/New_York"),
        CityLocation("ٹورنٹو", "Toronto", 43.6532, -79.3832, "America/Toronto"),
        CityLocation("سڈنی", "Sydney", -33.8688, 151.2093, "Australia/Sydney")
    )

    fun calculatePrayerTimes(
        lat: Double,
        lng: Double,
        date: Date = Date(),
        overrideTimeZone: TimeZone? = null
    ): List<PrayerTimeData> {
        val timeZone: TimeZone = overrideTimeZone ?: when {
            lat in 23.0..37.0 && lng in 60.0..78.0 -> TimeZone.getTimeZone("Asia/Karachi")
            else -> TimeZone.getDefault()
        }

        val cal = Calendar.getInstance(timeZone)
        cal.time = date

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // Astronomical Julian Date
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        val julianDate = (365.25 * (y + 4716)).toLong() + (30.6001 * (m + 1)).toLong() + day + b - 1524.5
        val d = julianDate - 2451545.0

        // Sun's mean anomaly and ecliptic longitude
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * dSin(g) + 0.020 * dSin(2 * g))

        val ob = 23.439 - 0.00000036 * d
        val ra = fixAngle(dAtan2(dCos(ob) * dSin(l), dCos(l)) / 15.0)
        val declination = dAsin(dSin(ob) * dSin(l))

        // Equation of Time (in hours)
        var eotDiff = fixAngle(l) - fixAngle(ra * 15.0)
        if (eotDiff > 180.0) eotDiff -= 360.0
        if (eotDiff < -180.0) eotDiff += 360.0
        val eqTimeHours = eotDiff / 15.0

        // TimeZone offset in hours
        val timeZoneOffsetHours = timeZone.getOffset(cal.timeInMillis) / 3600000.0

        // Solar Noon
        val solarNoonHours = fixHour(12.0 + timeZoneOffsetHours - lng / 15.0 - eqTimeHours)

        // Dhuhr: Solar Noon + 1 minute (safety margin)
        val dhuhrHours = solarNoonHours + (1.0 / 60.0)

        // Sunrise & Sunset (Maghrib) Angle = -0.833 degrees
        val sunAlt = -0.833
        val sunriseHour = solarNoonHours - sunAngleTime(sunAlt, lat, declination)
        val sunsetHour = solarNoonHours + sunAngleTime(sunAlt, lat, declination)

        // University of Karachi Method ONLY for Fajr & Isha (18.0 degrees)
        val fajrHour = solarNoonHours - sunAngleTime(-18.0, lat, declination)
        val ishaHour = solarNoonHours + sunAngleTime(-18.0, lat, declination)

        // Asr: Hanafi juristic method (shadow factor = 2)
        val asrShadowFactor = 2.0
        val noonShadowTan = dTan(abs(lat - declination))
        val asrAlt = dAtan(1.0 / (asrShadowFactor + noonShadowTan))
        val asrHour = solarNoonHours + sunAngleTime(asrAlt, lat, declination)

        // Create Date Objects
        val fajrDate = createDate(cal, fajrHour, timeZone)
        val sunriseDate = createDate(cal, sunriseHour, timeZone)
        val dhuhrDate = createDate(cal, dhuhrHours, timeZone)
        val asrDate = createDate(cal, asrHour, timeZone)
        val maghribDate = createDate(cal, sunsetHour, timeZone)
        val ishaDate = createDate(cal, ishaHour, timeZone)

        val now = Date()
        val rawList = listOf(
            PrayerTimeData("fajr", "فجر", "Fajr", "الفجر", formatTime(fajrDate, timeZone), fajrDate),
            PrayerTimeData("sunrise", "طلوعِ آفتاب", "Sunrise", "الشروق", formatTime(sunriseDate, timeZone), sunriseDate),
            PrayerTimeData("dhuhr", "ظہر", "Dhuhr", "الظهر", formatTime(dhuhrDate, timeZone), dhuhrDate),
            PrayerTimeData("asr", "عصر", "Asr", "العصر", formatTime(asrDate, timeZone), asrDate),
            PrayerTimeData("maghrib", "مغرب", "Maghrib", "المغرب", formatTime(maghribDate, timeZone), maghribDate),
            PrayerTimeData("isha", "عشاء", "Isha", "العشاء", formatTime(ishaDate, timeZone), ishaDate)
        )

        // Determine next prayer based on current time
        val nextPrayer = rawList.firstOrNull { it.dateObj.after(now) } ?: rawList.first()

        return rawList.map { prayer ->
            prayer.copy(isNext = prayer.id == nextPrayer.id)
        }
    }

    private fun sunAngleTime(angle: Double, lat: Double, declination: Double): Double {
        val cosH = (dSin(angle) - dSin(lat) * dSin(declination)) / (dCos(lat) * dCos(declination))
        val clampedCosH = cosH.coerceIn(-1.0, 1.0)
        return dAcos(clampedCosH) / 15.0
    }

    private fun createDate(cal: Calendar, hourDec: Double, timeZone: TimeZone): Date {
        val c = cal.clone() as Calendar
        c.timeZone = timeZone
        val h = fixHour(hourDec)
        val hours = h.toInt()
        val minutesDec = (h - hours) * 60.0
        val minutes = minutesDec.toInt()
        val seconds = ((minutesDec - minutes) * 60.0).toInt()

        c.set(Calendar.HOUR_OF_DAY, hours.coerceIn(0, 23))
        c.set(Calendar.MINUTE, minutes.coerceIn(0, 59))
        c.set(Calendar.SECOND, seconds.coerceIn(0, 59))
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    private fun formatTime(date: Date, timeZone: TimeZone): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        sdf.timeZone = timeZone
        return sdf.format(date)
    }

    private fun dSin(deg: Double) = sin(Math.toRadians(deg))
    private fun dCos(deg: Double) = cos(Math.toRadians(deg))
    private fun dTan(deg: Double) = tan(Math.toRadians(deg))
    private fun dAsin(valIn: Double) = Math.toDegrees(asin(valIn.coerceIn(-1.0, 1.0)))
    private fun dAcos(valIn: Double) = Math.toDegrees(acos(valIn.coerceIn(-1.0, 1.0)))
    private fun dAtan(valIn: Double) = Math.toDegrees(atan(valIn))
    private fun dAtan2(y: Double, x: Double) = Math.toDegrees(atan2(y, x))

    private fun fixAngle(a: Double): Double {
        var b = a - 360.0 * (a / 360.0).toInt()
        if (b < 0) b += 360.0
        return b
    }

    private fun fixHour(a: Double): Double {
        var b = a - 24.0 * (a / 24.0).toInt()
        if (b < 0) b += 24.0
        return b
    }
}
