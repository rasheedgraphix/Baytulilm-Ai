package com.example.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "Aalim Student",
    val email: String = "student@baytulilmai.app",
    val photo: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
    val phone: String = "+92 300 1234567",
    val country: String = "Pakistan",
    val province: String = "Punjab",
    val city: String = "Lahore",
    val role: String = UserRole.STUDENT, // Super Admin, Admin, Teacher, Student
    val language: String = "Urdu",
    val theme: String = "System",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = System.currentTimeMillis(),
    val emailVerified: Boolean = true,
    val isPremium: Boolean = true,
    val bookmarks: List<String> = emptyList(),
    val favorites: List<String> = emptyList(),
    val downloads: List<String> = emptyList(),
    val quizScore: Int = 180,
    val readingHistory: List<String> = emptyList(),
    val certificateList: List<String> = listOf("cert_001_darse_nizami_basics")
)

object UserRole {
    const val SUPER_ADMIN = "Super Admin"
    const val ADMIN = "Admin"
    const val TEACHER = "Teacher"
    const val STUDENT = "Student"
    const val GUEST = "Guest"
}

data class BookDoc(
    val id: String = "",
    val title: String = "",
    val titleArabic: String = "",
    val author: String = "",
    val description: String = "",
    val darja: String = "Darja-e-Ula",
    val subject: String = "Nahw",
    val language: String = "Arabic/Urdu",
    val publisher: String = "Maktaba Al-Bushra",
    val edition: String = "1st Edition",
    val coverImage: String = "",
    val pdfUrl: String = "",
    val pages: Int = 120,
    val size: String = "15 MB",
    val downloads: Int = 240,
    val views: Int = 1050,
    val rating: Float = 4.9f,
    val isFeatured: Boolean = true,
    val isMainBook: Boolean = true,
    val isSharh: Boolean = false,
    val isTranslation: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class DarjaDoc(
    val id: String = "",
    val name: String = "",
    val arabicName: String = "",
    val urduName: String = "",
    val description: String = "",
    val descriptionUrdu: String = "",
    val thumbnail: String = "",
    val bookCount: Int = 0,
    val subjectCount: Int = 0,
    val order: Int = 1,
    val progress: Float = 0.0f
) {
    fun getDisplayName(isUrdu: Boolean = true): String {
        return if (isUrdu && urduName.isNotBlank()) urduName else name
    }
    fun getDisplayDescription(isUrdu: Boolean = true): String {
        return if (isUrdu && descriptionUrdu.isNotBlank()) descriptionUrdu else description
    }
}

data class SubjectDoc(
    val id: String = "",
    val name: String = "",
    val arabicName: String = "",
    val category: String = "Islamic Sciences",
    val booksCount: Int = 0
)

data class McqQuestion(
    val id: Int = 1,
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: Int = 0,
    val explanation: String = "",
    val marks: Int = 10
)

data class QuizDoc(
    val id: String = "",
    val title: String = "",
    val chapter: String = "",
    val subject: String = "Fiqh",
    val darja: String = "Darja-e-Ula",
    val mcqs: List<McqQuestion> = emptyList(),
    val difficulty: String = "Medium",
    val marks: Int = 100,
    val timeLimitMinutes: Int = 15,
    val createdBy: String = "Teacher Aalim",
    val createdAt: Long = System.currentTimeMillis()
)

data class QuizResultDoc(
    val id: String = "",
    val studentUid: String = "",
    val studentName: String = "",
    val quizId: String = "",
    val quizTitle: String = "",
    val score: Int = 0,
    val maxScore: Int = 100,
    val percentage: Float = 0f,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val dateTimestamp: Long = System.currentTimeMillis()
)

data class BookmarkDoc(
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val page: Int = 1,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class ReadingHistoryDoc(
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val currentPage: Int = 1,
    val totalPages: Int = 120,
    val lastOpened: Long = System.currentTimeMillis(),
    val readingTimeSeconds: Long = 0
)

data class DownloadDoc(
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val downloadedAt: Long = System.currentTimeMillis(),
    val pdfPath: String = ""
)

data class DailyHadithDoc(
    val id: String = "",
    val textArabic: String = "",
    val textTranslation: String = "",
    val reference: String = "",
    val narrator: String = "",
    val date: String = ""
)

data class DailyAyahDoc(
    val id: String = "",
    val surahName: String = "",
    val verseNumber: Int = 1,
    val textArabic: String = "",
    val textTranslation: String = "",
    val date: String = ""
)

data class AnnouncementDoc(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val author: String = "Baytul Ilm Admin",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "General" // General, New Book, Daily Hadith, Quiz Alert
)

data class NotificationDoc(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class RemoteConfigSettings(
    val latestVersion: String = "1.0.0",
    val maintenanceMode: Boolean = false,
    val importantMessage: String = "Welcome to Baytul Ilm AI Digital Hub! Enjoy full access to Dars-e-Nizami Kutub, AI Scholar & Prayer Tools.",
    val minRequiredVersion: String = "1.0.0"
)

data class CertificateDoc(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val courseName: String = "",
    val issuedAt: Long = System.currentTimeMillis(),
    val certificateUrl: String = ""
)
