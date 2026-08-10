package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object LanguageSelection : Screen("language_selection", "Choose Language")
    object Splash : Screen("splash", "Splash")
    object EmailVerification : Screen("email_verification", "Email Verification")
    object Home : Screen("home", "Home")
    object Library : Screen("library", "Library")
    object Quiz : Screen("quiz", "Quiz")
    object AiAssistant : Screen("ai_assistant", "AI Scholar")
    object AiTeacher : Screen("ai_teacher", "AI Teacher")
    object Profile : Screen("profile", "Profile")

    object Darjat : Screen("darjat", "Darjat Curriculum")
    object DarjaDetail : Screen("darja_detail/{darjaName}", "Darja Books") {
        fun createRoute(darjaName: String) = "darja_detail/$darjaName"
    }

    object Subjects : Screen("subjects", "Subjects")
    object SubjectDetail : Screen("subject_detail/{subjectName}", "Subject Books") {
        fun createRoute(subjectName: String) = "subject_detail/$subjectName"
    }

    object BookDetail : Screen("book_detail/{bookId}", "Book Details") {
        fun createRoute(bookId: String) = "book_detail/$bookId"
    }

    object BookViewer : Screen("book_viewer/{bookId}", "Book Reader") {
        fun createRoute(bookId: String) = "book_viewer/$bookId"
    }

    object Search : Screen("search", "Search Books")
    object Bookmarks : Screen("bookmarks", "Bookmarks")
    object Favorites : Screen("favorites", "Favorites")
    object Recent : Screen("recent", "Recent Reading")
    object Downloads : Screen("downloads", "Downloads")
    object Offline : Screen("offline", "Offline")
    object Settings : Screen("settings", "Settings")
    object About : Screen("about", "About & Contact")
    object Login : Screen("login", "Sign In")
    object Register : Screen("register", "Create Account")
    object ForgotPassword : Screen("forgot_password", "Reset Password")

    // Student Dashboard & LMS Routes
    object StudentDashboard : Screen("student_dashboard", "Student Dashboard")
    object LmsStudyPlanner : Screen("lms_planner", "Study Planner & Schedule")
    object LmsNotes : Screen("lms_notes", "Notes & Highlighting")
    object LmsAssignments : Screen("lms_assignments", "Assignments & Homework")
    object LmsCertificates : Screen("lms_certificates", "Certificates & Diplomas")
    object LmsAchievements : Screen("lms_achievements", "Achievements & Badges")
    object LmsLeaderboard : Screen("lms_leaderboard", "Student Leaderboard")
    object LmsProfileEdit : Screen("lms_profile_edit", "Edit Profile")

    // Digital Madrasa Extended Modules
    object LiveClasses : Screen("live_classes", "Live Classes & Halaqah")
    object VideoCourses : Screen("video_courses", "Video Lectures & Lessons")
    object AudioLectures : Screen("audio_lectures", "Audio Daroos & Streaming")
    object ExamResults : Screen("exam_results", "Exam Results & Report Cards")
    object DiscussionForum : Screen("discussion_forum", "Q&A Discussion Forum")
    object DirectMessaging : Screen("direct_messaging", "Messages & Ustadh Chat")
    object OcrAssistant : Screen("ocr_assistant", "Book Page OCR & AI Scanner")
    object ParentDashboard : Screen("parent_dashboard", "Parent & Guardian Panel")
    object AdminBulkOps : Screen("admin_bulk_ops", "Admin Bulk Operations & Import")

    // Admin & CMS Routes
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard & CMS")
    object AdminRoleManagement : Screen("admin_roles", "Role & Permission Management")
    object AdminBookManagement : Screen("admin_books", "Book & PDF Management")
    object AdminShuroohTranslations : Screen("admin_shurooh", "Shurooh & Translations")
    object AdminQuizManagement : Screen("admin_quizzes", "Quiz Management & AI Generator")
    object AdminContentManagement : Screen("admin_content", "Notes, Videos & Audios")
    object AdminPushNotifications : Screen("admin_notifications", "Push Notifications & Announcements")
    object AdminUserManagement : Screen("admin_users", "User Management & Histories")
    object AdminAnalytics : Screen("admin_analytics", "Analytics & System Performance")
    object AdminCertificates : Screen("admin_certificates", "Certificates Management")
    object AdminBackupSettings : Screen("admin_backup_settings", "Database Backup & Remote Settings")
    object AdminSecurity : Screen("admin_security", "Firestore Rules & App Check")
}
