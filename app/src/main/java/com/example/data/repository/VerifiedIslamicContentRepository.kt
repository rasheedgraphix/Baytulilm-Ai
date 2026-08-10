package com.example.data.repository

import java.util.Calendar

data class VerifiedHadith(
    val id: Int,
    val textArabic: String,
    val textUrdu: String,
    val bookName: String,
    val hadithNumber: String,
    val verificationTag: String = "مستند حدیث (صحیح)"
)

data class VerifiedAyah(
    val id: Int,
    val textArabic: String,
    val textUrdu: String,
    val surahName: String,
    val surahNumber: Int,
    val ayahNumber: Int
)

object VerifiedIslamicContentRepository {

    private val verifiedHadiths = listOf(
        VerifiedHadith(
            id = 1,
            textArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            textUrdu = "اعمال کا دارومدار نیتوں پر ہے، اور ہر شخص کے لیے وہی کچھ ہے جس کی اس نے نیت کی ہو۔",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            hadithNumber = "حدیث: 1",
            verificationTag = "مستند حدیث (متفق علیہ)"
        ),
        VerifiedHadith(
            id = 2,
            textArabic = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            textUrdu = "تم میں سے کوئی شخص اس وقت تک کامل مومن نہیں ہو سکتا جب تک اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            hadithNumber = "حدیث: 13",
            verificationTag = "مستند حدیث (صحیح)"
        ),
        VerifiedHadith(
            id = 3,
            textArabic = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            textUrdu = "تم میں سے سب سے بہترین انسان وہ ہے جو قرآن مجید سیکھے اور اسے دوسروں کو سکھائے۔",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            hadithNumber = "حدیث: 5027",
            verificationTag = "مستند حدیث (صحیح)"
        ),
        VerifiedHadith(
            id = 4,
            textArabic = "الطَّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ",
            textUrdu = "پاکیزگی اور صفائی نصف ایمان ہے، اور 'الحمد لله' کہنا عمل کے ترازو کو بھر دیتا ہے۔",
            bookName = "صحیح مسلم (Sahih Muslim)",
            hadithNumber = "حدیث: 223",
            verificationTag = "مستند حدیث (صحیح)"
        ),
        VerifiedHadith(
            id = 5,
            textArabic = "لَيْسَ الشَّدِيدُ بِالصُُّرَعَةِ، إِنَّمَا الشَّدِيدُ الَّذِي يَمْلِكُ نَفْسَهُ عِنْدَ الْغَضَبِ",
            textUrdu = "پہلوان اور طاقتور وہ نہیں جو لوگوں کو پچھاڑ دے، بلکہ اصل طاقتور وہ ہے جو غصے کے وقت اپنے نفس پر قابو رکھے۔",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            hadithNumber = "حدیث: 6018",
            verificationTag = "مستند حدیث (صحیح)"
        ),
        VerifiedHadith(
            id = 6,
            textArabic = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيَقُلْ خَيْرًا أَوْ لِيَصْمُتْ",
            textUrdu = "جو شخص اللہ اور آخرت کے دن پر ایمان رکھتا ہے، اسے چاہیے کہ اچھی بات کہے یا پھر خاموش رہے۔",
            bookName = "جامع الترمذی (Jami at-Tirmidhi)",
            hadithNumber = "حدیث: 2501",
            verificationTag = "مستند حدیث (حسن صحیح)"
        ),
        VerifiedHadith(
            id = 7,
            textArabic = "الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمَنُ، ارْحَمُوا مَنْ فِي الأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ",
            textUrdu = "رحم کرنے والوں پر رحمن رحم فرماتا ہے، تم زمین والوں پر رحم کرو، آسمان والا تم پر رحم فرمائے گا۔",
            bookName = "سنن ابی داؤد (Sunan Abi Dawud)",
            hadithNumber = "حدیث: 4790",
            verificationTag = "مستند حدیث (صحیح)"
        ),
        VerifiedHadith(
            id = 8,
            textArabic = "الْكَلِمَةُ الطَّيِّبَةُ صَدَقَةٌ",
            textUrdu = "پاکیزہ اور خوش اخلاقی بھری بات کہنا بھی ایک صدقہ ہے۔",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            hadithNumber = "حدیث: 6011",
            verificationTag = "مستند حدیث (صحیح)"
        )
    )

    private val verifiedAyahs = listOf(
        VerifiedAyah(
            id = 1,
            textArabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
            textUrdu = "اللہ! اس کے سوا کوئی معبود نہیں، وہ زندہ جاوید اور تمام کائنات کو قائم رکھنے والا ہے۔ نہ اسے اونگھ آتی ہے اور نہ نیند، جو کچھ آسمانوں اور زمین میں ہے سب اسی کا ہے۔",
            surahName = "سورۃ البقرة (Al-Baqarah)",
            surahNumber = 2,
            ayahNumber = 255
        ),
        VerifiedAyah(
            id = 2,
            textArabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ﴿٥﴾ إِنَّ مَعَ الْعُسْرِ يُسْرًا ﴿٦﴾",
            textUrdu = "پس یقیناً ہر دشواری کے ساتھ آسانی ہے، بے شک ہر دشواری کے ساتھ ہی آسانی ہے۔",
            surahName = "سورۃ الشرح (Ash-Sharh)",
            surahNumber = 94,
            ayahNumber = 5
        ),
        VerifiedAyah(
            id = 3,
            textArabic = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
            textUrdu = "پس تم مجھے یاد رکھو، میں تمہیں یاد رکھوں گا، اور میرا شکر ادا کرو اور میری ناشکری نہ کرو۔",
            surahName = "سورۃ البقرة (Al-Baqarah)",
            surahNumber = 2,
            ayahNumber = 152
        ),
        VerifiedAyah(
            id = 4,
            textArabic = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            textUrdu = "جو لوگ ایمان لائے اور ان کے دل اللہ کے ذکر سے اطمینان پاتے ہیں، خبردار! اللہ کے ذکر سے ہی دلوں کو سکون و اطمینان ملتا ہے۔",
            surahName = "سورۃ الرعد (Ar-Ra'd)",
            surahNumber = 13,
            ayahNumber = 28
        ),
        VerifiedAyah(
            id = 5,
            textArabic = "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ",
            textUrdu = "اور تم سست نہ پڑو اور نہ غمگین ہو، تم ہی غالب رہو گے اگر تم سچے مومن ہو۔",
            surahName = "سورۃ آل عمران (Al-Imran)",
            surahNumber = 3,
            ayahNumber = 139
        ),
        VerifiedAyah(
            id = 6,
            textArabic = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            textUrdu = "(اے اللہ!) تیرے سوا کوئی معبود برحق نہیں، تو پاک ہے، بے شک میں ہی قصورواروں میں سے تھا۔",
            surahName = "سورۃ الأنبياء (Al-Anbiya)",
            surahNumber = 21,
            ayahNumber = 87
        ),
        VerifiedAyah(
            id = 7,
            textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ﴿١﴾ اللَّهُ الصَّمَدُ ﴿٢﴾ لَمْ يَلِدْ وَلَمْ يُولَدْ ﴿٣﴾ وَلَمْ يَكُن لَّهُ كُفُؤًا أَحَدٌ ﴿٤﴾",
            textUrdu = "آپ فرما دیجیے: وہ اللہ ایک ہے، اللہ بے نیاز ہے، نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے، اور نہ کوئی اس کا ہمسر ہے۔",
            surahName = "سورۃ الإخلاص (Al-Ikhlas)",
            surahNumber = 112,
            ayahNumber = 1
        )
    )

    fun getDailyHadith(): VerifiedHadith {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % verifiedHadiths.size
        return verifiedHadiths[index]
    }

    fun getDailyAyah(): VerifiedAyah {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % verifiedAyahs.size
        return verifiedAyahs[index]
    }
}
