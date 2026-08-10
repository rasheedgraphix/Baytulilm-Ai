package com.example.data.model

data class StudentProgress(
    val booksRead: Int = 12,
    val pagesRead: Int = 1450,
    val readingTimeHours: Float = 42.5f,
    val completedSubjects: Int = 8,
    val completedDarjat: Int = 2,
    val completedQuizzes: Int = 18,
    val averageScore: Float = 91.5f,
    val learningStreak: Int = 14,
    val dailyGoalPages: Int = 30,
    val currentDailyPages: Int = 22,
    val weeklyGoalHours: Float = 10f,
    val currentWeeklyHours: Float = 8.5f,
    val monthlyGoalHours: Float = 40f,
    val currentMonthlyHours: Float = 32f,
    val weeklyStudyHours: List<Float> = listOf(1.2f, 1.8f, 2.0f, 0.5f, 1.5f, 2.5f, 1.0f), // Mon-Sun
    val monthlyStudyHours: List<Float> = listOf(28f, 32f, 35f, 30f, 42f, 38f) // Past 6 months
)

data class StudentNoteDoc(
    val id: String = "",
    val studentUid: String = "student_001",
    val bookId: String = "book_1",
    val bookTitle: String = "Al-Hidayah (Sharh Bidayat al-Mubtadi)",
    val pageNumber: Int = 45,
    val highlightedText: String = "الطهور شطر الإيمان والحمد لله تملأ Mizan",
    val noteText: String = "Important definition of Taharah in Hanafi Fiqh according to Imam Al-Marghinani.",
    val colorCategory: String = "Yellow", // Yellow, Green, Blue, Pink
    val isFavorite: Boolean = true,
    val tags: List<String> = listOf("Fiqh", "Taharah", "Exam Prep"),
    val timestamp: Long = System.currentTimeMillis()
)

data class StudyPlanDoc(
    val id: String = "",
    val studentUid: String = "student_001",
    val title: String = "Revise Nahw Mir & Kafiyah",
    val description: String = "Review chapters on Marfu'at and Mansubat for upcoming midterm exams.",
    val subjectName: String = "Nahw",
    val scheduleType: String = "Daily", // Daily, Weekly, Monthly
    val timeSlot: String = "08:00 AM - 09:30 AM",
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true,
    val dateTimestamp: Long = System.currentTimeMillis()
)

data class AssignmentDoc(
    val id: String = "",
    val title: String = "Sharh Ibn 'Aqil Grammar Parsing Task",
    val description: String = "Translate and parse (I'rab) the first 10 verses of Surah Al-Kahf using Ibn 'Aqil rules.",
    val teacherName: String = "Mufti Muhammad Taqi",
    val subject: String = "Nahw / Arabic Grammar",
    val darja: String = "Darja-e-Rabia",
    val dueDate: String = "July 30, 2026",
    val maxMarks: Int = 50,
    val assignmentType: String = "Homework", // Homework, Reading Task, Assignment
    val attachmentsUrl: String = "sample_grammar_sheet.pdf"
)

data class AssignmentSubmissionDoc(
    val id: String = "",
    val assignmentId: String = "assign_001",
    val assignmentTitle: String = "Sharh Ibn 'Aqil Grammar Parsing Task",
    val studentUid: String = "student_001",
    val studentName: String = "Aalim Student",
    val submissionDate: Long = System.currentTimeMillis(),
    val textResponse: String = "I have completed the full parsing of Surah Al-Kahf verses 1-10 with references to Sharh Ibn Aqil.",
    val fileUrl: String = "submission_irab_solution.pdf",
    val fileType: String = "PDF", // PDF, Image, Text
    val status: String = "Graded", // Pending, Submitted, Graded
    val grade: String = "48 / 50",
    val teacherFeedback: String = "Excellent work on Marfu'at analysis. Keep up the thorough parsing!"
)

data class AchievementBadgeDoc(
    val id: String = "",
    val title: String = "First Book Completed",
    val description: String = "Read your first complete Kitab in Dars-e-Nizami",
    val iconName: String = "MenuBook",
    val category: String = "Reading",
    val dateUnlocked: String = "June 15, 2026",
    val progressPercentage: Float = 1.0f,
    val isUnlocked: Boolean = true
)

data class StudentCertificateDoc(
    val id: String = "",
    val studentUid: String = "student_001",
    val studentName: String = "Aalim Student",
    val darjaName: String = "Darja-e-Salisa (3rd Year)",
    val courseName: String = "Dars-e-Nizami Intermediate Qualification",
    val issueDate: String = "July 10, 2026",
    val certificateCode: String = "CERT-BAYTULILM-2026-8849",
    val qrVerificationUrl: String = "https://baytulilmai.app/verify/CERT-BAYTULILM-2026-8849",
    val pdfUrl: String = "darse_nizami_certificate.pdf"
)

data class LeaderboardEntryDoc(
    val id: String = "",
    val category: String = "Top Readers", // Top Readers, Top Quiz Scores, Most Active Students
    val studentUid: String = "student_001",
    val studentName: String = "Aalim Student",
    val studentPhoto: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
    val darja: String = "Darja-e-Rabia",
    val rank: Int = 1,
    val scoreValue: String = "1,450 Pages",
    val metricNumber: Int = 1450
)

// Digital Madrasa & Live Classes
data class LiveClassDoc(
    val id: String = "",
    val title: String = "Live Fiqh Halaqah: Sharh Al-Hidayah",
    val teacherName: String = "Mufti Muhammad Taqi Usmani",
    val subject: String = "Fiqh & Usul",
    val darja: String = "Darja-e-Rabia",
    val platform: String = "Google Meet", // Google Meet, Zoom, YouTube Live, Jitsi Meet
    val meetingLink: String = "https://meet.google.com/abc-defg-hij",
    val scheduledTime: String = "Today, 05:00 PM - 06:30 PM",
    val isLiveNow: Boolean = true,
    val totalAttendees: Int = 142,
    val recordingUrl: String = "https://youtube.com/watch?v=live_recording_sample"
)

// Video Courses
data class VideoLessonDoc(
    val id: String = "",
    val title: String = "Lesson 1: Introduction to Marfu'at",
    val durationMinutes: Int = 28,
    val videoUrl: String = "sample_video_lesson_1.mp4",
    val isCompleted: Boolean = true,
    val isDownloaded: Boolean = true
)

data class VideoCourseDoc(
    val id: String = "",
    val title: String = "Comprehensive Nahw Mir & Kafiyah Video Series",
    val subject: String = "Nahw / Syntax",
    val darja: String = "Darja-e-Thania",
    val teacherName: String = "Maulana Abdul Salam",
    val thumbnailUrl: String = "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=400&q=80",
    val description: String = "Detailed video lectures covering all chapters of Nahw Mir with practical parsing exercises.",
    val lessons: List<VideoLessonDoc> = emptyList(),
    val progressPercentage: Float = 0.65f,
    val lastWatchedLessonTitle: String = "Lesson 3: Na'ib al-Fa'il Rules"
)

// Audio Lectures
data class AudioLectureDoc(
    val id: String = "",
    val title: String = "Dars 42: Kitab al-Buyu' - Riba & Valid Sales",
    val speaker: String = "Shaykh al-Hadith Maulana Muhammad Zayd",
    val durationText: String = "45:20",
    val audioUrl: String = "sample_audio_dars.mp3",
    val category: String = "Fiqh",
    val isDownloaded: Boolean = true,
    val bookmarksCount: Int = 4,
    val currentPositionMs: Long = 120000L
)

// Examination & Result System
data class SubjectMarkDoc(
    val subjectName: String,
    val totalMarks: Int = 100,
    val obtainedMarks: Int
)

data class ExamResultDoc(
    val id: String = "",
    val studentUid: String = "student_001",
    val studentName: String = "Aalim Student",
    val darja: String = "Darja-e-Rabia",
    val examTerm: String = "Mid Term Examination 2026", // Mid Term, Final Exam, Monthly Test
    val totalMarks: Int = 500,
    val obtainedMarks: Int = 468,
    val percentage: Float = 93.6f,
    val grade: String = "A+",
    val position: String = "1st Position",
    val meritRank: Int = 1,
    val remarks: String = "Mumtaz (Outstanding Performance in Fiqh & Nahw)",
    val subjectMarks: List<SubjectMarkDoc> = listOf(
        SubjectMarkDoc("Sharh Ibn 'Aqil (Nahw)", 100, 96),
        SubjectMarkDoc("Al-Hidayah (Fiqh)", 100, 94),
        SubjectMarkDoc("Mishkat al-Masabih (Hadith)", 100, 95),
        SubjectMarkDoc("Nukhbat al-Fikar (Usul)", 100, 92),
        SubjectMarkDoc("Tafsir Jalalayn (Tafsir)", 100, 91)
    )
)

// Discussion Forum
data class ForumCommentDoc(
    val id: String = "",
    val authorName: String = "Maulana Abdul Hafeez",
    val authorRole: String = "Teacher / Ustadh",
    val text: String = "In Hanafi Fiqh, intention (Niyyah) in Wudu is Mustahabb (recommended), not a Fard, unlike Shafi'i Fiqh.",
    val timestamp: String = "2 hours ago",
    val isTeacherReply: Boolean = true
)

data class ForumPostDoc(
    val id: String = "",
    val title: String = "Difference in Niyyah for Wudu between Hanafi & Shafi'i Schools?",
    val question: String = "Can someone clarify the evidence (Daleel) regarding Niyyah in Taharah across the Madhahib?",
    val authorName: String = "Aalim Student",
    val authorRole: String = "Student",
    val subject: String = "Fiqh & Usul",
    val darja: String = "Darja-e-Salisa",
    val likesCount: Int = 24,
    val isLiked: Boolean = true,
    val isPinned: Boolean = true,
    val comments: List<ForumCommentDoc> = emptyList()
)

// Direct Messaging
data class DirectMessageDoc(
    val id: String = "",
    val senderName: String = "Mufti Muhammad Taqi",
    val senderRole: String = "Teacher",
    val receiverName: String = "Aalim Student",
    val messageText: String = "Assalamu Alaikum. Please review the updated parsing sheet for Sharh Ibn Aqil before class.",
    val mediaType: String = "PDF", // None, Image, PDF, Voice
    val mediaUrl: String = "sharh_ibn_aqil_parsing.pdf",
    val timestamp: String = "10:30 AM",
    val isMeSender: Boolean = false
)

// Parent / Guardian Dashboard
data class ParentChildOverview(
    val childName: String = "Muhammad Abdullah",
    val darja: String = "Darja-e-Rabia (4th Year)",
    val madrasaName: String = "Jamia Baytul Ilm Islamic Seminary",
    val overallAttendance: Float = 98.2f,
    val totalStudyHours: Float = 52.5f,
    val quizAverage: Float = 93.8f,
    val pendingHomeworks: Int = 1,
    val completedHomeworks: Int = 12,
    val teacherRemarks: String = "Abdullah displays exceptional dedication in Fiqh & Arabic Grammar. Top performer of the class.",
    val strongSubjects: List<String> = listOf("Nahw & Sarf", "Fiqh (Al-Hidayah)", "Hadith (Mishkat)"),
    val weakSubjects: List<String> = listOf("Mantiq (Logic)", "Usul al-Fiqh")
)

// OCR & Image to Text
data class OcrResultDoc(
    val id: String = "",
    val imageUrl: String = "",
    val extractedArabicText: String = "الطهور شطر الإيمان والحمد لله تملأ الميزان وسبحان الله والحمد لله تملآن ما بين السماوات والأرض",
    val extractedUrduText: String = "پاکیزگی ایمان کا نصف حصہ ہے اور الحمد للہ ترازو کو بھر دیتا ہے۔",
    val extractedEnglishText: String = "Purity is half of faith, and Al-hamdulillah fills the scale of good deeds.",
    val sourceBook: String = "Sahih Muslim & Mishkat al-Masabih",
    val confidencePercentage: Int = 98
)

// Gamification
data class GamificationProfile(
    val totalXp: Int = 3450,
    val currentLevel: Int = 8,
    val levelTitle: String = "Mumtaz Scholar (ممتاز)",
    val coins: Int = 420,
    val learningStreakDays: Int = 18,
    val dailyRewardClaimed: Boolean = false,
    val weeklyChallengeProgress: Float = 0.8f // 4 / 5 tasks done
)

