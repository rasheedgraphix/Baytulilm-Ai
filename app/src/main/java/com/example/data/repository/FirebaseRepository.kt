package com.example.data.repository

import android.util.Log
import com.example.data.model.AnnouncementDoc
import com.example.data.model.BookDoc
import com.example.data.model.BookmarkDoc
import com.example.data.model.CertificateDoc
import com.example.data.model.DailyAyahDoc
import com.example.data.model.DailyHadithDoc
import com.example.data.model.DarjaDoc
import com.example.data.model.DownloadDoc
import com.example.data.model.McqQuestion
import com.example.data.model.NotificationDoc
import com.example.data.model.QuizDoc
import com.example.data.model.QuizResultDoc
import com.example.data.model.ReadingHistoryDoc
import com.example.data.model.RemoteConfigSettings
import com.example.data.model.SubjectDoc
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirebaseRepository {

    private val tag = "FirebaseRepository"

    private val db: FirebaseFirestore?
        get() = runCatching { FirebaseFirestore.getInstance() }.getOrNull()

    private val auth: FirebaseAuth?
        get() = runCatching { FirebaseAuth.getInstance() }.getOrNull()

    private val listeners = mutableListOf<ListenerRegistration>()

    // User Profile State
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Remote Config State
    private val _remoteConfig = MutableStateFlow(
        RemoteConfigSettings(
            latestVersion = "1.0.0",
            maintenanceMode = false,
            importantMessage = "Bismillah! Welcome to Baytul Ilm AI Digital Hub. All Dars-e-Nizami Kutub, Shurooh & Quizzes are synchronized in real-time."
        )
    )
    val remoteConfig: StateFlow<RemoteConfigSettings> = _remoteConfig.asStateFlow()

    // Announcements
    private val _announcements = MutableStateFlow<List<AnnouncementDoc>>(initialAnnouncements())
    val announcements: StateFlow<List<AnnouncementDoc>> = _announcements.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationDoc>>(initialNotifications())
    val notifications: StateFlow<List<NotificationDoc>> = _notifications.asStateFlow()

    // Darjat Master List
    private val _darjatStateList = MutableStateFlow<List<DarjaDoc>>(initialDarjatList())
    val darjatListStream: StateFlow<List<DarjaDoc>> = _darjatStateList.asStateFlow()
    val darjatList: List<DarjaDoc> get() = _darjatStateList.value

    // Subjects Master List
    val subjectsList = listOf(
        SubjectDoc("nahw", "Nahw", "النحو", "Grammar", 25),
        SubjectDoc("sarf", "Sarf", "الصرف", "Etymology", 18),
        SubjectDoc("fiqh", "Fiqh", "الفقه", "Jurisprudence", 45),
        SubjectDoc("usul_fiqh", "Usul Fiqh", "أصول الفقه", "Principles of Fiqh", 30),
        SubjectDoc("hadith", "Hadith", "الحديث", "Traditions", 60),
        SubjectDoc("tafseer", "Tafseer", "التفسير", "Exegesis", 40),
        SubjectDoc("balagha", "Balagha", "البلاغة", "Rhetoric", 15),
        SubjectDoc("mantiq", "Mantiq", "المنطق", "Logic", 12),
        SubjectDoc("falsafa", "Falsafa", "الفلسفة", "Philosophy", 10),
        SubjectDoc("aqeedah", "Aqeedah", "العقيدة", "Theology", 20),
        SubjectDoc("arabic_lit", "Arabic Literature", "الأدب العربي", "Literature", 22),
        SubjectDoc("insha", "Insha", "الإنشاء", "Composition", 15),
        SubjectDoc("tajweed", "Tajweed", "التجويد", "Phonetics", 10),
        SubjectDoc("history", "History", "التاريخ الإسلامي", "Islamic History", 18)
    )

    // Books Database Collection
    private val _booksDatabase = MutableStateFlow<List<BookDoc>>(initialBooksList())
    val booksDatabase: StateFlow<List<BookDoc>> = _booksDatabase.asStateFlow()

    // Quizzes Collection
    private val _quizzesList = MutableStateFlow<List<QuizDoc>>(initialQuizzesList())
    val quizzesList: StateFlow<List<QuizDoc>> = _quizzesList.asStateFlow()

    // Quiz Results Storage
    private val _quizResults = MutableStateFlow<List<QuizResultDoc>>(emptyList())
    val quizResults: StateFlow<List<QuizResultDoc>> = _quizResults.asStateFlow()

    init {
        setupFirestoreRealtimeListeners()
    }

    private fun setupFirestoreRealtimeListeners() {
        val firestore = db ?: return

        // 1. Listen to 'books' collection
        runCatching {
            val bookListener = firestore.collection("books").addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(tag, "Books snapshot listener error", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val books = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(BookDoc::class.java) }.getOrNull()
                    }
                    if (books.isNotEmpty()) {
                        _booksDatabase.value = books
                    }
                } else if (snapshot != null && snapshot.isEmpty) {
                    seedBooksToFirestore()
                }
            }
            listeners.add(bookListener)
        }

        // 2. Listen to 'quizzes' collection
        runCatching {
            val quizListener = firestore.collection("quizzes").addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    val quizzes = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(QuizDoc::class.java) }.getOrNull()
                    }
                    if (quizzes.isNotEmpty()) {
                        _quizzesList.value = quizzes
                    }
                } else if (snapshot != null && snapshot.isEmpty) {
                    seedQuizzesToFirestore()
                }
            }
            listeners.add(quizListener)
        }

        // 3. Listen to 'announcements' collection
        runCatching {
            val annListener = firestore.collection("announcements").addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    val anns = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(AnnouncementDoc::class.java) }.getOrNull()
                    }
                    if (anns.isNotEmpty()) {
                        _announcements.value = anns
                    }
                }
            }
            listeners.add(annListener)
        }

        // 4. Listen to 'notifications' collection
        runCatching {
            val notifListener = firestore.collection("notifications").addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    val notifs = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(NotificationDoc::class.java) }.getOrNull()
                    }
                    if (notifs.isNotEmpty()) {
                        _notifications.value = notifs
                    }
                }
            }
            listeners.add(notifListener)
        }

        // 5. Listen to 'quiz_results' collection
        runCatching {
            val resultListener = firestore.collection("quiz_results").addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    val results = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(QuizResultDoc::class.java) }.getOrNull()
                    }
                    if (results.isNotEmpty()) {
                        _quizResults.value = results
                    }
                }
            }
            listeners.add(resultListener)
        }
    }

    private fun seedBooksToFirestore() {
        val firestore = db ?: return
        val cleanBooks = initialBooksList()
        cleanBooks.forEach { book ->
            firestore.collection("books").document(book.id).set(book, SetOptions.merge())
        }
    }

    /**
     * Deduplicates library books from all classes/levels, keeping only 1 copy of each book,
     * and syncs the cleaned library securely to Firebase database & storage.
     */
    fun cleanAndSyncLibraryToFirebase(): Int {
        val currentBooks = if (_booksDatabase.value.isNotEmpty()) _booksDatabase.value else initialBooksList()

        val seenIds = mutableSetOf<String>()
        val seenUrls = mutableSetOf<String>()
        val seenTitleDarja = mutableSetOf<Pair<String, String>>()

        val deduplicated = mutableListOf<BookDoc>()
        var duplicateCount = 0

        fun normalizeStr(s: String): String {
            val cleaned = s.lowercase()
                .replace(Regex("[\\u064B-\\u0652\\u0670]"), "")
                .replace(Regex("[\\u0622\\u0623\\u0625]"), "\u0627")
                .replace("ة", "ه")
                .replace("ى", "ي")
                .replace(Regex("\\(.*\\)"), "")
                .trim()
            return cleaned.split("\\s+".toRegex()).joinToString(" ")
        }

        for (book in currentBooks) {
            val normTitle = normalizeStr(book.title)
            val normDarja = normalizeStr(book.darja)
            val cleanUrl = book.pdfUrl.trim()
            val tdKey = Pair(normTitle, normDarja)

            val isDuplicate = seenIds.contains(book.id) ||
                    (cleanUrl.isNotEmpty() && !cleanUrl.contains("tracemonkey") && seenUrls.contains(cleanUrl)) ||
                    (normTitle.length > 3 && seenTitleDarja.contains(tdKey))

            if (isDuplicate) {
                duplicateCount++
                db?.collection("books")?.document(book.id)?.delete()
            } else {
                seenIds.add(book.id)
                if (cleanUrl.isNotEmpty() && !cleanUrl.contains("tracemonkey")) {
                    seenUrls.add(cleanUrl)
                }
                if (normTitle.length > 3) {
                    seenTitleDarja.add(tdKey)
                }
                deduplicated.add(book)
            }
        }

        _booksDatabase.value = deduplicated

        val firestore = db
        if (firestore != null) {
            deduplicated.forEach { book ->
                firestore.collection("books").document(book.id).set(book, SetOptions.merge())
            }
        }

        return duplicateCount
    }

    private fun seedQuizzesToFirestore() {
        val firestore = db ?: return
        initialQuizzesList().forEach { quiz ->
            firestore.collection("quizzes").document(quiz.id).set(quiz, SetOptions.merge())
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
        db?.collection("users")?.document(profile.uid)?.set(profile, SetOptions.merge())
    }

    fun getStoragePdfPath(darjaCode: String, filename: String): String {
        val folder = when (darjaCode.lowercase()) {
            "ula" -> "ula"
            "sania" -> "sania"
            "salisa" -> "salisa"
            "rabia" -> "rabia"
            "khamsa", "khamisa" -> "khamsa"
            "sadisa" -> "sadisa"
            "sabia" -> "sabia"
            "dora" -> "dora"
            else -> "general"
        }
        return "gs://baytulilm-ai.appspot.com/books/$folder/$filename"
    }

    fun searchBooks(query: String, darjaFilter: String? = null, subjectFilter: String? = null): List<BookDoc> {
        val q = query.trim().lowercase()
        return _booksDatabase.value.filter { book ->
            val matchesQuery = q.isEmpty() ||
                    book.title.lowercase().contains(q) ||
                    book.titleArabic.contains(q) ||
                    book.author.lowercase().contains(q) ||
                    book.darja.lowercase().contains(q) ||
                    book.subject.lowercase().contains(q) ||
                    book.language.lowercase().contains(q)

            val matchesDarja = darjaFilter.isNullOrBlank() || book.darja.equals(darjaFilter, ignoreCase = true)
            val matchesSubject = subjectFilter.isNullOrBlank() || book.subject.equals(subjectFilter, ignoreCase = true)

            matchesQuery && matchesDarja && matchesSubject
        }
    }

    // Real Firestore Book Actions
    fun addNewBook(book: BookDoc, userRole: String): Boolean {
        if (userRole != UserRole.ADMIN && userRole != UserRole.TEACHER) return false
        _booksDatabase.value = _booksDatabase.value + book
        db?.collection("books")?.document(book.id)?.set(book, SetOptions.merge())

        sendNotificationOrAnnouncement(
            title = "New Book Uploaded: ${book.title}",
            message = "${book.title} (${book.darja}) is now ready for students.",
            type = "New Books"
        )
        return true
    }

    fun editBook(book: BookDoc): Boolean {
        _booksDatabase.value = _booksDatabase.value.map { if (it.id == book.id) book else it }
        db?.collection("books")?.document(book.id)?.set(book, SetOptions.merge())
        return true
    }

    fun deleteBook(bookId: String): Boolean {
        _booksDatabase.value = _booksDatabase.value.filter { it.id != bookId }
        db?.collection("books")?.document(bookId)?.delete()
        return true
    }

    // Real Firestore Quiz Actions
    fun addNewQuiz(quiz: QuizDoc, userRole: String): Boolean {
        if (userRole != UserRole.ADMIN && userRole != UserRole.TEACHER) return false
        _quizzesList.value = _quizzesList.value + quiz
        db?.collection("quizzes")?.document(quiz.id)?.set(quiz, SetOptions.merge())

        sendNotificationOrAnnouncement(
            title = "New Quiz: ${quiz.title}",
            message = "Test your knowledge in ${quiz.subject} (${quiz.darja}).",
            type = "Quiz Alert"
        )
        return true
    }

    fun editQuiz(quiz: QuizDoc): Boolean {
        _quizzesList.value = _quizzesList.value.map { if (it.id == quiz.id) quiz else it }
        db?.collection("quizzes")?.document(quiz.id)?.set(quiz, SetOptions.merge())
        return true
    }

    fun deleteQuiz(quizId: String): Boolean {
        _quizzesList.value = _quizzesList.value.filter { it.id != quizId }
        db?.collection("quizzes")?.document(quizId)?.delete()
        return true
    }

    fun sendNotificationOrAnnouncement(title: String, message: String, type: String) {
        val annId = "ann_" + System.currentTimeMillis()
        val newAnn = AnnouncementDoc(
            id = annId,
            title = title,
            message = message,
            author = "Baytul Ilm Faculty",
            type = type
        )
        _announcements.value = listOf(newAnn) + _announcements.value
        db?.collection("announcements")?.document(annId)?.set(newAnn)

        val notifId = "notif_" + System.currentTimeMillis()
        val newNotif = NotificationDoc(
            id = notifId,
            title = title,
            body = message
        )
        _notifications.value = listOf(newNotif) + _notifications.value
        db?.collection("notifications")?.document(notifId)?.set(newNotif)
    }

    fun submitQuizResult(result: QuizResultDoc) {
        _quizResults.value = listOf(result) + _quizResults.value
        db?.collection("quiz_results")?.document(result.id)?.set(result)
        if (result.studentUid.isNotBlank()) {
            db?.collection("users")?.document(result.studentUid)
                ?.collection("quiz_scores")?.document(result.id)?.set(result)
        }
    }

    fun saveBookmarkToFirestore(bookmark: BookmarkDoc) {
        db?.collection("user_bookmarks")?.document(bookmark.id)?.set(bookmark)
        if (bookmark.userId.isNotBlank()) {
            db?.collection("users")?.document(bookmark.userId)
                ?.collection("bookmarks")?.document(bookmark.id)?.set(bookmark)
        }
    }

    fun saveReadingProgressToFirestore(progress: ReadingHistoryDoc) {
        db?.collection("reading_progress")?.document(progress.id)?.set(progress)
        if (progress.userId.isNotBlank()) {
            db?.collection("users")?.document(progress.userId)
                ?.collection("reading_history")?.document(progress.id)?.set(progress)
        }
    }

    fun updateRemoteConfig(message: String, maintenance: Boolean) {
        _remoteConfig.value = _remoteConfig.value.copy(
            importantMessage = message,
            maintenanceMode = maintenance
        )
        db?.collection("settings")?.document("remote_config")?.set(_remoteConfig.value)
    }

    fun destroy() {
        listeners.forEach { it.remove() }
        listeners.clear()
    }

    private fun initialBooksList(): List<BookDoc> {
        val seedDocs = InitialDataSeed.sampleBooks.map { entity ->
            BookDoc(
                id = entity.id,
                title = entity.title,
                titleArabic = entity.title,
                author = entity.author,
                description = entity.description,
                darja = if (entity.darja == "Darja Ula") "Darja-e-Ula" else entity.darja,
                subject = entity.subject,
                language = entity.language,
                publisher = "Maktaba Al-Bushra",
                coverImage = "img_hero_banner",
                pdfUrl = entity.pdfUrl,
                pages = entity.pageCount,
                views = 1250,
                downloads = 340,
                rating = entity.rating,
                isMainBook = entity.type == "Main Book",
                isSharh = entity.type == "Shurooh",
                isTranslation = entity.type == "Translation"
            )
        }

        val seenIds = mutableSetOf<String>()
        val seenUrls = mutableSetOf<String>()
        val seenTitleDarja = mutableSetOf<Pair<String, String>>()

        fun normalizeStr(s: String): String {
            val cleaned = s.lowercase()
                .replace(Regex("[\\u064B-\\u0652\\u0670]"), "")
                .replace(Regex("[\\u0622\\u0623\\u0625]"), "\u0627")
                .replace("ة", "ه")
                .replace("ى", "ي")
                .replace(Regex("\\(.*\\)"), "")
                .trim()
            return cleaned.split("\\s+".toRegex()).joinToString(" ")
        }

        val uniqueBooks = mutableListOf<BookDoc>()
        for (book in seedDocs) {
            val normTitle = normalizeStr(book.title)
            val normDarja = normalizeStr(book.darja)
            val cleanUrl = book.pdfUrl.trim()
            val tdKey = Pair(normTitle, normDarja)

            val isDuplicate = seenIds.contains(book.id) ||
                    (cleanUrl.isNotEmpty() && seenUrls.contains(cleanUrl)) ||
                    (normTitle.length > 3 && seenTitleDarja.contains(tdKey))

            if (!isDuplicate) {
                seenIds.add(book.id)
                if (cleanUrl.isNotEmpty()) seenUrls.add(cleanUrl)
                if (normTitle.length > 3) seenTitleDarja.add(tdKey)
                uniqueBooks.add(book)
            }
        }
        return uniqueBooks
    }

    private fun initialQuizzesList() = listOf(
        QuizDoc(
            id = "q_101",
            title = "Nahw & Sentence Structure Quiz",
            chapter = "Chapter 1: Al-Kalam & Marfoo'at",
            subject = "Nahw",
            darja = "Darja-e-Ula",
            mcqs = listOf(
                McqQuestion(1, "What is the definition of Al-Kalam in Arabic Grammar?", listOf("A single word", "A compound phrase that gives complete benefit", "A letter", "A verb without subject"), 1, "Al-Kalam is a compound statement providing complete understanding to the listener.", 10),
                McqQuestion(2, "How many signs of Ism (Noun) are mentioned in Nahw?", listOf("3", "5", "More than 7", "None"), 2, "Ism has signs like Alif-Lam, Tanween, Harf-Jar, Hafadh, etc.", 10),
                McqQuestion(3, "Which of the following is Marfoo' (Nominated)?", listOf("Fa'il (Subject)", "Maf'ool (Object)", "Mudaf Ilaihi", "Majroor"), 0, "Fa'il is always in the state of Raf' (Nominated).", 10)
            ),
            difficulty = "Medium",
            marks = 30
        ),
        QuizDoc(
            id = "q_102",
            title = "Fiqh: Taharat & Wudu Essentials",
            chapter = "Kitab ut-Taharah",
            subject = "Fiqh",
            darja = "Darja-e-Ula",
            mcqs = listOf(
                McqQuestion(1, "How many obligatory acts (Fara'id) are there in Wudu according to Hanafi Fiqh?", listOf("3", "4", "6", "7"), 1, "The 4 Farz acts are: washing face, washing arms to elbows, wiping 1/4th head, washing feet to ankles.", 10),
                McqQuestion(2, "What invalidates Tayammum?", listOf("Anything that invalidates Wudu or availability of water", "Sleeping on a chair", "Laughter outside prayer", "Reciting Quran"), 0, "Tayammum breaks when water becomes accessible or Wudu breaks.", 10)
            ),
            difficulty = "Easy",
            marks = 20
        )
    )

    private fun initialAnnouncements() = listOf(
        AnnouncementDoc(
            id = "ann_01",
            title = "New Sharh Added for Darja-e-Sadisa",
            message = "Sharh Hidayah Volume 3 & 4 with Urdu translation and explanation is now available.",
            author = "Admin Scholar",
            type = "New Books"
        ),
        AnnouncementDoc(
            id = "ann_02",
            title = "Weekly Dars-e-Nizami Online Quiz",
            message = "Fiqh & Usul Fiqh quiz competition for all students starting this Friday.",
            author = "Teacher Mufti",
            type = "Quiz Alert"
        )
    )

    private fun initialNotifications() = listOf(
        NotificationDoc(
            id = "notif_01",
            title = "Daily Hadith Reminder",
            body = "The best among you are those who learn the Qur'an and teach it.",
            isRead = false
        ),
        NotificationDoc(
            id = "notif_02",
            title = "Quiz Result Published",
            body = "Your score for Nahw & Sarf Quiz 1 has been updated in your profile.",
            isRead = true
        )
    )

    private fun initialDarjatList() = listOf(
        DarjaDoc("darja_ula", "Darja Ula", "اولی", "درجہ اولیٰ", "First Year Dars-e-Nizami (Foundation Arabic Syntax, Morphology, Tajweed & Fiqh)", "پہلا سال درسِ نظامی (بنیادی عربی نحو، صرف، تجوید اور فقہ)", "https://images.unsplash.com/photo-1585036156171-384164a8c675?auto=format&fit=crop&w=400&q=80", 94, 4, 1, 0.25f),
        DarjaDoc("darja_sania", "Darja Sania", "ثانیہ", "درجہ ثانیہ", "Second Year Dars-e-Nizami (Mukhtasar al-Quduri, Mantiq & Sarf In-Depth)", "دوسرا سال درسِ نظامی (مختصر القدوری، منطق اور تفصیلی صرف)", "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=400&q=80", 7, 5, 2, 0.15f),
        DarjaDoc("darja_salisa", "Darja Salisa", "ثالثہ", "درجہ ثالثہ", "Third Year Dars-e-Nizami (Kanz al-Daqaiq, Usul al-Shashi & Kafiyah Syntax)", "تیسرا سال درسِ نظامی (کنز الدقائق، اصول الشاشی اور کافیہ نحو)", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=400&q=80", 9, 6, 3, 0.10f),
        DarjaDoc("darja_rabia", "Darja Rabia", "رابعہ", "درجہ رابعہ", "Fourth Year Dars-e-Nizami (Riyad as-Salihin, Sharh Wiqayah & Noor al-Anwar)", "چوتھا سال درسِ نظامی (ریاض الصالحین، شرح وقایہ اور نور الانوار)", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80", 8, 5, 4, 0.05f),
        DarjaDoc("darja_khamisa", "Darja Khamisa", "خامسہ", "درجہ خامسہ", "Fifth Year Dars-e-Nizami (Sharh Aqeedah Tahawiyyah, Al-Hidayah, Aasaar us-Sunan & Balagha)", "پانچواں سال درسِ نظامی (شرح العقیدہ الطحاویہ، الہدایہ، آثار السنن اور بلاغت)", "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?auto=format&fit=crop&w=400&q=80", 19, 7, 5, 0.0f),
        DarjaDoc("darja_sadisa", "Class 6th", "سادسہ", "درجہ سادسہ", "Sixth Year Dars-e-Nizami (Al-Hidayah, Tafseer Jalalain, Siraji, Aqeedah & Usul Fiqh)", "چھٹا سال درسِ نظامی (الہدایہ، تفسیر جلالین، سراجی میراث، عقائد اور اصول فقہ)", "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=400&q=80", 58, 7, 6, 0.0f),
        DarjaDoc("darja_sabia", "Darja Sabi'a", "سابعہ", "درجہ سابعہ", "Seventh Year Dars-e-Nizami (Tafseer Baizawi, Mishkat al-Masabih & Nukhbat al-Fikar)", "ساتواں سال درسِ نظامی (تفسیر بیضاوی، مشکوۃ المصابیح اور نخبۃ الفکر)", "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?auto=format&fit=crop&w=400&q=80", 18, 7, 7, 0.0f),
        DarjaDoc("dora_hadith", "Dora-e-Hadith", "دورۂ حدیث", "دورۂ حدیث شریف", "Final Master Graduation Year (Sahih Bukhari, Sahih Muslim, Sunan Books & Shurooh)", "آخری سال تکمیلی (صحیح البخاری، صحیح المسلم، صحاح ستہ اور شروحات)", "https://images.unsplash.com/photo-1541963463532-d68292c34b19?auto=format&fit=crop&w=400&q=80", 19, 6, 8, 0.0f)
    )
}
