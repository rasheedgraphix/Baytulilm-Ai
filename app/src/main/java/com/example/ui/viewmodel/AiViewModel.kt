package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BookEntity
import com.example.data.repository.GeminiRepository
import com.example.data.repository.RAGResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AiMode {
    CHAT, ASK_BOOK, GLOBAL_SEARCH, SUMMARIZER, TRANSLATE, GRAMMAR, QUIZ, FLASHCARDS, NOTES
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
    val citation: RAGResponse? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageSender {
    USER, AI
}

class AiViewModel : ViewModel() {

    private val geminiRepository = GeminiRepository()

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

    init {
        loadFirestoreChatAndNotes()
    }

    private fun loadFirestoreChatAndNotes() {
        val firestore = db ?: return
        val uid = auth?.currentUser?.uid ?: "user_101"

        // Load chat history
        runCatching {
            firestore.collection("users").document(uid).collection("chat_history")
                .orderBy("timestamp")
                .limit(50)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        val history = snapshot.documents.mapNotNull { doc ->
                            runCatching {
                                val text = doc.getString("text") ?: ""
                                val senderStr = doc.getString("sender") ?: "AI"
                                val sender = if (senderStr == "USER") MessageSender.USER else MessageSender.AI
                                val id = doc.id
                                val ts = doc.getLong("timestamp") ?: System.currentTimeMillis()
                                ChatMessage(id = id, sender = sender, text = text, timestamp = ts)
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

    fun sendRAGMessage(prompt: String, libraryBooks: List<BookEntity>) {
        if (prompt.isBlank()) return

        val userMsg = ChatMessage(sender = MessageSender.USER, text = prompt)
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

            val result = geminiRepository.askScholarWithRAG(
                userPrompt = prompt,
                selectedBook = _selectedBook.value,
                libraryBooks = libraryBooks,
                history = history
            )
            _isLoading.value = false

            result.onSuccess { ragResp ->
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
                _errorMessage.value = err.localizedMessage ?: "Failed to get AI response."
                val errorMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = "Error: ${err.localizedMessage}. Please verify Gemini API key in AI Studio Secrets."
                )
                _messages.value = _messages.value + errorMsg
            }
        }
    }

    fun analyzeGrammar(sentence: String) {
        if (sentence.isBlank()) return
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.analyzeGrammar(sentence)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateSummary(bookTitle: String, type: String) {
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateSummary(bookTitle, type)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun translateText(text: String, src: String, target: String) {
        if (text.isBlank()) return
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.translateText(text, src, target)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateQuiz(bookTitle: String, difficulty: String, type: String) {
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateQuiz(bookTitle, difficulty, type)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
        }
    }

    fun generateFlashcards(bookTitle: String) {
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateFlashcards(bookTitle)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
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
        _isLoading.value = true
        _generatedResult.value = null
        viewModelScope.launch {
            val result = geminiRepository.generateStudyNotes(topic)
            _isLoading.value = false
            result.onSuccess { _generatedResult.value = it }
                .onFailure { _errorMessage.value = it.localizedMessage }
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
