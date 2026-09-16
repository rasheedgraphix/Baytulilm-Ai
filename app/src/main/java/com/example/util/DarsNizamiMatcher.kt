package com.example.util

import com.example.data.model.BookDoc
import com.example.data.model.BookEntity

object DarsNizamiMatcher {

    /**
     * Matches a book's darja string with a class ID, class name, or class order (1 to 8).
     */
    fun matchesClass(bookDarja: String, classIdOrName: String, order: Int? = null): Boolean {
        val b = bookDarja.trim()
        val c = classIdOrName.trim().lowercase()
        val ord = order ?: when {
            c.contains("ula") || c.contains("awwal") || c.contains("اول") || c.contains("1") -> 1
            c.contains("sania") || c.contains("sani") || c.contains("ثانی") || c.contains("2") -> 2
            c.contains("salisa") || c.contains("salsa") || c.contains("ثالث") || c.contains("3") -> 3
            c.contains("rabia") || c.contains("rabea") || c.contains("رابع") || c.contains("4") -> 4
            c.contains("khamisa") || c.contains("khamsa") || c.contains("خامس") || c.contains("5") -> 5
            c.contains("sadisa") || c.contains("سادس") || c.contains("6") -> 6
            c.contains("sabia") || c.contains("sabeaa") || c.contains("سابع") || c.contains("موقوف") || c.contains("7") -> 7
            c.contains("samina") || c.contains("dora") || c.contains("دورہ") || c.contains("ثامن") || c.contains("8") -> 8
            else -> 0
        }

        return when (ord) {
            1 -> b.contains("ula", ignoreCase = true) || b.contains("اول", ignoreCase = true) || b.contains("aula", ignoreCase = true) || b.contains("1")
            2 -> b.contains("sania", ignoreCase = true) || b.contains("sani", ignoreCase = true) || b.contains("ثانی", ignoreCase = true) || b.contains("2")
            3 -> b.contains("salisa", ignoreCase = true) || b.contains("salsa", ignoreCase = true) || b.contains("ثالث", ignoreCase = true) || b.contains("3")
            4 -> b.contains("rabia", ignoreCase = true) || b.contains("rabea", ignoreCase = true) || b.contains("رابع", ignoreCase = true) || b.contains("4")
            5 -> b.contains("khamisa", ignoreCase = true) || b.contains("khamsa", ignoreCase = true) || b.contains("خامس", ignoreCase = true) || b.contains("5")
            6 -> b.contains("sadisa", ignoreCase = true) || b.contains("سادس", ignoreCase = true) || b.contains("6")
            7 -> b.contains("sabia", ignoreCase = true) || b.contains("sabeaa", ignoreCase = true) || b.contains("سابع", ignoreCase = true) || b.contains("موقوف", ignoreCase = true) || b.contains("7")
            8 -> b.contains("samina", ignoreCase = true) || b.contains("ثامن", ignoreCase = true) || b.contains("دورہ", ignoreCase = true) || b.contains("dora", ignoreCase = true) || b.contains("8")
            else -> b.contains(classIdOrName, ignoreCase = true) || classIdOrName.contains(b, ignoreCase = true)
        }
    }

    /**
     * Filters all books belonging to a specific class (BookEntity list).
     */
    fun getBooksForClass(allBooks: List<BookEntity>, classIdOrName: String, order: Int? = null): List<BookEntity> {
        return allBooks.filter { matchesClass(it.darja, classIdOrName, order) }
    }

    /**
     * Filters all books belonging to a specific class (BookDoc list).
     */
    fun getBookDocsForClass(allBooks: List<BookDoc>, classIdOrName: String, order: Int? = null): List<BookDoc> {
        return allBooks.filter { matchesClass(it.darja, classIdOrName, order) }
    }

    /**
     * Returns the standardized list of unique subjects present in a class (BookEntity list).
     */
    fun getDistinctSubjectsForClass(allBooks: List<BookEntity>, classIdOrName: String, order: Int? = null): List<String> {
        return getBooksForClass(allBooks, classIdOrName, order)
            .map { normalizeSubject(it.subject) }
            .filter { it.isNotBlank() }
            .distinct()
    }

    /**
     * Returns the standardized list of unique subjects present in a class (BookDoc list).
     */
    fun getDistinctSubjectsForBookDocs(allBooks: List<BookDoc>, classIdOrName: String, order: Int? = null): List<String> {
        return getBookDocsForClass(allBooks, classIdOrName, order)
            .map { normalizeSubject(it.subject) }
            .filter { it.isNotBlank() }
            .distinct()
    }

    /**
     * Standardizes subject names across languages (Urdu / English / Arabic variations).
     */
    fun normalizeSubject(rawSubject: String): String {
        val s = rawSubject.trim()
        return when {
            s.equals("Nahw", ignoreCase = true) || s.contains("نحو") -> "نحو"
            s.equals("Sarf", ignoreCase = true) || s.contains("صرف") -> "صرف"
            s.equals("Fiqh", ignoreCase = true) || s.contains("فقہ") || s.contains("فقه") -> {
                if (s.contains("Miras", ignoreCase = true) || s.contains("Inheritance", ignoreCase = true) || s.contains("میراث") || s.contains("فرائض")) "فقہ / میراث"
                else "فقہ"
            }
            s.contains("Usul", ignoreCase = true) && (s.contains("Fiqh", ignoreCase = true) || s.contains("فقہ")) -> "اصول فقہ"
            s.contains("اصول فقہ") || s.contains("اصول الفقہ") || s.contains("اصولِ فقہ") -> "اصول فقہ"
            s.contains("Usul", ignoreCase = true) && (s.contains("Hadith", ignoreCase = true) || s.contains("حدیث")) -> "اصول حدیث"
            s.contains("اصول حدیث") || s.contains("اصول الحدیث") -> "اصول حدیث"
            s.equals("Hadith", ignoreCase = true) || s.contains("حدیث") -> "حدیث"
            s.equals("Tafseer", ignoreCase = true) || s.contains("تفسیر") -> "تفسیر"
            s.contains("Balagh", ignoreCase = true) || s.contains("بلاغت") || s.contains("بلاغۃ") -> "بلاغت"
            s.equals("Mantiq", ignoreCase = true) || s.contains("منطق") -> "منطق"
            s.contains("Aqeed", ignoreCase = true) || s.contains("Aqaid", ignoreCase = true) || s.contains("عقائد") || s.contains("عقیدہ") -> "عقائد"
            s.contains("Arabic Literature", ignoreCase = true) || s.contains("Adab", ignoreCase = true) || s.contains("عربی ادب") || s.contains("ادب") -> "عربی ادب"
            s.equals("Insha", ignoreCase = true) || s.contains("انشاء") -> "انشاء"
            s.equals("Tajweed", ignoreCase = true) || s.contains("تجوید") -> "تجوید"
            s.contains("Falsaf", ignoreCase = true) || s.contains("فلسفہ") || s.contains("فلسفۃ") -> "فلسفہ"
            s.equals("Persian", ignoreCase = true) || s.contains("فارسی") -> "فارسی"
            s.contains("Ethic", ignoreCase = true) || s.contains("اخلاق") -> "اخلاقیات"
            s.contains("Paper", ignoreCase = true) || s.contains("Exam", ignoreCase = true) || s.contains("امتحان") || s.contains("پرچہ") -> "امتحانات"
            s.contains("Histor", ignoreCase = true) || s.contains("تاریخ") -> "تاریخ اسلام"
            s.contains("Falak", ignoreCase = true) || s.contains("Astronomy", ignoreCase = true) || s.contains("فلکیات") -> "فلکیات"
            else -> s
        }
    }

    /**
     * Extracts class order (1-8) from class ID, name, or explicit order.
     */
    fun getClassOrder(classIdOrName: String, order: Int? = null): Int {
        if (order != null && order in 1..8) return order
        val c = classIdOrName.trim().lowercase()
        return when {
            c.contains("ula") || c.contains("awwal") || c.contains("اول") || c.contains("1") -> 1
            c.contains("sania") || c.contains("sani") || c.contains("ثانی") || c.contains("2") -> 2
            c.contains("salisa") || c.contains("salsa") || c.contains("ثالث") || c.contains("3") -> 3
            c.contains("rabia") || c.contains("rabea") || c.contains("رابع") || c.contains("4") -> 4
            c.contains("khamisa") || c.contains("khamsa") || c.contains("خامس") || c.contains("5") -> 5
            c.contains("sadisa") || c.contains("سادس") || c.contains("6") -> 6
            c.contains("sabia") || c.contains("sabeaa") || c.contains("سابع") || c.contains("موقوف") || c.contains("7") -> 7
            c.contains("samina") || c.contains("dora") || c.contains("دورہ") || c.contains("ثامن") || c.contains("8") -> 8
            else -> 1
        }
    }

    /**
     * Returns the prominent Arabic class name (e.g., الصف الأول, الصف الثاني, etc.)
     */
    fun getArabicClassTitle(classIdOrName: String, order: Int? = null): String {
        return when (getClassOrder(classIdOrName, order)) {
            1 -> "الصف الأول"
            2 -> "الصف الثاني"
            3 -> "الصف الثالث"
            4 -> "الصف الرابع"
            5 -> "الصف الخامس"
            6 -> "الصف السادس"
            7 -> "الصف السابع"
            8 -> "الصف الثامن"
            else -> "الصف الأول"
        }
    }

    /**
     * Returns the Arabic numeral for the class order (١, ٢, ٣, etc.)
     */
    fun getArabicNumeral(classIdOrName: String, order: Int? = null): String {
        return when (getClassOrder(classIdOrName, order)) {
            1 -> "١"
            2 -> "٢"
            3 -> "٣"
            4 -> "٤"
            5 -> "٥"
            6 -> "٦"
            7 -> "٧"
            8 -> "٨"
            else -> "١"
        }
    }

    /**
     * Matches a subject query with book subjects.
     */
    /**
     * Determines whether a book entity is a Sharh / Hashiyah / Translation / Key / Notes
     * or an Asal Kitab (Original Textbook / Matn).
     */
    fun isSharah(title: String, type: String = "", description: String = ""): Boolean {
        val t = "$title $type $description".lowercase()
        val typeL = type.lowercase()
        val titleL = title.lowercase()

        // Explicit types that denote Shuroohat / Helpers
        if (typeL.contains("شرح") || typeL.contains("حاشیہ") || typeL.contains("حواشی") ||
            typeL.contains("ترجمہ") || typeL.contains("تقریر") || typeL.contains("خلاصہ") ||
            typeL.contains("حل") || typeL.contains("نوٹس") || typeL.contains("نوٹ") ||
            typeL.contains("فرہنگ") || typeL.contains("کلید") || typeL.contains("تعلیق") ||
            typeL.contains("تسہیل") || typeL.contains("افادات") || typeL.contains("دروس") ||
            typeL.contains("توضیح") || typeL.contains("تراجم") || typeL.contains("شروح") ||
            typeL.contains("تفسیر") || typeL.contains("پرچہ") || typeL.contains("امتحان")) {
            // Note: If type is explicitly "اصل کتاب", "درسی کتاب", "اصل متن" and NOT "مع شرح", check title
            if ((typeL.contains("اصل کتاب") || typeL.contains("درسی کتاب") || typeL.contains("اصل متن")) &&
                !typeL.contains("مع شرح") && !typeL.contains("مع حاشیہ") && !typeL.contains("شرح")) {
                // fall through to title check
            } else {
                return true
            }
        }

        // Title checks
        if (titleL.contains("شرح") || titleL.contains("شروح") || titleL.contains("حاشیہ") ||
            titleL.contains("حواشی") || titleL.contains("تقریر") || titleL.contains("تقاریر") ||
            titleL.contains("تسہیل") || titleL.contains("تشریح") || titleL.contains("افادات") ||
            titleL.contains("کلید") || titleL.contains("تلخیص") || titleL.contains("خلاصہ") ||
            titleL.contains("دروس") || titleL.contains("ترجمہ") || titleL.contains("توضیح") ||
            titleL.contains("حل شدہ") || titleL.contains("تراجم") || titleL.contains("مع حاشیہ") ||
            titleL.contains("مع شرح") || titleL.contains("تعلیق")) {
            return true
        }

        return false
    }

    /**
     * Determines whether a book entity is an Original Textbook (Asal Kitab / Matn).
     */
    fun isOriginalBook(title: String, type: String = "", description: String = ""): Boolean {
        return !isSharah(title, type, description)
    }

    fun matchesSubject(bookSubject: String, querySubject: String): Boolean {
        if (querySubject == "All" || querySubject.isBlank()) return true
        val normBook = normalizeSubject(bookSubject)
        val normQuery = normalizeSubject(querySubject)
        return normBook.equals(normQuery, ignoreCase = true) ||
                bookSubject.equals(querySubject, ignoreCase = true) ||
                bookSubject.contains(querySubject, ignoreCase = true) ||
                querySubject.contains(bookSubject, ignoreCase = true)
    }
}
