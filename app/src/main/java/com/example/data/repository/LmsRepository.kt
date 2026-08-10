package com.example.data.repository

import com.example.data.model.AchievementBadgeDoc
import com.example.data.model.AssignmentDoc
import com.example.data.model.AssignmentSubmissionDoc
import com.example.data.model.AudioLectureDoc
import com.example.data.model.DirectMessageDoc
import com.example.data.model.ExamResultDoc
import com.example.data.model.ForumCommentDoc
import com.example.data.model.ForumPostDoc
import com.example.data.model.GamificationProfile
import com.example.data.model.LeaderboardEntryDoc
import com.example.data.model.LiveClassDoc
import com.example.data.model.OcrResultDoc
import com.example.data.model.ParentChildOverview
import com.example.data.model.StudentCertificateDoc
import com.example.data.model.StudentNoteDoc
import com.example.data.model.StudentProgress
import com.example.data.model.StudyPlanDoc
import com.example.data.model.SubjectMarkDoc
import com.example.data.model.VideoCourseDoc
import com.example.data.model.VideoLessonDoc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LmsRepository {

    private val _studentProgress = MutableStateFlow(
        StudentProgress(
            booksRead = 14,
            pagesRead = 1680,
            readingTimeHours = 52.5f,
            completedSubjects = 10,
            completedDarjat = 3,
            completedQuizzes = 24,
            averageScore = 93.8f,
            learningStreak = 18,
            dailyGoalPages = 30,
            currentDailyPages = 26,
            weeklyGoalHours = 12f,
            currentWeeklyHours = 9.5f,
            monthlyGoalHours = 45f,
            currentMonthlyHours = 38f,
            weeklyStudyHours = listOf(1.5f, 2.0f, 2.5f, 1.0f, 2.2f, 3.0f, 1.8f),
            monthlyStudyHours = listOf(30f, 34f, 38f, 36f, 44f, 42f)
        )
    )
    val studentProgress: StateFlow<StudentProgress> = _studentProgress.asStateFlow()

    private val _studentNotes = MutableStateFlow(
        listOf(
            StudentNoteDoc(
                id = "note_001",
                bookId = "book_1",
                bookTitle = "Al-Hidayah (Sharh Bidayat al-Mubtadi)",
                pageNumber = 45,
                highlightedText = "الطهور شطر الإيمان والحمد لله تملأ Mizan",
                noteText = "Core principle of Taharah in Hanafi Jurisprudence according to Imam Al-Marghinani.",
                colorCategory = "Yellow",
                isFavorite = true,
                tags = listOf("Fiqh", "Taharah", "Exam Notes")
            ),
            StudentNoteDoc(
                id = "note_002",
                bookId = "book_2",
                bookTitle = "Sharh Ibn 'Aqil 'Ala Alfiyyat Ibn Malik",
                pageNumber = 112,
                highlightedText = "الكلام هو اللفظ المفيد كاستقم واسم وفعل ثم حرف الكلم",
                noteText = "Definition of Kalam in Arabic Syntax (Nahw) - Requires articulate utterance and complete meaning.",
                colorCategory = "Green",
                isFavorite = false,
                tags = listOf("Nahw", "Ibn Aqil", "Grammar")
            ),
            StudentNoteDoc(
                id = "note_003",
                bookId = "book_3",
                bookTitle = "Mishkat al-Masabih Vol 1",
                pageNumber = 88,
                highlightedText = "إنما الأعمال بالنيات وإنما لكل امرئ ما نوى",
                noteText = "Hadith on Sincerity & Niyyah. Foundation of all ibadah in Islamic Law.",
                colorCategory = "Blue",
                isFavorite = true,
                tags = listOf("Hadith", "Niyyah", "Bukhari")
            )
        )
    )
    val studentNotes: StateFlow<List<StudentNoteDoc>> = _studentNotes.asStateFlow()

    private val _studyPlans = MutableStateFlow(
        listOf(
            StudyPlanDoc(
                id = "plan_001",
                title = "Morning Nahw Revision - Kafiyah",
                description = "Read 15 pages on Mansubat and solve 5 parsing exercises.",
                subjectName = "Nahw",
                scheduleType = "Daily",
                timeSlot = "07:30 AM - 09:00 AM",
                isCompleted = true,
                reminderEnabled = true
            ),
            StudyPlanDoc(
                id = "plan_002",
                title = "Fiqh Kitab al-Buyu' Analysis",
                description = "Review sales contracts and invalid transactions in Al-Quduri.",
                subjectName = "Fiqh",
                scheduleType = "Daily",
                timeSlot = "02:00 PM - 03:30 PM",
                isCompleted = false,
                reminderEnabled = true
            ),
            StudyPlanDoc(
                id = "plan_003",
                title = "Weekly Hadith Memorization & Takhrij",
                description = "Memorize 10 Hadiths from Mishkat with narrators' biographies.",
                subjectName = "Hadith",
                scheduleType = "Weekly",
                timeSlot = "Saturday 10:00 AM - 01:00 PM",
                isCompleted = false,
                reminderEnabled = true
            )
        )
    )
    val studyPlans: StateFlow<List<StudyPlanDoc>> = _studyPlans.asStateFlow()

    private val _assignments = MutableStateFlow(
        listOf(
            AssignmentDoc(
                id = "assign_001",
                title = "Sharh Ibn 'Aqil Grammar Parsing Task",
                description = "Translate and parse (I'rab) the first 10 verses of Surah Al-Kahf using Ibn 'Aqil rules.",
                teacherName = "Mufti Muhammad Taqi",
                subject = "Nahw",
                darja = "Darja-e-Rabia",
                dueDate = "July 30, 2026",
                maxMarks = 50,
                assignmentType = "Homework"
            ),
            AssignmentDoc(
                id = "assign_002",
                title = "Al-Hidayah Kitab al-Salat Case Research",
                description = "Write a 3-page research essay on Sajdah al-Sahw cases in Hanafi Fiqh with proofs from Sunan.",
                teacherName = "Maulana Abdul Hafeez",
                subject = "Fiqh",
                darja = "Darja-e-Khamisa",
                dueDate = "August 05, 2026",
                maxMarks = 100,
                assignmentType = "Assignment"
            ),
            AssignmentDoc(
                id = "assign_003",
                title = "Read Chapter 4 of Nukhbat al-Fikar",
                description = "Read pages 45-70 on Khabar Ahad and Mutawatir conditions before tomorrow's lecture.",
                teacherName = "Shaykh Muhammad Zayd",
                subject = "Usul al-Hadith",
                darja = "Darja-e-Sadisa",
                dueDate = "July 26, 2026",
                maxMarks = 20,
                assignmentType = "Reading Task"
            )
        )
    )
    val assignments: StateFlow<List<AssignmentDoc>> = _assignments.asStateFlow()

    private val _submissions = MutableStateFlow(
        listOf(
            AssignmentSubmissionDoc(
                id = "sub_001",
                assignmentId = "assign_001",
                assignmentTitle = "Sharh Ibn 'Aqil Grammar Parsing Task",
                studentUid = "student_001",
                studentName = "Aalim Student",
                submissionDate = System.currentTimeMillis() - 86400000L,
                textResponse = "Attached complete PDF solution containing verse by verse I'rab breakdown.",
                fileUrl = "surah_kahf_irab_solution.pdf",
                fileType = "PDF",
                status = "Graded",
                grade = "48 / 50",
                teacherFeedback = "SubhanAllah! Outstanding accuracy in Marfu'at and Mansubat classification."
            )
        )
    )
    val submissions: StateFlow<List<AssignmentSubmissionDoc>> = _submissions.asStateFlow()

    private val _achievements = MutableStateFlow(
        listOf(
            AchievementBadgeDoc(
                id = "badge_1",
                title = "First Book Completed",
                description = "Successfully read 1 full Kitab in Dars-e-Nizami curriculum",
                iconName = "MenuBook",
                category = "Reading",
                dateUnlocked = "May 10, 2026",
                progressPercentage = 1.0f,
                isUnlocked = true
            ),
            AchievementBadgeDoc(
                id = "badge_2",
                title = "10 Books Completed",
                description = "Read and studied 10 complete Kutub",
                iconName = "LibraryBooks",
                category = "Reading",
                dateUnlocked = "July 02, 2026",
                progressPercentage = 1.0f,
                isUnlocked = true
            ),
            AchievementBadgeDoc(
                id = "badge_3",
                title = "1,000 Pages Read",
                description = "Crossed 1,000 pages milestone in digital reader",
                iconName = "AutoStories",
                category = "Milestones",
                dateUnlocked = "June 20, 2026",
                progressPercentage = 1.0f,
                isUnlocked = true
            ),
            AchievementBadgeDoc(
                id = "badge_4",
                title = "30 Day Learning Streak",
                description = "Studied every day for 30 consecutive days",
                iconName = "LocalFireDepartment",
                category = "Consistency",
                dateUnlocked = "Locked",
                progressPercentage = 0.6f, // 18 / 30
                isUnlocked = false
            ),
            AchievementBadgeDoc(
                id = "badge_5",
                title = "Quiz Champion",
                description = "Scored 100% on 5 consecutive Dars-e-Nizami quizzes",
                iconName = "EmojiEvents",
                category = "Excellence",
                dateUnlocked = "July 12, 2026",
                progressPercentage = 1.0f,
                isUnlocked = true
            ),
            AchievementBadgeDoc(
                id = "badge_6",
                title = "Top Student",
                description = "Ranked #1 in Darja Leaderboard for monthly active reading",
                iconName = "Stars",
                category = "Leadership",
                dateUnlocked = "July 01, 2026",
                progressPercentage = 1.0f,
                isUnlocked = true
            )
        )
    )
    val achievements: StateFlow<List<AchievementBadgeDoc>> = _achievements.asStateFlow()

    private val _certificates = MutableStateFlow(
        listOf(
            StudentCertificateDoc(
                id = "cert_001",
                studentUid = "student_001",
                studentName = "Aalim Student",
                darjaName = "Darja-e-Salisa (3rd Year)",
                courseName = "Dars-e-Nizami Intermediate Qualification",
                issueDate = "July 10, 2026",
                certificateCode = "CERT-BAYTULILM-2026-8849",
                qrVerificationUrl = "https://baytulilmai.app/verify/CERT-BAYTULILM-2026-8849",
                pdfUrl = "darse_nizami_salisa_certificate.pdf"
            ),
            StudentCertificateDoc(
                id = "cert_002",
                studentUid = "student_001",
                studentName = "Aalim Student",
                darjaName = "Arabic Grammar Specialization",
                courseName = "Mastery in Nahw & Sarf Kutub",
                issueDate = "June 01, 2026",
                certificateCode = "CERT-BAYTULILM-2026-3321",
                qrVerificationUrl = "https://baytulilmai.app/verify/CERT-BAYTULILM-2026-3321",
                pdfUrl = "arabic_grammar_specialization.pdf"
            )
        )
    )
    val certificates: StateFlow<List<StudentCertificateDoc>> = _certificates.asStateFlow()

    private val _leaderboards = MutableStateFlow(
        listOf(
            LeaderboardEntryDoc(
                id = "lead_1",
                category = "Top Readers",
                studentUid = "student_001",
                studentName = "Aalim Student",
                studentPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Rabia",
                rank = 1,
                scoreValue = "1,680 Pages",
                metricNumber = 1680
            ),
            LeaderboardEntryDoc(
                id = "lead_2",
                category = "Top Readers",
                studentUid = "student_002",
                studentName = "Tariq Jameel",
                studentPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Khamisa",
                rank = 2,
                scoreValue = "1,520 Pages",
                metricNumber = 1520
            ),
            LeaderboardEntryDoc(
                id = "lead_3",
                category = "Top Readers",
                studentUid = "student_003",
                studentName = "Usman Ghani",
                studentPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Salisa",
                rank = 3,
                scoreValue = "1,390 Pages",
                metricNumber = 1390
            ),
            LeaderboardEntryDoc(
                id = "lead_4",
                category = "Top Quiz Scores",
                studentUid = "student_001",
                studentName = "Aalim Student",
                studentPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Rabia",
                rank = 1,
                scoreValue = "98.5% Avg",
                metricNumber = 98
            ),
            LeaderboardEntryDoc(
                id = "lead_5",
                category = "Top Quiz Scores",
                studentUid = "student_004",
                studentName = "Ahmad Raza",
                studentPhoto = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Sadisa",
                rank = 2,
                scoreValue = "96.2% Avg",
                metricNumber = 96
            ),
            LeaderboardEntryDoc(
                id = "lead_6",
                category = "Most Active Students",
                studentUid = "student_001",
                studentName = "Aalim Student",
                studentPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                darja = "Darja-e-Rabia",
                rank = 1,
                scoreValue = "52.5 Hours",
                metricNumber = 52
            )
        )
    )
    val leaderboards: StateFlow<List<LeaderboardEntryDoc>> = _leaderboards.asStateFlow()

    // CRUD Methods
    fun addNote(bookId: String, bookTitle: String, page: Int, text: String, note: String, category: String, tags: List<String>) {
        val newNote = StudentNoteDoc(
            id = "note_" + System.currentTimeMillis(),
            bookId = bookId,
            bookTitle = bookTitle,
            pageNumber = page,
            highlightedText = text,
            noteText = note,
            colorCategory = category,
            tags = tags,
            timestamp = System.currentTimeMillis()
        )
        _studentNotes.value = listOf(newNote) + _studentNotes.value
    }

    fun deleteNote(noteId: String) {
        _studentNotes.value = _studentNotes.value.filter { it.id != noteId }
    }

    fun addStudyPlan(title: String, desc: String, subject: String, scheduleType: String, timeSlot: String) {
        val newPlan = StudyPlanDoc(
            id = "plan_" + System.currentTimeMillis(),
            title = title,
            description = desc,
            subjectName = subject,
            scheduleType = scheduleType,
            timeSlot = timeSlot,
            isCompleted = false,
            reminderEnabled = true
        )
        _studyPlans.value = _studyPlans.value + newPlan
    }

    fun toggleStudyPlan(planId: String) {
        _studyPlans.value = _studyPlans.value.map {
            if (it.id == planId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun deleteStudyPlan(planId: String) {
        _studyPlans.value = _studyPlans.value.filter { it.id != planId }
    }

    fun submitAssignment(assignmentId: String, assignmentTitle: String, textResponse: String, fileType: String, fileName: String) {
        val newSub = AssignmentSubmissionDoc(
            id = "sub_" + System.currentTimeMillis(),
            assignmentId = assignmentId,
            assignmentTitle = assignmentTitle,
            studentUid = "student_001",
            studentName = "Aalim Student",
            submissionDate = System.currentTimeMillis(),
            textResponse = textResponse,
            fileUrl = fileName,
            fileType = fileType,
            status = "Submitted",
            grade = "Pending Grade",
            teacherFeedback = "Submitted successfully! Teacher review pending."
        )
        _submissions.value = listOf(newSub) + _submissions.value
    }

    // Live Classes
    private val _liveClasses = MutableStateFlow(
        listOf(
            LiveClassDoc(
                id = "live_1",
                title = "Live Fiqh Halaqah: Sharh Al-Hidayah (Kitab al-Buyu')",
                teacherName = "Mufti Muhammad Taqi Usmani",
                subject = "Fiqh",
                darja = "Darja-e-Rabia",
                platform = "Google Meet",
                meetingLink = "https://meet.google.com/baytulilm-fiqh-live",
                scheduledTime = "Today, 05:00 PM - 06:30 PM",
                isLiveNow = true,
                totalAttendees = 184,
                recordingUrl = "https://youtube.com/watch?v=sample_recording_1"
            ),
            LiveClassDoc(
                id = "live_2",
                title = "Interactive Nahw I'rab Workshop: Sharh Ibn 'Aqil",
                teacherName = "Maulana Abdul Salam",
                subject = "Nahw / Syntax",
                darja = "Darja-e-Thania",
                platform = "Zoom",
                meetingLink = "https://zoom.us/j/987654321",
                scheduledTime = "Tomorrow, 10:00 AM - 11:30 AM",
                isLiveNow = false,
                totalAttendees = 95,
                recordingUrl = "https://youtube.com/watch?v=sample_recording_2"
            )
        )
    )
    val liveClasses: StateFlow<List<LiveClassDoc>> = _liveClasses.asStateFlow()

    // Video Courses
    private val _videoCourses = MutableStateFlow(
        listOf(
            VideoCourseDoc(
                id = "course_1",
                title = "Mastery in Nahw Mir & Kafiyah Video Series",
                subject = "Nahw",
                darja = "Darja-e-Thania",
                teacherName = "Maulana Abdul Salam",
                description = "Complete video breakdown of Marfu'at, Mansubat, and Majroorat with interactive parsing diagrams.",
                lessons = listOf(
                    VideoLessonDoc("l1", "Lesson 1: Introduction to Kalam & Ism", 25, "video_1.mp4", true, true),
                    VideoLessonDoc("l2", "Lesson 2: Rules of Fa'il & Na'ib Fa'il", 32, "video_2.mp4", true, true),
                    VideoLessonDoc("l3", "Lesson 3: Mubtada & Khabar Deep Dive", 30, "video_3.mp4", false, false),
                    VideoLessonDoc("l4", "Lesson 4: Inna and Kana Conditions", 28, "video_4.mp4", false, false)
                ),
                progressPercentage = 0.50f,
                lastWatchedLessonTitle = "Lesson 2: Rules of Fa'il & Na'ib Fa'il"
            )
        )
    )
    val videoCourses: StateFlow<List<VideoCourseDoc>> = _videoCourses.asStateFlow()

    // Audio Lectures
    private val _audioLectures = MutableStateFlow(
        listOf(
            AudioLectureDoc("a1", "Dars 42: Kitab al-Buyu' - Valid Contracts in Al-Quduri", "Mufti Taqi Usmani", "45:20", "sample_fiqh_dars.mp3", "Fiqh", true, 3, 120000L),
            AudioLectureDoc("a2", "Dars 18: Nukhbat al-Fikar - Mutawatir vs Ahad Hadith", "Shaykh Muhammad Zayd", "38:15", "sample_hadith_dars.mp3", "Usul Hadith", false, 1, 0L)
        )
    )
    val audioLectures: StateFlow<List<AudioLectureDoc>> = _audioLectures.asStateFlow()

    // Exam Results
    private val _examResults = MutableStateFlow(
        listOf(
            ExamResultDoc(
                id = "res_1",
                studentUid = "student_001",
                studentName = "Aalim Student",
                darja = "Darja-e-Rabia (4th Year)",
                examTerm = "Mid-Term Examinations 2026",
                totalMarks = 500,
                obtainedMarks = 468,
                percentage = 93.6f,
                grade = "A+",
                position = "1st Position in Darja",
                meritRank = 1,
                remarks = "Mumtaz (Outstanding in Fiqh & Nahw)",
                subjectMarks = listOf(
                    SubjectMarkDoc("Sharh Ibn 'Aqil (Nahw)", 100, 96),
                    SubjectMarkDoc("Al-Hidayah (Fiqh)", 100, 94),
                    SubjectMarkDoc("Mishkat al-Masabih (Hadith)", 100, 95),
                    SubjectMarkDoc("Nukhbat al-Fikar (Usul)", 100, 92),
                    SubjectMarkDoc("Tafsir Jalalayn (Tafsir)", 100, 91)
                )
            )
        )
    )
    val examResults: StateFlow<List<ExamResultDoc>> = _examResults.asStateFlow()

    // Forum Posts
    private val _forumPosts = MutableStateFlow(
        listOf(
            ForumPostDoc(
                id = "post_1",
                title = "Difference in Niyyah for Wudu between Hanafi & Shafi'i Schools?",
                question = "Assalamu Alaikum Ustadh. Can you explain why Niyyah is Mustahabb in Hanafi Fiqh but Fard in Shafi'i Fiqh during Wudu?",
                authorName = "Aalim Student",
                authorRole = "Student",
                subject = "Fiqh & Usul",
                darja = "Darja-e-Salisa",
                likesCount = 38,
                isLiked = true,
                isPinned = true,
                comments = listOf(
                    ForumCommentDoc("c1", "Maulana Abdul Hafeez", "Teacher / Ustadh", "In Hanafi Fiqh, the Quranic verse (5:6) specifies 4 obligations for Wudu. Addition of Niyyah via Ahad Hadith cannot alter Quranic Fard, hence it is Sunnah Mu'akkadah / Mustahabb.", "2 hours ago", true)
                )
            )
        )
    )
    val forumPosts: StateFlow<List<ForumPostDoc>> = _forumPosts.asStateFlow()

    fun addForumPost(title: String, question: String, subject: String) {
        val newPost = ForumPostDoc(
            id = "post_" + System.currentTimeMillis(),
            title = title,
            question = question,
            authorName = "Aalim Student",
            authorRole = "Student",
            subject = subject,
            darja = "Darja-e-Rabia",
            likesCount = 1,
            isLiked = false,
            isPinned = false,
            comments = emptyList()
        )
        _forumPosts.value = listOf(newPost) + _forumPosts.value
    }

    fun addForumReply(postId: String, text: String) {
        _forumPosts.value = _forumPosts.value.map { post ->
            if (post.id == postId) {
                val newComment = ForumCommentDoc("c_" + System.currentTimeMillis(), "Aalim Student", "Student", text, "Just now", false)
                post.copy(comments = post.comments + newComment)
            } else post
        }
    }

    // Direct Messaging
    private val _directMessages = MutableStateFlow(
        listOf(
            DirectMessageDoc("m1", "Mufti Muhammad Taqi", "Teacher", "Aalim Student", "Assalamu Alaikum. Excellent solution on the Sharh Ibn Aqil parsing task!", "PDF", "irab_solution.pdf", "10:15 AM", false),
            DirectMessageDoc("m2", "Aalim Student", "Student", "Mufti Muhammad Taqi", "Wa Alaikum Assalam Ustadh! JazaakAllah Khair for your feedback.", "None", "", "10:18 AM", true)
        )
    )
    val directMessages: StateFlow<List<DirectMessageDoc>> = _directMessages.asStateFlow()

    fun sendMessage(receiverName: String, text: String, mediaType: String = "None", mediaUrl: String = "") {
        val newMsg = DirectMessageDoc(
            id = "m_" + System.currentTimeMillis(),
            senderName = "Aalim Student",
            senderRole = "Student",
            receiverName = receiverName,
            messageText = text,
            mediaType = mediaType,
            mediaUrl = mediaUrl,
            timestamp = "Just now",
            isMeSender = true
        )
        _directMessages.value = _directMessages.value + newMsg
    }

    // Parent Dashboard
    private val _parentOverview = MutableStateFlow(ParentChildOverview())
    val parentOverview: StateFlow<ParentChildOverview> = _parentOverview.asStateFlow()

    // Gamification
    private val _gamificationProfile = MutableStateFlow(GamificationProfile())
    val gamificationProfile: StateFlow<GamificationProfile> = _gamificationProfile.asStateFlow()

    fun claimDailyReward() {
        val current = _gamificationProfile.value
        if (!current.dailyRewardClaimed) {
            _gamificationProfile.value = current.copy(
                coins = current.coins + 50,
                totalXp = current.totalXp + 100,
                dailyRewardClaimed = true
            )
        }
    }
}

