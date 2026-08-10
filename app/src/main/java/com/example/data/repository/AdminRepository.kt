package com.example.data.repository

import android.util.Log
import com.example.data.model.CertificateDoc
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdminMetricStats(
    val totalUsers: Int = 1450,
    val activeUsers: Int = 980,
    val studentsCount: Int = 1200,
    val teachersCount: Int = 45,
    val adminsCount: Int = 5,
    val totalBooks: Int = 240,
    val totalPdfs: Int = 235,
    val totalSubjects: Int = 14,
    val totalDarjat: Int = 8,
    val totalQuizzes: Int = 38,
    val completedQuizzes: Int = 1890,
    val totalDownloads: Int = 12450,
    val totalBookmarks: Int = 3400,
    val dailyActiveUsers: Int = 410,
    val weeklyActiveUsers: Int = 890,
    val monthlyActiveUsers: Int = 1380,
    val storageUsedGb: Float = 18.4f,
    val storageTotalGb: Float = 50.0f
)

data class ActivityLogDoc(
    val id: String = "",
    val action: String = "",
    val user: String = "",
    val role: String = "",
    val timestamp: String = "",
    val category: String = "" // Book, Quiz, User, Security, Backup
)

data class NoteDoc(
    val id: String = "",
    val title: String = "",
    val subject: String = "Nahw",
    val darja: String = "Darja-e-Ula",
    val author: String = "Mufti Ahmad",
    val pdfUrl: String = "",
    val fileType: String = "PDF", // PDF, Image, Document
    val uploadedAt: Long = System.currentTimeMillis()
)

data class VideoLectureDoc(
    val id: String = "",
    val title: String = "",
    val category: String = "Course Video", // YouTube, Firebase Video, Lecture
    val darja: String = "Darja-e-Sania",
    val subject: String = "Fiqh",
    val url: String = "https://youtube.com/watch?v=sample",
    val durationMinutes: Int = 45,
    val teacherName: String = "Maulana Tariq"
)

data class AudioLectureDoc(
    val id: String = "",
    val title: String = "",
    val type: String = "Lecture Audio", // MP3, Quran Audio, Hadith Audio, Lecture Audio
    val reciterOrTeacher: String = "Qari Mishary",
    val url: String = "https://sample.audio/lecture.mp3",
    val duration: String = "28:15",
    val sizeMb: Float = 14.2f
)

data class SecurityRuleDoc(
    val collectionName: String = "books",
    val readPermission: String = "Public / Authenticated",
    val writePermission: String = "Admin / Teacher",
    val isAppCheckRequired: Boolean = true
)

class AdminRepository {

    private val db: FirebaseFirestore?
        get() = runCatching { FirebaseFirestore.getInstance() }.getOrNull()

    private val listeners = mutableListOf<ListenerRegistration>()

    // Metrics State
    private val _metrics = MutableStateFlow(AdminMetricStats())
    val metrics: StateFlow<AdminMetricStats> = _metrics.asStateFlow()

    // Activity Logs
    private val _activityLogs = MutableStateFlow<List<ActivityLogDoc>>(initialLogs())
    val activityLogs: StateFlow<List<ActivityLogDoc>> = _activityLogs.asStateFlow()

    // Users List
    private val _usersList = MutableStateFlow<List<UserProfile>>(initialUsers())
    val usersList: StateFlow<List<UserProfile>> = _usersList.asStateFlow()

    // Notes List
    private val _notesList = MutableStateFlow<List<NoteDoc>>(initialNotes())
    val notesList: StateFlow<List<NoteDoc>> = _notesList.asStateFlow()

    // Video Lectures
    private val _videoList = MutableStateFlow<List<VideoLectureDoc>>(initialVideos())
    val videoList: StateFlow<List<VideoLectureDoc>> = _videoList.asStateFlow()

    // Audio Lectures
    private val _audioList = MutableStateFlow<List<AudioLectureDoc>>(initialAudios())
    val audioList: StateFlow<List<AudioLectureDoc>> = _audioList.asStateFlow()

    // Certificates List
    private val _certificatesList = MutableStateFlow<List<CertificateDoc>>(initialCertificates())
    val certificatesList: StateFlow<List<CertificateDoc>> = _certificatesList.asStateFlow()

    // Security Rules
    val securityRulesList = listOf(
        SecurityRuleDoc("users", "Owner / Admin", "Owner / Admin", true),
        SecurityRuleDoc("books", "Public Read", "Admin / Teacher Write", true),
        SecurityRuleDoc("pdfs", "Authenticated Students", "Admin Write", true),
        SecurityRuleDoc("quizzes", "Authenticated Students", "Teacher / Admin Write", true),
        SecurityRuleDoc("certificates", "Public Verify / Owner Read", "Admin Write", true)
    )

    init {
        setupFirestoreListeners()
    }

    private fun setupFirestoreListeners() {
        val firestore = db ?: return

        // Users
        runCatching {
            val l = firestore.collection("users").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(UserProfile::class.java) }.getOrNull()
                    }
                    if (users.isNotEmpty()) {
                        _usersList.value = users
                        _metrics.value = _metrics.value.copy(
                            totalUsers = users.size,
                            studentsCount = users.count { it.role == UserRole.STUDENT },
                            teachersCount = users.count { it.role == UserRole.TEACHER },
                            adminsCount = users.count { it.role == UserRole.ADMIN || it.role == UserRole.SUPER_ADMIN }
                        )
                    }
                }
            }
            listeners.add(l)
        }

        // Notes
        runCatching {
            val l = firestore.collection("notes").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val notes = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(NoteDoc::class.java) }.getOrNull()
                    }
                    if (notes.isNotEmpty()) _notesList.value = notes
                }
            }
            listeners.add(l)
        }

        // Video Lectures
        runCatching {
            val l = firestore.collection("video_lectures").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val vids = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(VideoLectureDoc::class.java) }.getOrNull()
                    }
                    if (vids.isNotEmpty()) _videoList.value = vids
                }
            }
            listeners.add(l)
        }

        // Audio Lectures
        runCatching {
            val l = firestore.collection("audio_lectures").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val auds = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(AudioLectureDoc::class.java) }.getOrNull()
                    }
                    if (auds.isNotEmpty()) _audioList.value = auds
                }
            }
            listeners.add(l)
        }

        // Certificates
        runCatching {
            val l = firestore.collection("certificates").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val certs = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(CertificateDoc::class.java) }.getOrNull()
                    }
                    if (certs.isNotEmpty()) _certificatesList.value = certs
                }
            }
            listeners.add(l)
        }

        // Activity Logs
        runCatching {
            val l = firestore.collection("activity_logs").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val logs = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(ActivityLogDoc::class.java) }.getOrNull()
                    }
                    if (logs.isNotEmpty()) _activityLogs.value = logs
                }
            }
            listeners.add(l)
        }
    }

    // User Operations
    fun updateUserRole(userId: String, newRole: String) {
        _usersList.value = _usersList.value.map { u ->
            if (u.uid == userId) u.copy(role = newRole) else u
        }
        db?.collection("users")?.document(userId)?.update("role", newRole)
        addLog("Changed user $userId role to $newRole", "Admin User", UserRole.ADMIN, "User")
    }

    fun toggleBlockUser(userId: String) {
        val target = _usersList.value.find { it.uid == userId } ?: return
        val isBlocked = target.country == "BLOCKED"
        val newCountry = if (isBlocked) "Pakistan" else "BLOCKED"
        _usersList.value = _usersList.value.map { u ->
            if (u.uid == userId) u.copy(country = newCountry) else u
        }
        db?.collection("users")?.document(userId)?.update("country", newCountry)
        addLog("Toggled block status for user $userId", "Admin User", UserRole.ADMIN, "User")
    }

    fun deleteUser(userId: String) {
        _usersList.value = _usersList.value.filter { it.uid != userId }
        db?.collection("users")?.document(userId)?.delete()
        addLog("Deleted user account $userId", "Admin User", UserRole.ADMIN, "User")
    }

    // Note Operations
    fun addNote(note: NoteDoc) {
        _notesList.value = listOf(note) + _notesList.value
        db?.collection("notes")?.document(note.id)?.set(note, SetOptions.merge())
        addLog("Uploaded note: ${note.title}", "Teacher", UserRole.TEACHER, "Book")
    }

    fun deleteNote(noteId: String) {
        _notesList.value = _notesList.value.filter { it.id != noteId }
        db?.collection("notes")?.document(noteId)?.delete()
    }

    // Video Operations
    fun addVideo(video: VideoLectureDoc) {
        _videoList.value = listOf(video) + _videoList.value
        db?.collection("video_lectures")?.document(video.id)?.set(video, SetOptions.merge())
        addLog("Added video lecture: ${video.title}", "Faculty", UserRole.TEACHER, "Book")
    }

    fun deleteVideo(videoId: String) {
        _videoList.value = _videoList.value.filter { it.id != videoId }
        db?.collection("video_lectures")?.document(videoId)?.delete()
    }

    // Audio Operations
    fun addAudio(audio: AudioLectureDoc) {
        _audioList.value = listOf(audio) + _audioList.value
        db?.collection("audio_lectures")?.document(audio.id)?.set(audio, SetOptions.merge())
        addLog("Added audio file: ${audio.title}", "Faculty", UserRole.TEACHER, "Book")
    }

    fun deleteAudio(audioId: String) {
        _audioList.value = _audioList.value.filter { it.id != audioId }
        db?.collection("audio_lectures")?.document(audioId)?.delete()
    }

    // Certificate Actions
    fun generateCertificate(userName: String, courseName: String) {
        val certId = "cert_" + System.currentTimeMillis()
        val newCert = CertificateDoc(
            id = certId,
            userId = "u_" + (100..999).random(),
            userName = userName,
            courseName = courseName,
            issuedAt = System.currentTimeMillis(),
            certificateUrl = "https://rasheed.edu/cert/generated_${System.currentTimeMillis()}.pdf"
        )
        _certificatesList.value = listOf(newCert) + _certificatesList.value
        db?.collection("certificates")?.document(certId)?.set(newCert)
        addLog("Issued Certificate for $userName", "Admin", UserRole.ADMIN, "User")
    }

    // Logging helper
    fun addLog(action: String, user: String, role: String, category: String) {
        val logId = "log_" + System.currentTimeMillis()
        val newLog = ActivityLogDoc(
            id = logId,
            action = action,
            user = user,
            role = role,
            timestamp = "Just now",
            category = category
        )
        _activityLogs.value = listOf(newLog) + _activityLogs.value
        db?.collection("activity_logs")?.document(logId)?.set(newLog)
    }

    fun destroy() {
        listeners.forEach { it.remove() }
        listeners.clear()
    }

    private fun initialLogs() = listOf(
        ActivityLogDoc("log_1", "Uploaded Sharh Hidayah Vol 3", "Mufti Usmani", UserRole.TEACHER, "10 mins ago", "Book"),
        ActivityLogDoc("log_2", "Created Weekly Fiqh Quiz", "Admin Scholar", UserRole.ADMIN, "1 hour ago", "Quiz"),
        ActivityLogDoc("log_3", "Approved Certificate #CERT-104", "Super Admin", UserRole.ADMIN, "2 hours ago", "User"),
        ActivityLogDoc("log_4", "System Firestore Backup Completed", "Auto System", "System", "6 hours ago", "Backup"),
        ActivityLogDoc("log_5", "Updated Remote Config Notice", "Admin Scholar", UserRole.ADMIN, "1 day ago", "Settings")
    )

    private fun initialUsers() = listOf(
        UserProfile("u_01", "Hafiz Nouman", "nouman@rasheed.edu", "https://images.unsplash.com/photo-1534528741775-53994a69daeb", "+92 300 1234567", UserRole.ADMIN, "Urdu", "Pakistan"),
        UserProfile("u_02", "Mufti Muhammad", "mufti@rasheed.edu", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", "+92 321 9876543", UserRole.TEACHER, "Arabic", "Pakistan"),
        UserProfile("u_03", "Abdullah Student", "abdullah@student.com", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e", "+92 333 4567890", UserRole.STUDENT, "Urdu", "Pakistan"),
        UserProfile("u_04", "Tariq Jameel", "tariq@student.com", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce", "+92 312 3334455", UserRole.STUDENT, "English", "UK"),
        UserProfile("u_05", "Usman Learner", "usman@student.com", "", "+92 300 0000000", UserRole.STUDENT, "Urdu", "Pakistan")
    )

    private fun initialNotes() = listOf(
        NoteDoc("n_01", "Nahw Mir Summary Charts", "Nahw", "Darja-e-Ula", "Mufti Bilal", "gs://rasheed/notes/nahw_charts.pdf", "PDF"),
        NoteDoc("n_02", "Al-Kuduri Ibadaat Key Points", "Fiqh", "Darja-e-Sania", "Maulana Asad", "gs://rasheed/notes/kuduri_summary.png", "Image")
    )

    private fun initialVideos() = listOf(
        VideoLectureDoc("v_01", "Hidayat un Nahw Lecture 1 - Al-Kalam", "YouTube", "Darja-e-Sania", "Nahw", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", 42, "Mufti Usmani"),
        VideoLectureDoc("v_02", "Sharh Aqaid Usul Fiqh Intro", "Course Video", "Darja-e-Rabia", "Usul Fiqh", "https://rasheed-media.com/videos/v2.mp4", 55, "Maulana Tariq")
    )

    private fun initialAudios() = listOf(
        AudioLectureDoc("a_01", "Mishkat Sharif Dars - Kitab ul Iman", "Lecture Audio", "Maulana Rumi", "https://rasheed-media.com/audio/mishkat_01.mp3", "45:20", 22.5f),
        AudioLectureDoc("a_02", "Surah Yaseen Recitation & Tajweed Rules", "Quran Audio", "Qari Mishary", "https://rasheed-media.com/audio/yaseen.mp3", "18:40", 12.0f)
    )

    private fun initialCertificates() = listOf(
        CertificateDoc("cert_001", "u_03", "Abdullah Student", "Dars-e-Nizami Primary Examination", System.currentTimeMillis(), "https://rasheed.edu/cert/cert_001.pdf"),
        CertificateDoc("cert_002", "u_04", "Tariq Jameel", "Nahw & Sarf Master Certificate", System.currentTimeMillis() - 86400000L * 5, "https://rasheed.edu/cert/cert_002.pdf")
    )
}
