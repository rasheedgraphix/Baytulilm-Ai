package com.example.data.model

enum class DifficultyLevel(
    val id: String,
    val displayName: String,
    val colorHex: Long,
    val iconEmoji: String,
    val description: String
) {
    BEGINNER("beginner", "Beginner", 0xFF4CAF50, "🟢", "Foundational Dars-e-Nizami Fiqh, Sarf & Nahw"),
    MEDIUM("medium", "Medium", 0xFFFFC107, "🟡", "Intermediate Usul al-Fiqh, Mantiq & Hadith"),
    ADVANCED("advanced", "Advanced", 0xFFFF9800, "🟠", "Advanced Al-Hidayah, Sharh Aqaid & Tafseer"),
    EXPERT("expert", "Expert", 0xFFF44336, "🔴", "Sihah Sittah Hadith Canons & Baizawi Tafseer");

    fun getDisplayName(langCode: String): String {
        return when (langCode) {
            "ps" -> when (this) {
                BEGINNER -> "ابتدایي"
                MEDIUM -> "منځنی"
                ADVANCED -> "پرمختللی"
                EXPERT -> "ماهر"
            }
            "ur" -> when (this) {
                BEGINNER -> "ابتدائی"
                MEDIUM -> "درمیانی"
                ADVANCED -> "اعلیٰ"
                EXPERT -> "ماہر"
            }
            else -> displayName
        }
    }

    fun getDisplayName(isUrdu: Boolean): String {
        return getDisplayName(if (isUrdu) "ur" else "en")
    }

    fun getDescription(langCode: String): String {
        return when (langCode) {
            "ps" -> when (this) {
                BEGINNER -> "بنیادي درسِ نظامي فقه، صرف او نحو"
                MEDIUM -> "منځنۍ کچه اصولِ فقه، منطق او حدیث"
                ADVANCED -> "پرمختللې کچه الهدايه، شرح عقائد او تفسیر"
                EXPERT -> "صحاح ستة حدیث او تفسیر بیضاوي"
            }
            "ur" -> when (this) {
                BEGINNER -> "بنیادی درسِ نظامی فقہ، صرف اور نحو"
                MEDIUM -> "درمیانی اصولِ فقہ، منطق اور حدیث"
                ADVANCED -> "اعلیٰ سطح الہدایہ، شرح عقائد اور تفسیر"
                EXPERT -> "صحاح ستہ حدیث و تفسیر بیضاوی"
            }
            else -> description
        }
    }

    fun getDescription(isUrdu: Boolean): String {
        return getDescription(if (isUrdu) "ur" else "en")
    }
}

data class ChapterProgress(
    val difficulty: String, // "beginner", "medium", "advanced", "expert"
    val chapterNumber: Int, // 1 to 50
    val isUnlocked: Boolean = (chapterNumber == 1),
    val isCompleted: Boolean = false,
    val highestScore: Int = 0, // out of 50
    val highestPercentage: Float = 0f,
    val attempts: Int = 0,
    val totalCorrect: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val accuracyPercentage: Float = 0f,
    val timeTakenSeconds: Long = 0
)

data class ComprehensiveQuizQuestion(
    val id: String,
    val quizId: String,
    val question: String,
    val questionUrdu: String? = null,
    val questionPashto: String? = null,
    val arabicText: String? = null,
    val translation: String? = null,
    val translationUrdu: String? = null,
    val translationPashto: String? = null,
    val options: List<String>,
    val optionsUrdu: List<String>? = null,
    val optionsPashto: List<String>? = null,
    val correctAnswerIndex: Int,
    val explanation: String,
    val explanationUrdu: String? = null,
    val explanationPashto: String? = null,
    val bookName: String = "Mukhtasar al-Quduri",
    val darja: String = "Darja-e-Ula",
    val subject: String = "Fiqh",
    val chapter: String = "Taharah",
    val pageNumber: Int = 12,
    val difficulty: String = "Medium", // Easy, Medium, Hard
    val marks: Int = 5,
    val questionType: String = "MCQ", // MCQ, TrueFalse, FillBlank, Grammar, Translation
    var isBookmarked: Boolean = false,
    var isFavorite: Boolean = false
) {
    fun getDisplayQuestion(langCode: String): String {
        return when (langCode) {
            "ps" -> questionPashto.takeIf { !it.isNullOrBlank() }
                ?: questionUrdu.takeIf { !it.isNullOrBlank() }?.let { PashtoQuizTranslator.toPashto(it) }
                ?: question
            "ur" -> questionUrdu.takeIf { !it.isNullOrBlank() } ?: question
            else -> question
        }
    }

    fun getDisplayOptions(langCode: String): List<String> {
        return when (langCode) {
            "ps" -> optionsPashto.takeIf { !it.isNullOrEmpty() }
                ?: optionsUrdu.takeIf { !it.isNullOrEmpty() }?.map { PashtoQuizTranslator.toPashto(it) }
                ?: options
            "ur" -> optionsUrdu.takeIf { !it.isNullOrEmpty() } ?: options
            else -> options
        }
    }

    fun getDisplayExplanation(langCode: String): String {
        return when (langCode) {
            "ps" -> explanationPashto.takeIf { !it.isNullOrBlank() }
                ?: explanationUrdu.takeIf { !it.isNullOrBlank() }?.let { PashtoQuizTranslator.toPashto(it) }
                ?: explanation
            "ur" -> explanationUrdu.takeIf { !it.isNullOrBlank() } ?: explanation
            else -> explanation
        }
    }

    fun getDisplayCitation(langCode: String): String {
        return when (langCode) {
            "ps" -> "سرچینه: $bookName • $subject (پاڼه $pageNumber)"
            "ur" -> "حوالہ: $bookName • $subject (صفحہ $pageNumber)"
            else -> "Reference: $bookName • $subject (Page $pageNumber)"
        }
    }
}

object PashtoQuizTranslator {
    fun toPashto(text: String?): String {
        if (text.isNullOrBlank()) return ""
        var s = text
        s = s.replace("کا وزن اور صیغہ کیا ہے؟", "وزن او صیغه څه ده؟")
            .replace("کا صیغہ کیا ہے؟", "صیغه څه ده؟")
            .replace("کی تعریف کیا ہے؟", "تعریف څه دی؟")
            .replace("کا کیا حکم ہے؟", "حکم څه دی؟")
            .replace("کون سا ہے؟", "کوم یو دی؟")
            .replace("کون سی ہے؟", "کومه ده؟")
            .replace("کیا ہے؟", "څه دی؟")
            .replace("کہتے ہیں", "وایي")
            .replace("درست جواب", "سم ځواب")
            .replace("غلط جواب", "غلط ځواب")
            .replace("واحد مذکر غائب", "واحد مذکر غائب")
            .replace("واحد مذکر حاضر", "واحد مذکر حاضر")
            .replace("جمع مذکر غائب", "جمع مذکر غائب")
            .replace("جمع مذکر حاضر", "جمع مذکر حاضر")
            .replace("فعل ماضی معروف", "فعل ماضی معروف")
            .replace("فعل ماضی مجہول", "فعل ماضی مجہول")
            .replace("فعل مضارع معروف", "فعل مضارع معروف")
            .replace("فعل مضارع مجہول", "فعل مضارع مجہول")
            .replace("فعل امر حاضر معروف", "فعل امر حاضر معروف")
            .replace("فعل نہی معروف", "فعل نہی معروف")
            .replace("اسم فاعل", "اسم فاعل")
            .replace("اسم مفعول", "اسم مفعول")
            .replace("ثلاثی مجرد", "ثلاثي مجرد")
            .replace("ثلاثی مزید فیہ", "ثلاثي مزید فیه")
            .replace("کا فعل ماضی معروف ہے۔", "فعل ماضی معروف دی.")
            .replace("کا فعل مضارع معروف ہے۔", "فعل مضارع معروف دی.")
            .replace("موجود ہے، جو", "موجود دی، چې")
            .replace("کا صیغہ ہے۔", "صیغه ده.")
            .replace("حوالہ:", "سرچینه:")
            .replace("صفحہ", "پاڼه")
            .replace(" اور ", " او ")
            .replace(" میں ", " کې ")
            .replace(" پر ", " په ")
            .replace(" سے ", " له ")
            .replace(" کا ", " د ")
            .replace(" کی ", " د ")
            .replace(" کے ", " د ")
            .replace(" ہے۔", " دی.")
            .replace(" ہیں۔", " دي.")
        return s
    }
}

data class QuizSet(
    val id: String,
    val title: String,
    val titleUrdu: String? = null,
    val description: String,
    val descriptionUrdu: String? = null,
    val darja: String,
    val subject: String,
    val chapter: String,
    val bookName: String,
    val questionCount: Int,
    val durationMinutes: Int,
    val totalMarks: Int,
    val difficulty: String = "Medium",
    val questions: List<ComprehensiveQuizQuestion> = emptyList()
)

data class QuizResultRecord(
    val id: String,
    val quizTitle: String,
    val darja: String,
    val subject: String,
    val score: Int,
    val totalQuestions: Int,
    val totalMarks: Int,
    val percentage: Float,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedQuestions: Int,
    val timeTakenSeconds: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: String = "Exam",
    val rank: String = "Top 5%"
)

data class CertificateInfo(
    val id: String,
    val studentName: String = "Hafiz Muhammad Rasheed",
    val courseName: String = "Dars-e-Nizami Islamic Assessment",
    val darja: String,
    val subject: String,
    val score: Int,
    val percentage: Float,
    val date: String,
    val verificationQrCode: String
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val darja: String,
    val totalScore: Int,
    val quizzesCompleted: Int,
    val badge: String
)

data class QuizBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: String = "Unlocked 2026"
)

data class QuizProgressStats(
    val completedQuizzes: Int = 12,
    val averageScore: Float = 88.5f,
    val totalQuestionsAnswered: Int = 180,
    val correctPercentage: Float = 89.2f,
    val strongSubjects: List<String> = listOf("Fiqh", "Nahw", "Hadith"),
    val weakSubjects: List<String> = listOf("Balagha", "Mantiq"),
    val studyHours: Float = 14.5f
)

data class ChapterMeta(
    val stepId: String,
    val chapterNumber: Int,
    val titleUrdu: String,
    val titleEn: String,
    val assignedDarjatUrdu: String,
    val assignedDarjatEn: String,
    val bookName: String,
    val subject: String
)

