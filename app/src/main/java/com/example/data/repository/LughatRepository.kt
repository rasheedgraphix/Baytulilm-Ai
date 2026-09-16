package com.example.data.repository

import com.example.data.model.LughatItem

object LughatRepository {

    private val lughatList = listOf(
        LughatItem(
            id = "lughat_ghiyas_ul_lughaat_farsi",
            title = "Ghiyas Ul Lughaat (Farsi)",
            titleUrdu = "غیاث اللغات (فارسی)",
            languageTypeUrdu = "فارسی لغت",
            compilerUrdu = "مولوی محمد غیاث الدین رامپوری",
            descriptionUrdu = "فارسی زبان و ادب اور درسِ نظامی کے طلبا و اساتذہ کے لیے مستند و جامع ترین فارسی لغت۔",
            pdfUrl = "https://archive.org/download/jun2023/GHIYAS_UL_LUGHAAT_FARSI.pdf",
            totalPages = 890
        ),
        LughatItem(
            id = "lughat_al_qamoos_al_muheet",
            title = "Al-Qamoos Al-Muheet",
            titleUrdu = "القاموس المحیط",
            languageTypeUrdu = "عربی لغت",
            compilerUrdu = "علامہ مجد الدین الفیروز آبادی",
            descriptionUrdu = "عربی زبان کے عظیم ترین اور امہات المعاجم میں سے کلاسک شاہکار معاجمِ لغت۔",
            pdfUrl = "https://archive.org/download/besturdubooks3/Al-Qamoos-Al-Muhith---www.besturdubooks.wordpress.com.pdf",
            totalPages = 1450
        ),
        LughatItem(
            id = "lughat_feroz_ul_lughaat_farsi_urdu",
            title = "Feroz Ul Lughaat Farsi-Urdu",
            titleUrdu = "فیروز اللغات (فارسی تا اردو)",
            languageTypeUrdu = "فارسی تا اردو",
            compilerUrdu = "الحاج مولوی فیروز الدین",
            descriptionUrdu = "فارسی سے اردو ترجمہ اور تشریح کے لیے جامع اور مستند لغت، گلستان و بوستان کے مطالعہ کے لیے بے حد مفید۔",
            pdfUrl = "https://archive.org/download/FerozUlLughaatFarsi/Feroz_Ul_Lughaat_Farsi-Urdu.pdf",
            totalPages = 720
        ),
        LughatItem(
            id = "lughat_misbah_ul_lughaat",
            title = "Misbah Ul Lughaat",
            titleUrdu = "مصباح اللغات (عربی تا اردو)",
            languageTypeUrdu = "عربی تا اردو",
            compilerUrdu = "مولانا عبد الحفیظ بلیاوی",
            descriptionUrdu = "مدارسِ اسلامیہ میں عربی کتب فہمی کے لیے سب سے مقبول، رائج اور آسان عربی تا اردو لغت۔",
            pdfUrl = "https://archive.org/download/MisbahUlLughaat/Misbah%20Ul%20Lughaat.pdf",
            totalPages = 1040
        ),
        LughatItem(
            id = "lughat_feroz_ul_lughaat_jame",
            title = "Feroz Ul Lughaat Jame",
            titleUrdu = "فیروز اللغات جامع (اردو)",
            languageTypeUrdu = "اردو لغت",
            compilerUrdu = "الحاج مولوی فیروز الدین",
            descriptionUrdu = "اردو زبان و ادب کا عظیم الشان اور سب سے جامع ذخیرہ الفاظ، تلفظ اور معانی کا انسائیکلوپیڈیا۔",
            pdfUrl = "https://archive.org/download/FerozUlLughaatJame/Feroz%20Ul%20Lughaat%20Jame%20By%20Alhaj%20Molvi%20Feroz%20ud%20deen.pdf",
            totalPages = 1480
        ),
        LughatItem(
            id = "lughat_anwaar_ul_bayan_vol_01",
            title = "Anwaar Ul Bayan Vol 01",
            titleUrdu = "انوار البیان فی حل لغات القرآن (جلد ۱)",
            languageTypeUrdu = "قرآنی لغات",
            compilerUrdu = "مولانا محمد علی",
            descriptionUrdu = "قرآن حکیم کے الفاظ و تراکیب اور لغات کا مستند اور جامع حل، پارہ ۱ تا پارہ ۸۔",
            pdfUrl = "https://archive.org/download/AnwaarUlBayan/Anwaar%20Ul%20Bayan%20Vol%201%20By%20Muhammad%20Ali.pdf",
            totalPages = 620
        ),
        LughatItem(
            id = "lughat_anwaar_ul_bayan_vol_02",
            title = "Anwaar Ul Bayan Vol 02",
            titleUrdu = "انوار البیان فی حل لغات القرآن (جلد ۲)",
            languageTypeUrdu = "قرآنی لغات",
            compilerUrdu = "مولانا محمد علی",
            descriptionUrdu = "قرآن حکیم کے الفاظ و تراکیب اور لغات کا مستند اور جامع حل، پارہ ۹ تا پارہ ۱۵۔",
            pdfUrl = "https://archive.org/download/AnwaarUlBayan/Anwaar%20Ul%20Bayan%20Vol%202%20By%20Muhammad%20Ali.pdf",
            totalPages = 590
        ),
        LughatItem(
            id = "lughat_anwaar_ul_bayan_vol_03",
            title = "Anwaar Ul Bayan Vol 03",
            titleUrdu = "انوار البیان فی حل لغات القرآن (جلد ۳)",
            languageTypeUrdu = "قرآنی لغات",
            compilerUrdu = "مولانا محمد علی",
            descriptionUrdu = "قرآن حکیم کے الفاظ و تراکیب اور لغات کا مستند اور جامع حل، پارہ ۱۶ تا پارہ ۲۲۔",
            pdfUrl = "https://archive.org/download/AnwaarUlBayan/Anwaar%20Ul%20Bayan%20Vol%203%20By%20Muhammad%20Ali.pdf",
            totalPages = 610
        ),
        LughatItem(
            id = "lughat_anwaar_ul_bayan_vol_04",
            title = "Anwaar Ul Bayan Vol 04",
            titleUrdu = "انوار البیان فی حل لغات القرآن (جلد ۴)",
            languageTypeUrdu = "قرآنی لغات",
            compilerUrdu = "مولانا محمد علی",
            descriptionUrdu = "قرآن حکیم کے الفاظ و تراکیب اور لغات کا مستند اور جامع حل، پارہ ۲۳ تا پارہ ۳۰۔",
            pdfUrl = "https://archive.org/download/AnwaarUlBayan/Anwaar%20Ul%20Bayan%20Vol%204%20By%20Muhammad%20Ali.pdf",
            totalPages = 640
        ),
        LughatItem(
            id = "lughat_al_qamoos_ul_jadeed_urdu_to_arabic",
            title = "Al Qamoos Ul Jadeed (Urdu to Arabic)",
            titleUrdu = "القاموس الجدید (اردو تا عربی)",
            languageTypeUrdu = "اردو تا عربی",
            compilerUrdu = "مولانا وحید الزماں قاسمی کیرانوی",
            descriptionUrdu = "عصرِ حاضر کی ضروریات اور جدید عربی مکالمہ و تحریر کے لیے اردو سے عربی کا بہترین قاموس۔",
            pdfUrl = "https://archive.org/download/AlQamoosUlJadeedUrduToArabic/Al%20Qamoos%20Ul%20Jadeed%20%28Urdu%20to%20Arabic%29%20By%20Maulana%20Waheed%20Uz%20Zaman%20Qasmi.pdf",
            totalPages = 820
        )
    )

    fun getAllLughat(): List<LughatItem> = lughatList

    fun getLughatById(id: String): LughatItem? = lughatList.find { it.id == id }
}
