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

    fun getDisplayName(isUrdu: Boolean): String {
        return if (isUrdu) {
            when (this) {
                BEGINNER -> "ابتدائی"
                MEDIUM -> "درمیانی"
                ADVANCED -> "اعلیٰ"
                EXPERT -> "ماہر"
            }
        } else displayName
    }

    fun getDescription(isUrdu: Boolean): String {
        return if (isUrdu) {
            when (this) {
                BEGINNER -> "بنیادی درسِ نظامی فقہ، صرف اور نحو"
                MEDIUM -> "درمیانی اصولِ فقہ، منطق اور حدیث"
                ADVANCED -> "اعلیٰ سطح الہدایہ، شرح عقائد اور تفسیر"
                EXPERT -> "صحاح ستہ حدیث و تفسیر بیضاوی"
            }
        } else description
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
    val arabicText: String? = null,
    val translation: String? = null,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
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
)

data class QuizSet(
    val id: String,
    val title: String,
    val description: String,
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
