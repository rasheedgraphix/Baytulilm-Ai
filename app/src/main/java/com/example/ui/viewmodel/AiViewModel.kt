package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BookEntity
import com.example.data.repository.GeminiRepository
import com.example.data.repository.RAGResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AiMode {
    CHAT, ASK_BOOK, GLOBAL_SEARCH, SUMMARIZER, TRANSLATE, GRAMMAR, QUIZ, FLASHCARDS, NOTES
}

fun Bitmap.toBase64String(maxDimension: Int = 1024): String {
    val width = this.width
    val height = this.height
    val bitmapToUse = if (width > maxDimension || height > maxDimension) {
        val ratio = width.toFloat() / height.toFloat()
        val targetWidth = if (ratio > 1) maxDimension else (maxDimension * ratio).toInt()
        val targetHeight = if (ratio > 1) (maxDimension / ratio).toInt() else maxDimension
        Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
    } else {
        this
    }
    val outputStream = ByteArrayOutputStream()
    bitmapToUse.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

data class SavedAiNote(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis()
)

data class SavedFlashcard(
    val id: String = java.util.UUID.randomUUID().toString(),
    val front: String = "",
    val back: String = "",
    val bookTitle: String = "",
    var isMastered: Boolean = false
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender = MessageSender.AI,
    val text: String = "",
    val imageBitmap: Bitmap? = null,
    val citation: RAGResponse? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageSender {
    USER, AI
}

class AiViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiRepository = GeminiRepository(application.applicationContext)

    fun getCustomApiKey(): String? {
        return GeminiRepository.getCustomApiKey(getApplication<Application>().applicationContext)
    }

    fun saveCustomApiKey(key: String?) {
        GeminiRepository.saveCustomApiKey(getApplication<Application>().applicationContext, key)
    }

    fun getGroqApiKey(): String? =
        GeminiRepository.getGroqApiKey(getApplication<Application>().applicationContext)

    fun saveGroqApiKey(key: String?) =
        GeminiRepository.saveGroqApiKey(getApplication<Application>().applicationContext, key)

    fun getMistralApiKey(): String? =
        GeminiRepository.getMistralApiKey(getApplication<Application>().applicationContext)

    fun saveMistralApiKey(key: String?) =
        GeminiRepository.saveMistralApiKey(getApplication<Application>().applicationContext, key)

    fun getCloudflareAiKey(): String? =
        GeminiRepository.getCloudflareAiKey(getApplication<Application>().applicationContext)

    fun saveCloudflareAiKey(key: String?) =
        GeminiRepository.saveCloudflareAiKey(getApplication<Application>().applicationContext, key)

    fun getOpenRouterApiKey(): String? =
        GeminiRepository.getOpenRouterApiKey(getApplication<Application>().applicationContext)

    fun saveOpenRouterApiKey(key: String?) =
        GeminiRepository.saveOpenRouterApiKey(getApplication<Application>().applicationContext, key)

    fun getActiveKeysCount(): Int =
        GeminiRepository.getActiveKeysCount(getApplication<Application>().applicationContext)

    fun hasAnyCustomKey(): Boolean =
        GeminiRepository.hasAnyCustomKey(getApplication<Application>().applicationContext)

    private val db: FirebaseFirestore?
        get() = runCatching { FirebaseFirestore.getInstance() }.getOrNull()

    private val auth: FirebaseAuth?
        get() = runCatching { FirebaseAuth.getInstance() }.getOrNull()

    private val _currentMode = MutableStateFlow(AiMode.CHAT)
    val currentMode: StateFlow<AiMode> = _currentMode.asStateFlow()

    private val _selectedBook = MutableStateFlow<BookEntity?>(null)
    val selectedBook: StateFlow<BookEntity?> = _selectedBook.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = "Assalamu Alaikum! I am Baytul Ilm AI Scholar. I answer strictly from your uploaded Islamic books library with verifiable page citations!"
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _generatedResult = MutableStateFlow<String?>(null)
    val generatedResult: StateFlow<String?> = _generatedResult.asStateFlow()

    private val _savedNotes = MutableStateFlow<List<SavedAiNote>>(
        listOf(
            SavedAiNote(title = "Conditions of Wudu in Hanafi Fiqh", content = "Four Fara'id of Wudu: Washing face, washing hands up to elbows, wiping 1/4th head, washing feet up to ankles."),
            SavedAiNote(title = "Nahw Rule: Marfoo'at", content = "The Marfoo'at are eight: Fa'il, Na'ib Fa'il, Mubtada, Khabar, Ism of Kana, Khabar of Inna...")
        )
    )
    val savedNotes: StateFlow<List<SavedAiNote>> = _savedNotes.asStateFlow()

    private val _savedFlashcards = MutableStateFlow<List<SavedFlashcard>>(
        listOf(
            SavedFlashcard(front = "What is the primary condition of Taharah for Salah?", back = "Ritual purity (Wudu / Ghusl) and Cleanliness of body, clothes, and place.", bookTitle = "Nur al-Idah"),
            SavedFlashcard(front = "Define 'Hadith Sahih' according to Muhadditheen", back = "A Hadith with a continuous chain of trustworthy and precise narrators without defects or anomaly.", bookTitle = "Nukhbat al-Fikar")
        )
    )
    val savedFlashcards: StateFlow<List<SavedFlashcard>> = _savedFlashcards.asStateFlow()

    // Daily AI Question Quota (10 questions per user per day)
    companion object {
        const val MAX_DAILY_AI_QUESTIONS = 10
        const val DAILY_LIMIT_EXCEEDED_MESSAGE = "آپ آج کے 10 AI سوالات مکمل کر چکے ہیں۔ کل دوبارہ کوشش کریں۔"
        private const val PREFS_NAME = "baytulilm_ai_usage_prefs"
        private const val KEY_USAGE_DATE = "key_ai_usage_date"
        private const val KEY_USAGE_COUNT = "key_ai_usage_count"
    }

    private val _dailyAiUsage = MutableStateFlow(0)
    val dailyAiUsage: StateFlow<Int> = _dailyAiUsage.asStateFlow()

    private val _remainingAiQuestions = MutableStateFlow(MAX_DAILY_AI_QUESTIONS)
    val remainingAiQuestions: StateFlow<Int> = _remainingAiQuestions.asStateFlow()

    private val _isLimitExceeded = MutableStateFlow(false)
    val isLimitExceeded: StateFlow<Boolean> = _isLimitExceeded.asStateFlow()

    init {
        refreshDailyUsageQuota()
        loadFirestoreChatAndNotes()
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun isDailyLimitReached(): Boolean {
        refreshDailyUsageQuota()
        return _dailyAiUsage.value >= MAX_DAILY_AI_QUESTIONS
    }

    /**
     * Checks local SharedPreferences and resets count to 0 if a new calendar day has started.
     */
    fun refreshDailyUsageQuota() {
        val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayStr = getTodayDateString()
        val storedDate = prefs.getString(KEY_USAGE_DATE, "") ?: ""
        val storedCount = prefs.getInt(KEY_USAGE_COUNT, 0)

        if (storedDate != todayStr) {
            // New day: Reset usage to 0
            prefs.edit()
                .putString(KEY_USAGE_DATE, todayStr)
                .putInt(KEY_USAGE_COUNT, 0)
                .apply()
            _dailyAiUsage.value = 0
            _remainingAiQuestions.value = MAX_DAILY_AI_QUESTIONS
            _isLimitExceeded.value = false
        } else {
            // Same day: load local count
            val count = storedCount.coerceAtLeast(0)
            _dailyAiUsage.value = count
            val remaining = (MAX_DAILY_AI_QUESTIONS - count).coerceAtLeast(0)
            _remainingAiQuestions.value = remaining
            _isLimitExceeded.value = count >= MAX_DAILY_AI_QUESTIONS
        }
    }

    private fun recordSuccessfulAiRequest() {
        val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayStr = getTodayDateString()
        val storedDate = prefs.getString(KEY_USAGE_DATE, "") ?: ""
        val storedCount = prefs.getInt(KEY_USAGE_COUNT, 0)

        val currentCount = if (storedDate == todayStr) storedCount else 0
        val nextCount = (currentCount + 1).coerceAtMost(MAX_DAILY_AI_QUESTIONS)

        prefs.edit()
            .putString(KEY_USAGE_DATE, todayStr)
            .putInt(KEY_USAGE_COUNT, nextCount)
            .apply()

        _dailyAiUsage.value = nextCount
        _remainingAiQuestions.value = (MAX_DAILY_AI_QUESTIONS - nextCount).coerceAtLeast(0)
        _isLimitExceeded.value = nextCount >= MAX_DAILY_AI_QUESTIONS

        val firestore = db
        val uid = auth?.currentUser?.uid ?: "user_101"
        if (firestore != null) {
            val docRef = firestore.collection("users").document(uid).collection("ai_usage").document("daily")
            docRef.set(
                mapOf(
                    "date" to todayStr,
                    "count" to nextCount,
                    "lastUpdated" to System.currentTimeMillis()
                ),
                SetOptions.merge()
            )
        }
    }

    private fun loadFirestoreChatAndNotes() {
        val firestore = db ?: return
        val uid = auth?.currentUser?.uid ?: "user_101"

        // Load daily AI usage from Firestore
        runCatching {
            val todayStr = getTodayDateString()
            firestore.collection("users").document(uid).collection("ai_usage").document("daily")
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        val recordedDate = snapshot.getString("date") ?: ""
                        if (recordedDate == todayStr) {
                            val count = (snapshot.getLong("count") ?: 0L).toInt()
                            // Only update if count is valid for today
                            if (count >= 0) {
                                _dailyAiUsage.value = count
                                _remainingAiQuestions.value = (MAX_DAILY_AI_QUESTIONS - count).coerceAtLeast(0)
                                _isLimitExceeded.value = count >= MAX_DAILY_AI_QUESTIONS
                            }
                        } else {
                            // New day recorded in Firestore: reset
                            _dailyAiUsage.value = 0
                            _remainingAiQuestions.value = MAX_DAILY_AI_QUESTIONS
                            _isLimitExceeded.value = false
                        }
                    }
                }
        }

        // Load chat history, filtering out any old malformed raw JSON error messages
        runCatching {
            firestore.collection("users").document(uid).collection("chat_history")
                .orderBy("timestamp")
                .limit(50)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        val history = snapshot.documents.mapNotNull { doc ->
                            runCatching {
                                val text = doc.getString("text") ?: ""
                                // Clean up any old corrupt JSON error payloads from earlier builds
                                if (text.startsWith("{\"error\"") || text.contains("\"NOT_FOUND\"") || text.contains("models/gemini-2.5-flash")) {
                                    doc.reference.delete()
                                    null
                                } else {
                                    val senderStr = doc.getString("sender") ?: "AI"
                                    val sender = if (senderStr == "USER") MessageSender.USER else MessageSender.AI
                                    val id = doc.id
                                    val ts = doc.getLong("timestamp") ?: System.currentTimeMillis()
                                    ChatMessage(id = id, sender = sender, text = text, timestamp = ts)
                                }
                            }.getOrNull()
                        }
                        if (history.isNotEmpty()) {
                            _messages.value = history
                        }
                    }
                }
        }

        // Load saved notes
        runCatching {
            firestore.collection("users").document(uid).collection("ai_notes")
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        val notes = snapshot.documents.mapNotNull { doc ->
                            runCatching { doc.toObject(SavedAiNote::class.java) }.getOrNull()
                        }
                        if (notes.isNotEmpty()) _savedNotes.value = notes
                    }
                }
        }

        // Load saved flashcards
        runCatching {
            firestore.collection("users").document(uid).collection("ai_flashcards")
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        val cards = snapshot.documents.mapNotNull { doc ->
                            runCatching { doc.toObject(SavedFlashcard::class.java) }.getOrNull()
                        }
                        if (cards.isNotEmpty()) _savedFlashcards.value = cards
                    }
                }
        }
    }

    fun setMode(mode: AiMode) {
        _currentMode.value = mode
        _generatedResult.value = null
    }

    fun setSelectedBook(book: BookEntity?) {
        _selectedBook.value = book
    }

    fun regenerateLastMessage(libraryBooks: List<BookEntity>) {
        val lastUserMessage = _messages.value.lastOrNull { it.sender == MessageSender.USER } ?: return
        if (_messages.value.isNotEmpty() && _messages.value.last().sender == MessageSender.AI) {
            _messages.value = _messages.value.dropLast(1)
        }
        sendRAGMessage(lastUserMessage.text, libraryBooks)
    }

    fun sendRAGMessage(
        prompt: String,
        libraryBooks: List<BookEntity>,
        imageBitmap: Bitmap? = null
    ) {
        if (prompt.isBlank() && imageBitmap == null) return

        // 1. Check daily limit before sending request or calling Gemini
        if (isDailyLimitReached()) {
            val userMsg = ChatMessage(sender = MessageSender.USER, text = prompt, imageBitmap = imageBitmap)
            val limitMsg = ChatMessage(
                sender = MessageSender.AI,
                text = DAILY_LIMIT_EXCEEDED_MESSAGE
            )
            _messages.value = _messages.value + userMsg + limitMsg
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }

        val userMsg = ChatMessage(sender = MessageSender.USER, text = prompt, imageBitmap = imageBitmap)
        _messages.value = _messages.value + userMsg
        _isLoading.value = true
        _errorMessage.value = null

        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)?.collection("chat_history")
            ?.document(userMsg.id)?.set(mapOf(
                "id" to userMsg.id,
                "sender" to "USER",
                "text" to userMsg.text,
                "timestamp" to userMsg.timestamp
            ))

        viewModelScope.launch {
            val history = _messages.value.dropLast(1).chunked(2).mapNotNull { pair ->
                if (pair.size == 2 && pair[0].sender == MessageSender.USER && pair[1].sender == MessageSender.AI) {
                    pair[0].text to pair[1].text
                } else null
            }

            val imageBase64 = imageBitmap?.toBase64String()

            val result = geminiRepository.askScholarWithRAG(
                userPrompt = prompt,
                selectedBook = _selectedBook.value,
                libraryBooks = libraryBooks,
                history = history,
                imageBase64 = imageBase64
            )
            _isLoading.value = false

            result.onSuccess { ragResp ->
                // Record & increment counter ONLY on successful Gemini response
                recordSuccessfulAiRequest()

                val aiMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = ragResp.answer,
                    citation = ragResp
                )
                _messages.value = _messages.value + aiMsg

                db?.collection("users")?.document(uid)?.collection("chat_history")
                    ?.document(aiMsg.id)?.set(mapOf(
                        "id" to aiMsg.id,
                        "sender" to "AI",
                        "text" to aiMsg.text,
                        "timestamp" to aiMsg.timestamp
                    ))

            }.onFailure { err ->
                val errorText = if (err.message == DAILY_LIMIT_EXCEEDED_MESSAGE || isDailyLimitReached()) {
                    DAILY_LIMIT_EXCEEDED_MESSAGE
                } else {
                    err.localizedMessage ?: "AI سروس سے رابطہ نہیں ہو سکا۔"
                }
                _errorMessage.value = errorText
                val errorMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = errorText
                )
                _messages.value = _messages.value + errorMsg
            }
        }
    }

    fun analyzeGrammar(sentence: String) {
        if (sentence.isBlank()) return
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.analyzeGrammar(sentence)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateSummary(bookTitle: String, type: String) {
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateSummary(bookTitle, type)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun translateText(text: String, src: String, target: String) {
        if (text.isBlank()) return
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.translateText(text, src, target)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateQuiz(bookTitle: String, difficulty: String, type: String) {
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateQuiz(bookTitle, difficulty, type)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateFlashcards(bookTitle: String) {
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateFlashcards(bookTitle)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun saveNote(title: String, content: String, category: String = "General") {
        val note = SavedAiNote(title = title, content = content, category = category)
        _savedNotes.value = listOf(note) + _savedNotes.value

        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)?.collection("ai_notes")
            ?.document(note.id)?.set(note, SetOptions.merge())
    }

    fun deleteNote(noteId: String) {
        _savedNotes.value = _savedNotes.value.filter { it.id != noteId }
        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)?.collection("ai_notes")
            ?.document(noteId)?.delete()
    }

    fun saveFlashcard(front: String, back: String, bookTitle: String) {
        val card = SavedFlashcard(front = front, back = back, bookTitle = bookTitle)
        _savedFlashcards.value = listOf(card) + _savedFlashcards.value

        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)?.collection("ai_flashcards")
            ?.document(card.id)?.set(card, SetOptions.merge())
    }

    fun toggleFlashcardMastered(cardId: String) {
        _savedFlashcards.value = _savedFlashcards.value.map {
            if (it.id == cardId) it.copy(isMastered = !it.isMastered) else it
        }
        val target = _savedFlashcards.value.find { it.id == cardId }
        if (target != null) {
            val uid = auth?.currentUser?.uid ?: "user_101"
            db?.collection("users")?.document(uid)?.collection("ai_flashcards")
                ?.document(cardId)?.set(target, SetOptions.merge())
        }
    }

    fun generateNotes(topic: String) {
        if (topic.isBlank()) return
        if (isDailyLimitReached()) {
            _errorMessage.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            _generatedResult.value = DAILY_LIMIT_EXCEEDED_MESSAGE
            return
        }
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateStudyNotes(topic)
            _isLoading.value = false
            result.onSuccess {
                recordSuccessfulAiRequest()
                _generatedResult.value = it
            }.onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun clearHistory() {
        _messages.value = listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = "Assalamu Alaikum! Conversation history cleared. Ask me any topic from your uploaded Islamic books!"
            )
        )
        val uid = auth?.currentUser?.uid ?: "user_101"
        db?.collection("users")?.document(uid)?.collection("chat_history")
            ?.get()?.addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    doc.reference.delete()
                }
            }
    }
}
