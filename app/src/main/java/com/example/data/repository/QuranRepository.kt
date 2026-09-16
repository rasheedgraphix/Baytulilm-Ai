package com.example.data.repository

import com.example.data.model.QuranEdition

object QuranRepository {

    private val quranEditions = listOf(
        QuranEdition(
            id = "quran_16_lines_tajweedi_iqra",
            title = "Quran 16 Lines Tajweedi (Iqra Quran Company)",
            titleUrdu = "قرآن مجید ۱۶ سطری تجویدی (اقراء قرآن کمپنی)",
            scriptTypeUrdu = "۱۶ سطری تجویدی",
            lines = 16,
            publisherUrdu = "اقراء قرآن کمپنی",
            descriptionUrdu = "خوبصورت اور واضح ۱۶ سطری تجویدی رسم الخط، حفظِ قرآن اور تجوید کے لیے خصوصی ایڈیشن۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/QURAN_16_LINES_TAJWEEDI_IQRA_QURAN_COMPANY.pdf",
            isTajweedi = true
        ),
        QuranEdition(
            id = "quran_16_lines_tajweedi_idara_al_haram",
            title = "Quran 16 Lines Tajweedi (Idara Al Haram)",
            titleUrdu = "قرآن مجید ۱۶ سطری تجویدی (ادارہ الحرم)",
            scriptTypeUrdu = "۱۶ سطری تجویدی",
            lines = 16,
            publisherUrdu = "ادارہ الحرم",
            descriptionUrdu = "ادارہ الحرم کا شائع کردہ معتبر ۱۶ سطری تجویدی نسخہ، رنگین علاماتِ تجوید کے ساتھ۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/QURAN_16_LINES_TAJWEEDI_IDARA_AL_HARAM.pdf",
            isTajweedi = true
        ),
        QuranEdition(
            id = "quran_hakeem_15_lines_tajweedi",
            title = "Quran Hakeem 15 Lines Tajweedi",
            titleUrdu = "قرآن حکیم ۱۵ سطری تجویدی",
            scriptTypeUrdu = "۱۵ سطری تجویدی",
            lines = 15,
            publisherUrdu = "مکتبہ تجوید القرآن",
            descriptionUrdu = "حفاظِ کرام کا پسندیدہ ۱۵ سطری تجویدی قرآن مجید، عمدہ طباعت اور مخارج و صفات کے اشارات۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/QURAN-HAKEEM-15-LINES-TAJWEEDI.pdf",
            isTajweedi = true
        ),
        QuranEdition(
            id = "quran_hakeem_15_lines_standard",
            title = "Quran Hakeem 15 Lines Standard",
            titleUrdu = "قرآن حکیم ۱۵ سطری (حفاظی نسخہ)",
            scriptTypeUrdu = "۱۵ سطری حفاظی",
            lines = 15,
            publisherUrdu = "تاج کمپنی / معتبر طباعت",
            descriptionUrdu = "برصغیر پاک و ہند کے مدارس و حفاظ میں سب سے زیادہ رائج ۱۵ سطری معیاری قرآن مجید۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/QURAN-HAKEEM-15-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_13_lines_tajweedi",
            title = "Al-Quran 13 Lines Tajweedi",
            titleUrdu = "القرآن الکریم ۱۳ سطری تجویدی",
            scriptTypeUrdu = "۱۳ سطری تجویدی",
            lines = 13,
            publisherUrdu = "نورانی کتب خانہ",
            descriptionUrdu = "۱۳ سطری جلی و کشادہ حروف کے ساتھ تجویدی قواعد پر مبنی نسخہ، ناظرہ خوانی کے لیے بہترین۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-13-LINES-TAJWEEDI.pdf",
            isTajweedi = true
        ),
        QuranEdition(
            id = "al_quran_10_lines",
            title = "Al-Quran 10 Lines (Large Font)",
            titleUrdu = "القرآن الکریم ۱۰ سطری (بڑے حروف)",
            scriptTypeUrdu = "۱۰ سطری جلی",
            lines = 10,
            publisherUrdu = "مکتبہ معارف القرآن",
            descriptionUrdu = "انتہائی جلی، واضح اور بڑے حروف والا ۱۰ سطری نسخہ، تلاوت میں انتہائی سہولت۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-10-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_11_lines",
            title = "Al-Quran 11 Lines",
            titleUrdu = "القرآن الکریم ۱۱ سطری",
            scriptTypeUrdu = "۱۱ سطری کشادہ",
            lines = 11,
            publisherUrdu = "قدیمی کتب خانہ",
            descriptionUrdu = "۱۱ سطری کشادہ اور صاف نسخہ، طلبہ اور بزرگ قارئین کے مطالعہ و تلاوت کے لیے موزوں۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-11-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_14_lines",
            title = "Al-Quran 14 Lines",
            titleUrdu = "القرآن الکریم ۱۴ سطری",
            scriptTypeUrdu = "۱۴ سطری",
            lines = 14,
            publisherUrdu = "دار الکتاب",
            descriptionUrdu = "۱۴ سطری متوازن اور روایتی رسم الخط کا معتبر ایڈیشن۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-14-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_17_lines",
            title = "Al-Quran 17 Lines",
            titleUrdu = "القرآن الکریم ۱۷ سطری",
            scriptTypeUrdu = "۱۷ سطری",
            lines = 17,
            publisherUrdu = "مکتبہ عثمانیہ",
            descriptionUrdu = "۱۷ سطری نسخہ، معیاری سائز اور جامع فارمیٹ میں تلاوت کا انتظام۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-17-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_18_lines",
            title = "Al-Quran 18 Lines",
            titleUrdu = "القرآن الکریم ۱۸ سطری",
            scriptTypeUrdu = "۱۸ سطری",
            lines = 18,
            publisherUrdu = "دار الاشاعت",
            descriptionUrdu = "۱۸ سطری نسخہ، پاکٹ و کلاسک کتابی سائز کے مطابق تلاوت کے لیے۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-18-LINES.pdf",
            isTajweedi = false
        ),
        QuranEdition(
            id = "al_quran_21_lines",
            title = "Al-Quran 21 Lines",
            titleUrdu = "القرآن الکریم ۲۱ سطری",
            scriptTypeUrdu = "۲۱ سطری",
            lines = 21,
            publisherUrdu = "المکتبۃ السلفیہ",
            descriptionUrdu = "۲۱ سطری جامع نسخہ، زیادہ سطور اور کم صفحات میں مکمل قرآن مجید۔",
            pdfUrl = "https://archive.org/download/BestUrduBooks517/AL-QURAN-21-LINES.pdf",
            isTajweedi = false
        )
    )

    fun getAllEditions(): List<QuranEdition> = quranEditions

    fun getEditionById(id: String): QuranEdition? = quranEditions.find { it.id == id }
}
