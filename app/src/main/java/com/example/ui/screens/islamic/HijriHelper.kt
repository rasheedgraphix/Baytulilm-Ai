package com.example.ui.screens.islamic

import java.time.LocalDate
import java.time.YearMonth
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

object HijriHelper {
    private val hijrahChronology = HijrahChronology.INSTANCE

    val monthNamesUrdu = listOf(
        "محرم الحرام", "صفر المظفر", "ربیع الاول", "ربیع الثانی",
        "جمادی الاولیٰ", "جمادی الثانیہ", "رجب المرجب", "شعبان المعظم",
        "رمضان المبارک", "شوال المکرم", "ذوالقعدہ", "ذوالحجہ"
    )

    val monthNamesUrduShort = listOf(
        "محرم", "صفر", "ر۔الاول", "ر۔الثانی",
        "ج۔الاولیٰ", "ج۔الثانیہ", "رجب", "شعبان",
        "رمضان", "شوال", "ذوالقعدہ", "ذوالحجہ"
    )

    val monthNamesPashto = listOf(
        "محرم", "صفر", "ربیع الاول", "ربیع الثانی",
        "جمادی الاولی", "جمادی الثانی", "رجب", "شعبان",
        "رمضان", "شوال", "ذوالقعده", "ذوالحجه"
    )

    val monthNamesArabic = listOf(
        "محرّم", "صفر", "ربيع الأوّل", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوّال", "ذو القعدة", "ذو الحجّة"
    )

    val monthNamesEnglish = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    data class HijriDateDetails(
        val day: Int,
        val month: Int,
        val year: Int,
        val monthName: String,
        val formattedFull: String,
        val shortFormatted: String
    )

    fun getHijriDetails(gregorianDate: LocalDate, langCode: String = "ur"): HijriDateDetails {
        return try {
            val hijrahDate: HijrahDate = hijrahChronology.date(gregorianDate)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
            val year = hijrahDate.get(ChronoField.YEAR_OF_ERA)

            val monthName = when (langCode) {
                "ps" -> monthNamesPashto.getOrElse(month - 1) { "صفر" }
                "en" -> monthNamesEnglish.getOrElse(month - 1) { "Safar" }
                "ar" -> monthNamesArabic.getOrElse(month - 1) { "صفر" }
                else -> monthNamesUrdu.getOrElse(month - 1) { "صفر المظفر" }
            }

            val shortMonth = when (langCode) {
                "ps" -> monthNamesPashto.getOrElse(month - 1) { "صفر" }
                "en" -> monthNamesEnglish.getOrElse(month - 1) { "Saf" }
                "ar" -> monthNamesArabic.getOrElse(month - 1) { "صفر" }
                else -> monthNamesUrduShort.getOrElse(month - 1) { "صفر" }
            }

            val suffix = if (langCode == "en") "AH" else if (langCode == "ar" || langCode == "ps") "هـ" else "ھ"
            val formattedFull = "$day $monthName $year $suffix"
            val shortFormatted = "$day $shortMonth"

            HijriDateDetails(day, month, year, monthName, formattedFull, shortFormatted)
        } catch (e: Exception) {
            HijriDateDetails(14, 2, 1448, "صفر", "14 صفر 1448 ھ", "14 صفر")
        }
    }

    /**
     * Dynamically calculates today's Hijri date based on current system Gregorian date.
     */
    fun getTodayHijriDate(langCode: String = "ur"): String {
        return getHijriDetails(LocalDate.now(), langCode).formattedFull
    }

    /**
     * Converts any Gregorian LocalDate to a localized Hijri Date string with day, month name, and year.
     */
    fun getHijriDateFormatted(gregorianDate: LocalDate, langCode: String = "ur"): String {
        return getHijriDetails(gregorianDate, langCode).formattedFull
    }

    /**
     * Returns the Hijri Month span for a given Gregorian YearMonth.
     * e.g., "صفر المظفر — ربیع الاول 1448 ھ"
     */
    fun getHijriMonthSpan(yearMonth: YearMonth, langCode: String = "ur"): String {
        return try {
            val firstDay = yearMonth.atDay(1)
            val lastDay = yearMonth.atEndOfMonth()

            val h1 = getHijriDetails(firstDay, langCode)
            val h2 = getHijriDetails(lastDay, langCode)

            val suffix = if (langCode == "en") "AH" else if (langCode == "ar" || langCode == "ps") "هـ" else "ھ"

            if (h1.month == h2.month && h1.year == h2.year) {
                "${h1.monthName} ${h1.year} $suffix"
            } else if (h1.year == h2.year) {
                "${h1.monthName} / ${h2.monthName} ${h1.year} $suffix"
            } else {
                "${h1.monthName} ${h1.year} / ${h2.monthName} ${h2.year} $suffix"
            }
        } catch (e: Exception) {
            "صفر المظفر 1448 ھ"
        }
    }

    /**
     * Backward-compatible simple format used in Calendar grids
     */
    fun getHijriDate(gregorianDate: LocalDate): String {
        val details = getHijriDetails(gregorianDate, "ur")
        return "${details.day} ${details.monthName}"
    }
}
