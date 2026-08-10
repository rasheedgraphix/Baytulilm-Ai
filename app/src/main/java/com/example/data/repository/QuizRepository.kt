package com.example.data.repository

import android.util.Log
import com.example.data.model.ComprehensiveQuizQuestion
import com.example.data.model.CertificateInfo
import com.example.data.model.ChapterProgress
import com.example.data.model.DifficultyLevel
import com.example.data.model.LeaderboardEntry
import com.example.data.model.QuizBadge
import com.example.data.model.QuizProgressStats
import com.example.data.model.QuizResultRecord
import com.example.data.model.QuizSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class ChapterResultOutcome(
    val percentage: Float,
    val isPassed: Boolean, // score >= 90%
    val isNextChapterUnlocked: Boolean,
    val unlockedChapterNumber: Int?
)

class QuizRepository {

    private val geminiRepository = GeminiRepository()

    private val db: FirebaseFirestore?
        get() = runCatching { FirebaseFirestore.getInstance() }.getOrNull()

    private val auth: FirebaseAuth?
        get() = runCatching { FirebaseAuth.getInstance() }.getOrNull()

    private val listeners = mutableListOf<ListenerRegistration>()

    private val _chapterProgressMap = MutableStateFlow<Map<String, List<ChapterProgress>>>(
        initChapterProgressMap()
    )
    val chapterProgressMap: StateFlow<Map<String, List<ChapterProgress>>> = _chapterProgressMap.asStateFlow()

    private val _quizzesFlow = MutableStateFlow<List<QuizSet>>(initialQuizzes())
    val quizzesFlow: StateFlow<List<QuizSet>> = _quizzesFlow.asStateFlow()

    private val _resultsFlow = MutableStateFlow<List<QuizResultRecord>>(initialResults())
    val resultsFlow: StateFlow<List<QuizResultRecord>> = _resultsFlow.asStateFlow()

    private val _bookmarksFlow = MutableStateFlow<List<ComprehensiveQuizQuestion>>(emptyList())
    val bookmarksFlow: StateFlow<List<ComprehensiveQuizQuestion>> = _bookmarksFlow.asStateFlow()

    private val _favoritesFlow = MutableStateFlow<List<ComprehensiveQuizQuestion>>(emptyList())
    val favoritesFlow: StateFlow<List<ComprehensiveQuizQuestion>> = _favoritesFlow.asStateFlow()

    init {
        setupFirestoreListeners()
    }

    private fun setupFirestoreListeners() {
        val firestore = db ?: return

        // 1. Listen to 'quizzes' collection
        runCatching {
            val l = firestore.collection("quizzes").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(QuizSet::class.java) }.getOrNull()
                    }
                    if (list.isNotEmpty()) {
                        _quizzesFlow.value = list
                    }
                }
            }
            listeners.add(l)
        }

        // 2. Listen to 'quiz_results' collection
        runCatching {
            val l = firestore.collection("quiz_results").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val results = snapshot.documents.mapNotNull { doc ->
                        runCatching { doc.toObject(QuizResultRecord::class.java) }.getOrNull()
                    }
                    if (results.isNotEmpty()) {
                        _resultsFlow.value = results
                    }
                }
            }
            listeners.add(l)
        }
    }

    private fun initChapterProgressMap(): Map<String, List<ChapterProgress>> {
        val map = mutableMapOf<String, List<ChapterProgress>>()
        listOf("beginner", "medium", "advanced", "expert").forEach { diff ->
            val chapters = (1..50).map { ch ->
                ChapterProgress(
                    difficulty = diff,
                    chapterNumber = ch,
                    isUnlocked = (ch == 1),
                    isCompleted = false,
                    highestScore = 0,
                    highestPercentage = 0f,
                    attempts = 0,
                    totalCorrect = 0,
                    totalQuestionsAnswered = 0,
                    accuracyPercentage = 0f,
                    timeTakenSeconds = 0
                )
            }
            map[diff] = chapters
        }
        return map
    }

    fun getChapterProgressList(difficulty: String): List<ChapterProgress> {
        val key = difficulty.lowercase()
        return _chapterProgressMap.value[key] ?: initChapterProgressMap()[key] ?: emptyList()
    }

    fun getQuestionsForChapter(difficulty: String, chapterNumber: Int): List<ComprehensiveQuizQuestion> {
        return DarsENizamiQuizGenerator.generate50Questions(difficulty, chapterNumber)
    }

    fun recordChapterResult(
        difficulty: String,
        chapterNumber: Int,
        score: Int,
        totalQuestions: Int = 50,
        timeTakenSecs: Long
    ): ChapterResultOutcome {
        val key = difficulty.lowercase()
        val currentList = (_chapterProgressMap.value[key] ?: getChapterProgressList(key)).toMutableList()
        val index = currentList.indexOfFirst { it.chapterNumber == chapterNumber }

        val percentage = if (totalQuestions > 0) (score.toFloat() / totalQuestions.toFloat()) * 100f else 0f
        val isPassed = percentage >= 90f
        var isNextChapterUnlocked = false
        var unlockedChapterNumber: Int? = null

        if (index != -1) {
            val old = currentList[index]
            val newAttempts = old.attempts + 1
            val newHighestScore = maxOf(old.highestScore, score)
            val newHighestPct = maxOf(old.highestPercentage, percentage)
            val newTotalCorrect = old.totalCorrect + score
            val newTotalAns = old.totalQuestionsAnswered + totalQuestions
            val newAccuracy = if (newTotalAns > 0) (newTotalCorrect.toFloat() / newTotalAns.toFloat()) * 100f else 0f
            val newBestTime = if (old.timeTakenSeconds == 0L) timeTakenSecs else minOf(old.timeTakenSeconds, timeTakenSecs)

            currentList[index] = old.copy(
                isCompleted = old.isCompleted || isPassed,
                highestScore = newHighestScore,
                highestPercentage = newHighestPct,
                attempts = newAttempts,
                totalCorrect = newTotalCorrect,
                totalQuestionsAnswered = newTotalAns,
                accuracyPercentage = newAccuracy,
                timeTakenSeconds = newBestTime
            )

            if (isPassed && chapterNumber < 50) {
                val nextChapterIndex = currentList.indexOfFirst { it.chapterNumber == chapterNumber + 1 }
                if (nextChapterIndex != -1) {
                    val nextChapter = currentList[nextChapterIndex]
                    if (!nextChapter.isUnlocked) {
                        currentList[nextChapterIndex] = nextChapter.copy(isUnlocked = true)
                        isNextChapterUnlocked = true
                        unlockedChapterNumber = chapterNumber + 1
                    }
                }
            }
        }

        val updatedMap = _chapterProgressMap.value.toMutableMap()
        updatedMap[key] = currentList
        _chapterProgressMap.value = updatedMap

        // Sync to Firestore
        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)
            ?.collection("chapter_progress")?.document("${key}_ch_$chapterNumber")
            ?.set(mapOf(
                "difficulty" to key,
                "chapterNumber" to chapterNumber,
                "score" to score,
                "percentage" to percentage,
                "isPassed" to isPassed,
                "timestamp" to System.currentTimeMillis()
            ), SetOptions.merge())

        return ChapterResultOutcome(
            percentage = percentage,
            isPassed = isPassed,
            isNextChapterUnlocked = isNextChapterUnlocked,
            unlockedChapterNumber = unlockedChapterNumber
        )
    }

    val darjatList = listOf(
        "Darja-e-Ula", "Darja-e-Sania", "Darja-e-Salisa",
        "Darja-e-Rabia", "Darja-e-Khamisa", "Darja-e-Sadisa",
        "Darja-e-Sabi'a", "Dora Hadith"
    )

    val subjectsList = listOf(
        "Nahw", "Sarf", "Fiqh", "Usul-ul-Fiqh", "Hadith",
        "Tafseer", "Balagha", "Mantiq", "Falsafa", "Aqeedah",
        "Arabic Literature", "Insha", "Tajweed", "History"
    )

    fun getQuizById(id: String): QuizSet? {
        return _quizzesFlow.value.find { it.id == id } ?: _quizzesFlow.value.firstOrNull()
    }

    fun saveResult(record: QuizResultRecord) {
        _resultsFlow.value = listOf(record) + _resultsFlow.value
        db?.collection("quiz_results")?.document(record.id)?.set(record)

        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)
            ?.collection("quiz_results")?.document(record.id)?.set(record)
    }

    fun toggleBookmark(question: ComprehensiveQuizQuestion) {
        val current = _bookmarksFlow.value.toMutableList()
        val existing = current.find { it.id == question.id }
        if (existing != null) {
            current.remove(existing)
            db?.collection("user_bookmarks")?.document("q_bm_${question.id}")?.delete()
        } else {
            current.add(question.copy(isBookmarked = true))
            db?.collection("user_bookmarks")?.document("q_bm_${question.id}")?.set(question)
        }
        _bookmarksFlow.value = current
    }

    fun toggleFavorite(question: ComprehensiveQuizQuestion) {
        val current = _favoritesFlow.value.toMutableList()
        val existing = current.find { it.id == question.id }
        if (existing != null) {
            current.remove(existing)
            db?.collection("user_favorites")?.document("q_fav_${question.id}")?.delete()
        } else {
            current.add(question.copy(isFavorite = true))
            db?.collection("user_favorites")?.document("q_fav_${question.id}")?.set(question)
        }
        _favoritesFlow.value = current
    }

    fun addQuiz(quizSet: QuizSet) {
        _quizzesFlow.value = listOf(quizSet) + _quizzesFlow.value
        db?.collection("quizzes")?.document(quizSet.id)?.set(quizSet, SetOptions.merge())
    }

    fun deleteQuiz(quizId: String) {
        _quizzesFlow.value = _quizzesFlow.value.filter { it.id != quizId }
        db?.collection("quizzes")?.document(quizId)?.delete()
    }

    fun getBadges(): List<QuizBadge> {
        val count = _resultsFlow.value.size
        return listOf(
            QuizBadge("b1", "First Step", "Completed your 1st Islamic Quiz", "star", isUnlocked = count >= 1),
            QuizBadge("b2", "Scholar Scholar", "Completed 5 Quizzes", "school", isUnlocked = count >= 5),
            QuizBadge("b3", "Fiqh Master", "Achieved 100% in Fiqh Assessment", "verified", isUnlocked = _resultsFlow.value.any { it.subject == "Fiqh" && it.percentage >= 100f }),
            QuizBadge("b4", "Nahw Expert", "Scored 80%+ in Arabic Grammar", "spellcheck", isUnlocked = _resultsFlow.value.any { it.subject == "Nahw" && it.percentage >= 80f }),
            QuizBadge("b5", "Top Student", "Ranked #1 on Weekly Leaderboard", "military_tech", isUnlocked = true)
        )
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        return listOf(
            LeaderboardEntry(1, "Hafiz Muhammad Rasheed", "Dora Hadith", 1250, 24, "Gold Scholar"),
            LeaderboardEntry(2, "Usman Ahmad", "Darja-e-Sabi'a", 1120, 20, "Silver Scholar"),
            LeaderboardEntry(3, "Abdullah Qasim", "Darja-e-Sadisa", 980, 18, "Bronze Scholar"),
            LeaderboardEntry(4, "Zubair Mahmood", "Darja-e-Khamisa", 870, 16, "Master"),
            LeaderboardEntry(5, "Bilal Farooq", "Darja-e-Rabia", 750, 14, "Achiever")
        )
    }

    fun generateCertificate(quizTitle: String, darja: String, subject: String, score: Int, percentage: Float): CertificateInfo {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        val code = "VERIFIED-RASHEED-${UUID.randomUUID().toString().take(8).uppercase()}"

        val cert = CertificateInfo(
            id = UUID.randomUUID().toString(),
            studentName = "Hafiz Muhammad Rasheed",
            courseName = "Dars-e-Nizami $subject Assessment",
            darja = darja,
            subject = subject,
            score = score,
            percentage = percentage,
            date = dateStr,
            verificationQrCode = code
        )

        db?.collection("certificates")?.document(cert.id)?.set(cert)
        return cert
    }

    fun destroy() {
        listeners.forEach { it.remove() }
        listeners.clear()
    }

    private fun initialQuizzes() = listOf(
        QuizSet(
            id = "quiz_01",
            title = "Fiqh Beginner Assessment - Mukhtasar al-Quduri",
            description = "Comprehensive assessment on Kitab al-Taharah & Kitab al-Salah from Quduri.",
            darja = "Darja-e-Ula",
            subject = "Fiqh",
            chapter = "Taharah & Salah",
            bookName = "Mukhtasar al-Quduri",
            questionCount = 5,
            durationMinutes = 10,
            totalMarks = 25,
            difficulty = "Easy",
            questions = listOf(
                ComprehensiveQuizQuestion(
                    id = "q1",
                    quizId = "quiz_01",
                    question = "What is the foundational ruling on Wudu according to Mukhtasar al-Quduri?",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ",
                    translation = "O you who have believed, when you rise to [perform] prayer, wash your faces...",
                    options = listOf("4 Faraid (Obligations)", "6 Faraid", "3 Faraid", "5 Faraid"),
                    correctAnswerIndex = 0,
                    explanation = "The Faraid of Wudu in Hanafi Fiqh are 4: washing face, washing arms to elbows, wiping 1/4th head, washing feet to ankles.",
                    bookName = "Mukhtasar al-Quduri",
                    darja = "Darja-e-Ula",
                    subject = "Fiqh",
                    chapter = "Taharah",
                    pageNumber = 8,
                    difficulty = "Easy",
                    marks = 5
                ),
                ComprehensiveQuizQuestion(
                    id = "q2",
                    quizId = "quiz_01",
                    question = "Which water is permissible for Taharah (purification)?",
                    arabicText = "الْمَاءُ الطَّهُورُ الَّذِي لَمْ تُغَيِّرْهُ نَجَاسَةٌ",
                    translation = "Pure water that has not been altered by impurity...",
                    options = listOf("Rain, river and well water", "Rose water only", "Water mixed with soup", "Used (Musta'mal) water"),
                    correctAnswerIndex = 0,
                    explanation = "Natural water like rain, river, spring, and well water retain original purity (Mutlaq Water) for ritual purification.",
                    bookName = "Mukhtasar al-Quduri",
                    darja = "Darja-e-Ula",
                    subject = "Fiqh",
                    chapter = "Taharah",
                    pageNumber = 12,
                    difficulty = "Easy",
                    marks = 5
                )
            )
        )
    )

    private fun initialResults() = listOf(
        QuizResultRecord(
            id = "res_1",
            quizTitle = "Fiqh Beginner Assessment",
            darja = "Darja-e-Ula",
            subject = "Fiqh",
            score = 25,
            totalQuestions = 5,
            totalMarks = 25,
            percentage = 100f,
            correctAnswers = 5,
            wrongAnswers = 0,
            skippedQuestions = 0,
            timeTakenSeconds = 180,
            rank = "Rank 1"
        )
    )
}
