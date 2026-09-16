package com.example.data.repository

import java.util.Calendar

data class VerifiedHadith(
    val id: Int,
    val textArabic: String,
    val textUrdu: String,
    val textEnglish: String,
    val textPashto: String = "",
    val bookName: String,
    val bookNameEnglish: String = "",
    val hadithNumber: String,
    val hadithNumberEnglish: String = "",
    val verificationTag: String = "مستند حدیث (صحیح)",
    val verificationTagEnglish: String = "Authentic Hadith (Sahih)"
)

data class VerifiedAyah(
    val id: Int,
    val textArabic: String,
    val textUrdu: String,
    val textEnglish: String,
    val textPashto: String = "",
    val surahName: String,
    val surahNameEnglish: String = "",
    val surahNumber: Int,
    val ayahNumber: Int
)

object VerifiedIslamicContentRepository {

    private val verifiedHadiths = listOf(
        VerifiedHadith(
            id = 1,
            textArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            textUrdu = "اعمال کا دارومدار نیتوں پر ہے، اور ہر شخص کے لیے وہی کچھ ہے جس کی اس نے نیت کی ہو۔",
            textEnglish = "Actions are but by intentions, and every person will have only what they intended.",
            textPashto = "د عملونو دارومدار په نيتونو پورې دی، او د هر سړي لپاره هغه څه دي چې نيت يې کړی وي.",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            bookNameEnglish = "Sahih al-Bukhari",
            hadithNumber = "حدیث: 1",
            hadithNumberEnglish = "Hadith: 1",
            verificationTag = "مستند حدیث (متفق علیہ)",
            verificationTagEnglish = "Authentic (Muttafaqun Alayh)"
        ),
        VerifiedHadith(
            id = 2,
            textArabic = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            textUrdu = "تم میں سے کوئی شخص اس وقت تک کامل مومن نہیں ہو سکتا جب تک اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
            textEnglish = "None of you truly believes until he loves for his brother what he loves for himself.",
            textPashto = "په تاسو کې هیڅوک تر هغه پورې پوره مومن نه شي کیدای تر څو چې د خپل ورور لپاره هغه څه خوښ نه کړي چې د ځان لپاره یې خوښوي.",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            bookNameEnglish = "Sahih al-Bukhari",
            hadithNumber = "حدیث: 13",
            hadithNumberEnglish = "Hadith: 13",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        ),
        VerifiedHadith(
            id = 3,
            textArabic = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            textUrdu = "تم میں سے سب سے بہترین انسان وہ ہے جو قرآن مجید سیکھے اور اسے دوسروں کو سکھائے۔",
            textEnglish = "The best among you are those who learn the Quran and teach it to others.",
            textPashto = "په تاسو کې تر ټولو غوره هغه څوک دی چې قرآن زده کړي او نورو ته یې ور زده کړي.",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            bookNameEnglish = "Sahih al-Bukhari",
            hadithNumber = "حدیث: 5027",
            hadithNumberEnglish = "Hadith: 5027",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        ),
        VerifiedHadith(
            id = 4,
            textArabic = "الطَّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ",
            textUrdu = "پاکیزگی اور صفائی نصف ایمان ہے، اور 'الحمد لله' کہنا عمل کے ترازو کو بھر دیتا ہے۔",
            textEnglish = "Purity is half of faith, and saying 'Alhamdulillah' (Praise be to Allah) fills the scale.",
            textPashto = "پاکي او پاکيزګي نيم ايمان دی، او 'الحمد لله' ويل د تلي پلې ډکوي.",
            bookName = "صحیح مسلم (Sahih Muslim)",
            bookNameEnglish = "Sahih Muslim",
            hadithNumber = "حدیث: 223",
            hadithNumberEnglish = "Hadith: 223",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        ),
        VerifiedHadith(
            id = 5,
            textArabic = "لَيْسَ الشَّدِيدُ بِالصُُّرَعَةِ، إِنَّمَا الشَّدِيدُ الَّذِي يَمْلِكُ نَفْسَهُ عِنْدَ الْغَضَبِ",
            textUrdu = "پہلوان اور طاقتور وہ نہیں جو لوگوں کو پچھاڑ دے، بلکہ اصل طاقتور وہ ہے جو غصے کے وقت اپنے نفس پر قابو رکھے۔",
            textEnglish = "The strong person is not the one who overcomes people in wrestling; the truly strong person is the one who controls himself in anger.",
            textPashto = "طاقتور او زړور هغه نه دی چې نور په غېږه کې مات کړي، بلکې اصلي طاقتور هغه دی چې د غصې په وخت کې خپل نفس قابو کړي.",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            bookNameEnglish = "Sahih al-Bukhari",
            hadithNumber = "حدیث: 6018",
            hadithNumberEnglish = "Hadith: 6018",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        ),
        VerifiedHadith(
            id = 6,
            textArabic = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيَقُلْ خَيْرًا أَوْ لِيَصْمُتْ",
            textUrdu = "جو شخص اللہ اور آخرت کے دن پر ایمان رکھتا ہے، اسے چاہیے کہ اچھی بات کہے یا پھر خاموش رہے۔",
            textEnglish = "Whoever believes in Allah and the Last Day should speak good or remain silent.",
            textPashto = "څوک چې پر الله او د آخرت پر ورځ ايمان لري، هغه ته پکار دي چې ښه خبره وکړي او يا خاموش پاتې شي.",
            bookName = "جامع الترمذی (Jami at-Tirmidhi)",
            bookNameEnglish = "Jami at-Tirmidhi",
            hadithNumber = "حدیث: 2501",
            hadithNumberEnglish = "Hadith: 2501",
            verificationTag = "مستند حدیث (حسن صحیح)",
            verificationTagEnglish = "Authentic (Hasan Sahih)"
        ),
        VerifiedHadith(
            id = 7,
            textArabic = "الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمَنُ، ارْحَمُوا مَنْ فِي الأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ",
            textUrdu = "رحم کرنے والوں پر رحمن رحم فرماتا ہے، تم زمین والوں پر رحم کرو، آسمان والا تم پر رحم فرمائے گا۔",
            textEnglish = "The merciful will be shown mercy by the Most Merciful. Be merciful to those on earth, and the One in the heavens will have mercy on you.",
            textPashto = "پر رحم کوونکو باندې رحمن رحم کوي، تاسو د ځمکې پر اوسېدونکو رحم وکړئ، د اسمان څښتن به پر تاسو رحم وکړي.",
            bookName = "سنن ابی داؤد (Sunan Abi Dawud)",
            bookNameEnglish = "Sunan Abi Dawud",
            hadithNumber = "حدیث: 4790",
            hadithNumberEnglish = "Hadith: 4790",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        ),
        VerifiedHadith(
            id = 8,
            textArabic = "الْكَلِمَةُ الطَّيِّبَةُ صَدَقَةٌ",
            textUrdu = "پاکیزہ اور خوش اخلاقی بھری بات کہنا بھی ایک صدقہ ہے۔",
            textEnglish = "A good, kind word is a form of charity.",
            textPashto = "ښه او نېکه خبره کول هم يوه صدقه ده.",
            bookName = "صحیح البخاری (Sahih Bukhari)",
            bookNameEnglish = "Sahih al-Bukhari",
            hadithNumber = "حدیث: 6011",
            hadithNumberEnglish = "Hadith: 6011",
            verificationTag = "مستند حدیث (صحیح)",
            verificationTagEnglish = "Authentic (Sahih)"
        )
    )

    private val verifiedAyahs = listOf(
        VerifiedAyah(
            id = 1,
            textArabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
            textUrdu = "اللہ! اس کے سوا کوئی معبود نہیں، وہ زندہ جاوید اور تمام کائنات کو قائم رکھنے والا ہے۔ نہ اسے اونگھ آتی ہے اور نہ نیند، جو کچھ آسمانوں اور زمین میں ہے سب اسی کا ہے۔",
            textEnglish = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth.",
            textPashto = "الله هغه ذات دی چې له هغه پرته بل هېڅ حق معبود نشته، ژوندی او د ټولې هستۍ پالونکی او قايم ساتونکی دی.",
            surahName = "سورۃ البقرة (Al-Baqarah)",
            surahNameEnglish = "Surah Al-Baqarah",
            surahNumber = 2,
            ayahNumber = 255
        ),
        VerifiedAyah(
            id = 2,
            textArabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ﴿٥﴾ إِنَّ مَعَ الْعُسْرِ يُسْرًا ﴿٦﴾",
            textUrdu = "پس یقیناً ہر دشواری کے ساتھ آسانی ہے، بے شک ہر دشواری کے ساتھ ہی آسانی ہے۔",
            textEnglish = "Indeed, with hardship comes ease. Truly, with hardship comes ease.",
            textPashto = "نو بېشکه له هرې سختۍ سره اساني شته، يقيناً له سختۍ سره اساني ده.",
            surahName = "سورۃ الشرح (Ash-Sharh)",
            surahNameEnglish = "Surah Ash-Sharh",
            surahNumber = 94,
            ayahNumber = 5
        ),
        VerifiedAyah(
            id = 3,
            textArabic = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
            textUrdu = "پس تم مجھے یاد رکھو، میں تمہیں یاد رکھوں گا، اور میرا شکر ادا کرو اور میری ناشکری نہ کرو۔",
            textEnglish = "So remember Me; I will remember you. And be grateful to Me and do not deny Me.",
            textPashto = "نو تاسو ما ياد کړئ، زه به تاسو ياد کړم، او زما شکر ادا کړئ او ناشکري مه کوئ.",
            surahName = "سورۃ البقرة (Al-Baqarah)",
            surahNameEnglish = "Surah Al-Baqarah",
            surahNumber = 2,
            ayahNumber = 152
        ),
        VerifiedAyah(
            id = 4,
            textArabic = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            textUrdu = "جو لوگ ایمان لائے اور ان کے دل اللہ کے ذکر سے اطمینان پاتے ہیں، خبردار! اللہ کے ذکر سے ہی دلوں کو سکون و اطمینان ملتا ہے۔",
            textEnglish = "Those who have believed and whose hearts find comfort in the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.",
            textPashto = "هغه کسان چې ايمان يې راوړی او زړونه يې د الله په ذکر ډاډمن کېږي، خبردار! د الله په ذکر زړونه سکون مومي.",
            surahName = "سورۃ الرعد (Ar-Ra'd)",
            surahNameEnglish = "Surah Ar-Ra'd",
            surahNumber = 13,
            ayahNumber = 28
        ),
        VerifiedAyah(
            id = 5,
            textArabic = "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ",
            textUrdu = "اور تم سست نہ پڑو اور نہ غمگین ہو، تم ہی غالب رہو گے اگر تم سچے مومن ہو۔",
            textEnglish = "Do not weaken and do not grieve, and you will be superior if you are true believers.",
            textPashto = "او تاسو مه کمزوري کېږئ او مه خفه کېږئ، تاسو به برلاسي ياست که چېرې رښتيني مومنان وئ.",
            surahName = "سورۃ آل عمران (Al-Imran)",
            surahNameEnglish = "Surah Ali 'Imran",
            surahNumber = 3,
            ayahNumber = 139
        ),
        VerifiedAyah(
            id = 6,
            textArabic = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            textUrdu = "(اے اللہ!) تیرے سوا کوئی معبود برحق نہیں، تو پاک ہے، بے شک میں ہی قصورواروں میں سے تھا۔",
            textEnglish = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            textPashto = "له تا پرته بل هېڅ حق معبود نشته، ته پاک يې، بېشکه زه له ظالمانو او خطا کارانو څخه وم.",
            surahName = "سورۃ الأنبياء (Al-Anbiya)",
            surahNameEnglish = "Surah Al-Anbiya",
            surahNumber = 21,
            ayahNumber = 87
        ),
        VerifiedAyah(
            id = 7,
            textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ﴿١﴾ اللَّهُ الصَّمَدُ ﴿٢﴾ لَمْ يَلِدْ وَلَمْ يُولَدْ ﴿٣﴾ وَلَمْ يَكُن لَّهُ كُفُؤًا أَحَدٌ ﴿٤﴾",
            textUrdu = "آپ فرما دیجیے: وہ اللہ ایک ہے، اللہ بے نیاز ہے، نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے، اور نہ کوئی اس کا ہمسر ہے۔",
            textEnglish = "Say, 'He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.'",
            textPashto = "ووايه: هغه الله يو دی، الله بې نيازه دی، نه يې څوک زېږولي او نه له چا زېږېدلی دی، او هېڅوک د هغه سيال نشته.",
            surahName = "سورۃ الإخلاص (Al-Ikhlas)",
            surahNameEnglish = "Surah Al-Ikhlas",
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

