package com.example.data.repository

import com.example.data.model.RightsStatus
import com.example.data.model.Tafseer
import com.example.data.model.TafseerVolume

object TafseerRepository {
    fun getTafaseer(): List<Tafseer> = listOf(
        Tafseer(
            id = "tafseer_1",
            title = "Rushd-ul-Quran",
            titleUrdu = "رشد القرآن فی تفسیر آیات القرآن",
            author = "مولانا اسید اللہ",
            authorUrdu = "مولانا اسید اللہ",
            language = "Pashto",
            description = "پشتو زبان میں قرآن کریم کی تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v1_1", "tafseer_1", 1, "Volume 1", "جلد 1", "https://archive.org/download/5_20240422_20240422_1628/%D8%B1%D8%B4%D8%AF_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B1_.pdf"),
                TafseerVolume("v1_2", "tafseer_1", 2, "Volume 2", "جلد 2", "https://archive.org/download/5_20240422_20240422_1628/%D8%B1%D8%B4%D8%AF_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B2.pdf"),
                TafseerVolume("v1_3", "tafseer_1", 3, "Volume 3", "جلد 3", "https://archive.org/download/5_20240422_20240422_1628/%D8%B1%D8%B4%D8%AF_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B3.pdf"),
                TafseerVolume("v1_4", "tafseer_1", 4, "Volume 4", "جلد 4", "https://archive.org/download/5_20240422_20240422_1628/4_%D8%B1%D8%B4%D8%AF%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1_%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B4.pdf"),
                TafseerVolume("v1_5", "tafseer_1", 5, "Volume 5", "جلد 5", "https://archive.org/download/5_20240422_20240422_1628/5_%D8%B1%D8%B4%D8%AF%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1_%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B5.pdf"),
                TafseerVolume("v1_6", "tafseer_1", 6, "Volume 6", "جلد 6", "https://archive.org/download/5_20240422_20240422_1628/6_%D8%B1%D8%B4%D8%AF%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B6.pdf"),
                TafseerVolume("v1_7", "tafseer_1", 7, "Volume 7", "جلد 7", "https://archive.org/download/5_20240422_20240422_1628/%D8%B1%D8%B4%D8%AF_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%81%DB%8C_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%D8%A2%DB%8C%D8%A7%D8%AA_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%BE%DA%9A%D8%AA%D9%88_%DB%B0%DB%B7.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_2",
            title = "Tafseer Usmani",
            titleUrdu = "تفسیر عثمانی",
            author = "مولانا شبیر احمد عثمانی",
            authorUrdu = "مولانا شبیر احمد عثمانی",
            language = "Urdu",
            description = "مستند تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v2_1", "tafseer_2", 1, "Volume 1", "جلد 1", "https://archive.org/download/TOOBAA-tafseer-e-usmani-urdu/TAFSEER%20E%20USMANI%20-%20URDU.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_3",
            title = "Asan Bayan-ul-Quran",
            titleUrdu = "آسان بیان القرآن مع تفسیر عثمانی",
            author = "مولانا عمر انور بدخشانی",
            authorUrdu = "مولانا عمر انور بدخشانی",
            language = "Urdu",
            description = "آسان فہم تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v3_1", "tafseer_3", 1, "Volume 1", "جلد 1", "https://archive.org/download/1-o_20240128/%D8%A2%D8%B3%D8%A7%D9%86_%D8%A8%DB%8C%D8%A7%D9%86_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%85%D8%B9_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1_%D8%B9%D8%AB%D9%85%D8%A7%D9%86%DB%8C_%D8%AC%D9%84%D8%AF_1_o.pdf"),
                TafseerVolume("v3_2", "tafseer_3", 2, "Volume 2", "جلد 2", "https://archive.org/download/1-o_20240128/%D8%A2%D8%B3%D8%A7%D9%86_%D8%A8%DB%8C%D8%A7%D9%86_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%85%D8%B9_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1_%D8%B9%D8%AB%D9%85%D8%A7%D9%86%DB%8C_%D8%AC%D9%84%D8%AF_2_o.pdf"),
                TafseerVolume("v3_3", "tafseer_3", 3, "Volume 3", "جلد 3", "https://archive.org/download/1-o_20240128/%D8%A2%D8%B3%D8%A7%D9%86_%D8%A8%DB%8C%D8%A7%D9%86_%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86_%D9%85%D8%B9_%D8%AA%D9%81%D8%B3%DB%8C%D8%B1_%D8%B9%D8%AB%D9%85%D8%A7%D9%86%DB%8C_%D8%AC%D9%84%D8%AF_3_o.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_4",
            title = "Tafseer Hidayat-ul-Quran",
            titleUrdu = "تفسیر ہدایت القرآن",
            author = "مفتی سعید احمد پالنپوری",
            authorUrdu = "مفتی سعید احمد پالنپوری",
            language = "Urdu",
            description = "مفتی سعید احمد پالنپوری کی مشہور تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v4_1", "tafseer_4", 1, "Volume 1", "جلد 1", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%201%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_2", "tafseer_4", 2, "Volume 2", "جلد 2", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%202%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_3", "tafseer_4", 3, "Volume 3", "جلد 3", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%203%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_4", "tafseer_4", 4, "Volume 4", "جلد 4", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%204%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_5", "tafseer_4", 5, "Volume 5", "جلد 5", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%205%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_6", "tafseer_4", 6, "Volume 6", "جلد 6", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%206%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_7", "tafseer_4", 7, "Volume 7", "جلد 7", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%207%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf"),
                TafseerVolume("v4_8", "tafseer_4", 8, "Volume 8", "جلد 8", "https://archive.org/download/2_20250918_202509/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%DB%81%D8%AF%D8%A7%DB%8C%D8%AA%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%208%DB%94%20%D9%85%D9%81%D8%AA%DB%8C%20%D8%B3%D8%B9%DB%8C%D8%AF%20%D8%A7%D8%AD%D9%85%D8%AF%20%D9%BE%D8%A7%D9%84%D9%86%20%D9%BE%D9%88%D8%B1%DB%8C%20%20%DB%94%D8%AD%D9%82%20.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_5",
            title = "Tafseer Ibn-e-Kaseer",
            titleUrdu = "تفسیر ابن کثیر",
            author = "علامہ ابن کثیر",
            authorUrdu = "علامہ ابن کثیر",
            language = "Urdu",
            description = "مستند تفسیری کتاب",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v5_1", "tafseer_5", 1, "Volume 1", "جلد 1", "https://archive.org/download/tafseer-ibn-e-kaseer-01/Tafseer%20Ibn-e-Kaseer%2001.pdf"),
                TafseerVolume("v5_2", "tafseer_5", 2, "Volume 2", "جلد 2", "https://archive.org/download/tafseer-ibn-e-kaseer-01/Tafseer%20Ibn-e-Kaseer%2002.pdf"),
                TafseerVolume("v5_3", "tafseer_5", 3, "Volume 3", "جلد 3", "https://archive.org/download/tafseer-ibn-e-kaseer-01/Tafseer%20Ibn-e-Kaseer%2003.pdf"),
                TafseerVolume("v5_4", "tafseer_5", 4, "Volume 4", "جلد 4", "https://archive.org/download/tafseer-ibn-e-kaseer-01/Tafseer%20Ibn-e-Kaseer%2004.pdf"),
                TafseerVolume("v5_5", "tafseer_5", 5, "Volume 5", "جلد 5", "https://archive.org/download/tafseer-ibn-e-kaseer-01/Tafseer%20Ibn-e-Kaseer%2005.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_6",
            title = "Tasheel-ul-Bayan",
            titleUrdu = "تسہیل البیان فی تفسیر القرآن",
            author = "محمد اسلم شیخوپوری",
            authorUrdu = "محمد اسلم شیخوپوری",
            language = "Urdu",
            description = "تسہیل البیان فی تفسیر القرآن",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v6_1", "tafseer_6", 1, "Volume 1", "جلد 1", "https://archive.org/download/3_20250921_20250921/%D8%AA%D8%B3%DB%81%DB%8C%D9%84%20%D8%A7%D9%84%D8%A8%DB%8C%D8%A7%D9%86%20%D9%81%DB%8C%20%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%20%D8%AC%D9%84%D8%AF%201%20%DB%94%20%D9%85%D8%AD%D9%85%D8%AF%20%D8%A7%D8%B3%D9%84%D9%85%20%D8%B4%DB%8C%D8%AE%D9%88%D9%BE%D9%88%D8%B1%DB%8C%20%DB%94%20%D8%AD%D9%82%20%20.pdf"),
                TafseerVolume("v6_2", "tafseer_6", 2, "Volume 2", "جلد 2", "https://archive.org/download/3_20250921_20250921/%D8%AA%D8%B3%DB%81%DB%8C%D9%84%20%D8%A7%D9%84%D8%A8%DB%8C%D8%A7%D9%86%20%D9%81%DB%8C%20%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%20%D8%AC%D9%84%D8%AF%202%DB%94%20%D9%85%D8%AD%D9%85%D8%AF%20%D8%A7%D8%B3%D9%84%D9%85%20%D8%B4%DB%8C%D8%AE%D9%88%D9%BE%D9%88%D8%B1%DB%8C%20%DB%94%20%D8%AD%D9%82%20%20.pdf"),
                TafseerVolume("v6_3", "tafseer_6", 3, "Volume 3", "جلد 3", "https://archive.org/download/3_20250921_20250921/%D8%AA%D8%B3%DB%81%DB%8C%D9%84%20%D8%A7%D9%84%D8%A8%DB%8C%D8%A7%D9%86%20%D9%81%DB%8C%20%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%20%D8%AC%D9%84%D8%AF%203%20%DB%94%20%D9%85%D8%AD%D9%85%D8%AF%20%D8%A7%D8%B3%D9%84%D9%85%20%D8%B4%DB%8C%D8%AE%D9%88%D9%BE%D9%88%D8%B1%DB%8C%20%DB%94%20%D8%AD%D9%82%20%20.pdf"),
                TafseerVolume("v6_4", "tafseer_6", 4, "Volume 4", "جلد 4", "https://archive.org/download/3_20250921_20250921/%D8%AA%D8%B3%DB%81%DB%8C%D9%84%20%D8%A7%D9%84%D8%A8%DB%8C%D8%A7%D9%86%20%D9%81%DB%8C%20%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D8%A7%D9%84%D9%82%D8%B1%D8%A2%D9%86%20%D8%AC%D9%84%D8%AF%204%20%DB%94%20%D9%85%D8%AD%D9%85%D8%AF%20%D8%A7%D8%B3%D9%84%D9%85%20%D8%B4%DB%8C%D8%AE%D9%88%D9%BE%D9%88%D8%B1%DB%8C%20%DB%94%20%D8%AD%D9%82%20%20.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_7",
            title = "Tafseer Qurtubi",
            titleUrdu = "تفسیر قرطبی",
            author = "امام قرطبی",
            authorUrdu = "امام قرطبی",
            language = "Arabic",
            description = "عربی زبان میں مستند تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v7_1", "tafseer_7", 1, "Volume 1", "جلد 1", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%281%29%20.pdf"),
                TafseerVolume("v7_2", "tafseer_7", 2, "Volume 2", "جلد 2", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%282%29%20.pdf"),
                TafseerVolume("v7_3", "tafseer_7", 3, "Volume 3", "جلد 3", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%283%29%20.pdf"),
                TafseerVolume("v7_4", "tafseer_7", 4, "Volume 4", "جلد 4", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%284%29%20.pdf"),
                TafseerVolume("v7_5", "tafseer_7", 5, "Volume 5", "جلد 5", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%285%29%20.pdf"),
                TafseerVolume("v7_6", "tafseer_7", 6, "Volume 6", "جلد 6", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%286%29%20.pdf"),
                TafseerVolume("v7_7", "tafseer_7", 7, "Volume 7", "جلد 7", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%287%29%20.pdf"),
                TafseerVolume("v7_8", "tafseer_7", 8, "Volume 8", "جلد 8", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%288%29%20.pdf"),
                TafseerVolume("v7_9", "tafseer_7", 9, "Volume 9", "جلد 9", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%289%29%20.pdf"),
                TafseerVolume("v7_10", "tafseer_7", 10, "Volume 10", "جلد 10", "https://archive.org/download/1_20250310_20250310_0313/%D8%AA%D9%81%D8%B3%DB%8C%D8%B1%20%D9%82%D8%B1%D8%B7%D8%A8%DB%8C%20%D8%AC%D9%84%D8%AF%20%2810%29%20.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_8",
            title = "Tafseer-e-Haqqani",
            titleUrdu = "تفسیر حقانی",
            author = "مولانا عبد الحق حقانی دہلوی",
            authorUrdu = "مولانا عبد الحق حقانی دہلوی",
            language = "Urdu",
            description = "اردو زبان کی جامع اور عقلی و نقلی دلائل سے مزین معروف و مستند تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v8_1", "tafseer_8", 1, "Volume 1", "جلد 1", "https://archive.org/download/th-th/TAFSIR_E_HAQQANI_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_9",
            title = "Asan Bayan-ul-Quran Tafseer Usmani",
            titleUrdu = "آسان بیان القرآن مع تفسیر عثمانی (طبع جدید)",
            author = "مولانا عمر انور بدخشانی / مولانا شبیر احمد عثمانی",
            authorUrdu = "مولانا عمر انور بدخشانی / مولانا شبیر احمد عثمانی",
            language = "Urdu",
            description = "آسان فہم اور سلیس انداز میں قرآن مجید کی مستند تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v9_1", "tafseer_9", 1, "Volume 1", "جلد 1", "https://archive.org/download/abqtu/ASAN_BAYAN_UL_QURAN_TAFSIR_E_USMANI_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_10",
            title = "Zakhirat-ul-Janan Fi Fahm-il-Quran",
            titleUrdu = "ذخیرۃ الجنان فی فہم القرآن",
            author = "مولانا سرفراز خان صفدر",
            authorUrdu = "مولانا سرفراز خان صفدر",
            language = "Urdu",
            description = "امام اہل سنت حضرت مولانا سرفراز خان صفدر رحمہ اللہ کے تفسیری دروس کا گراں قدر مجموعہ",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v10_1", "tafseer_10", 1, "Volume 1", "جلد 1", "https://www.mediafire.com/file/40mdn0cxex1bg77/ZAKHIRATUL_JANAN_VOL_01.pdf/file")
            )
        ),
        Tafseer(
            id = "tafseer_11",
            title = "Maarif-ul-Bayan Fi Tafseer-il-Quran",
            titleUrdu = "معارف البیان فی تفسیر القرآن",
            author = "مولانا عنایت اللہ فاروقی",
            authorUrdu = "مولانا عنایت اللہ فاروقی",
            language = "Urdu",
            description = "قرآن کریم کی تفسیری و توضیحی رہنمائی پر مشتمل تحقیقی تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v11_1", "tafseer_11", 1, "Volume 1", "جلد 1", "https://archive.org/download/nov22_2/MAARIF_UL_BAYAN_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_12",
            title = "Tafseer-e-Mazhari (Urdu)",
            titleUrdu = "تفسیر مظہری (اردو)",
            author = "قاضی ثناء اللہ پانی پتی",
            authorUrdu = "قاضی ثناء اللہ پانی پتی",
            language = "Urdu",
            description = "حضرت قاضی ثناء اللہ پانی پتی رحمہ اللہ کی شہرہ آفاق روحانی و فقہی تفسیر کا اردو ترجمہ",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v12_1", "tafseer_12", 1, "Volume 1", "جلد 1", "https://archive.org/download/Mar_23/TAFSIR_E_MAZHARI_URDU_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_13",
            title = "Maarif-ul-Quran",
            titleUrdu = "معارف القرآن",
            author = "مفتی محمد شفیع عثمانی",
            authorUrdu = "مفتی محمد شفیع عثمانی",
            language = "Urdu",
            description = "مفتی اعظم پاکستان حضرت مولانا مفتی محمد شفیع عثمانی رحمہ اللہ کی شاہکار اور مقبول عام تفسیر",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v13_1", "tafseer_13", 1, "Volume 1", "جلد 1", "https://archive.org/download/Besturdubooks417/MAARIF-UL-QURAN-VOL-01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_14",
            title = "Tafseer-ul-Jalalain",
            titleUrdu = "تفسیر جلالین شریف (البشریٰ کلر)",
            author = "علامہ جلال الدین محلی و علامہ جلال الدین سیوطی",
            authorUrdu = "علامہ جلال الدین محلی و علامہ جلال الدین سیوطی",
            language = "Arabic",
            description = "درسِ نظامی کے نصاب میں شامل نہایت مستند و جامع عربی تفسیر (مکتبۃ البشریٰ رنگین ایڈیشن)",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v14_1", "tafseer_14", 1, "Volume 1", "جلد 1", "https://archive.org/download/AlSadisah6thYear1/TafseerUlJalalainVol1AlBushraColor.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_15",
            title = "Tafseer-ul-Baizawi (Anwar-ut-Tanzil)",
            titleUrdu = "تفسیر بیضاوی - انوار التنزیل (البشریٰ کلر)",
            author = "علامہ ناصر الدین بیضاوی",
            authorUrdu = "علامہ ناصر الدین بیضاوی",
            language = "Arabic",
            description = "بلاغت و اعجازِ قرآنی کے دقیق ترین اسرار و معارف پر مبنی عظیم عربی تفسیر (البشریٰ کلر ایڈیشن)",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v15_1", "tafseer_15", 1, "Volume 1", "جلد 1", "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/TafseerUlBaizawiAlBushraColor.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_16",
            title = "Tasheel I'rab-ul-Quran al-Mubeen",
            titleUrdu = "تسہیل اعراب القرآن المبین",
            author = "مولانا محمد مشتاق احمد چریاکوٹی",
            authorUrdu = "مولانا محمد مشتاق احمد چریاکوٹی",
            language = "Urdu / Arabic",
            description = "قرآن کریم کے اعرابی قواعد، نحوی تراکیب اور لغوی تجزیہ کی سہل اور مدلل کتاب",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v16_1", "tafseer_16", 1, "Volume 1", "جلد 1", "https://archive.org/download/bmay26/TASHEEL_IRAB_UL_QURAN_AL_MUBEEN.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_17",
            title = "Maarif-ul-Furqan Qasmi",
            titleUrdu = "معارف الفرقان قاسمی",
            author = "مولانا قاسم قاسمی",
            authorUrdu = "مولانا قاسم قاسمی",
            language = "Urdu",
            description = "قرآن مجید کی آسان، عام فہم اور سلیس اردو تفسیر و توضیحات",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v17_1", "tafseer_17", 1, "Volume 1", "جلد 1", "https://archive.org/download/bfeb24/MAARIF_UL_FURQAN_QASMI_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_18",
            title = "Safwat-ut-Tafasir (Urdu)",
            titleUrdu = "صفوۃ التفاسیر (اردو)",
            author = "علامہ محمد علی صابونی",
            authorUrdu = "علامہ محمد علی صابونی",
            language = "Urdu",
            description = "معتبر تفاسیر کا نچوڑ اور خلاصہ، علامہ صابونی کی شہرۂ آفاق تفسیر کا باوقار اردو ترجمہ",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v18_1", "tafseer_18", 1, "Volume 1", "جلد 1", "https://archive.org/download/st-urdu/SAFWAT_AL_TAFASIR_URDU_VOL_01.pdf")
            )
        ),
        Tafseer(
            id = "tafseer_19",
            title = "Safwat-ut-Tafasir (Arabic)",
            titleUrdu = "صفوۃ التفاسیر (عربی)",
            author = "علامہ محمد علی صابونی",
            authorUrdu = "علامہ محمد علی صابونی",
            language = "Arabic",
            description = "الصفوة من أمهات كتب التفسير بأسلوب ميسر وعصري رائع (النص العربي الكامل)",
            rightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
            volumes = listOf(
                TafseerVolume("v19_1", "tafseer_19", 1, "Volume 1", "جلد 1", "https://archive.org/download/st-urdu/SAFWAT_AL_TAFASIR_VOL_01.pdf")
            )
        )
    )
}

