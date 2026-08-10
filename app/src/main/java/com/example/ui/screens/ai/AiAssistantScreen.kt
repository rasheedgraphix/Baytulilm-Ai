package com.example.ui.screens.ai

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.BookEntity
import com.example.data.repository.RAGResponse
import com.example.ui.viewmodel.AiMode
import com.example.ui.viewmodel.AiViewModel
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MessageSender
import com.example.util.LocalAppLanguage
import java.util.Locale

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ui.viewmodel.SavedAiNote
import com.example.ui.viewmodel.SavedFlashcard

@Composable
fun AiAssistantScreen(
    aiViewModel: AiViewModel,
    mainViewModel: MainViewModel
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val allBooks by mainViewModel.allBooks.collectAsStateWithLifecycle()
    val currentMode by aiViewModel.currentMode.collectAsStateWithLifecycle()
    val selectedBook by aiViewModel.selectedBook.collectAsStateWithLifecycle()
    val messages by aiViewModel.messages.collectAsStateWithLifecycle()
    val isLoading by aiViewModel.isLoading.collectAsStateWithLifecycle()
    val generatedResult by aiViewModel.generatedResult.collectAsStateWithLifecycle()
    val savedNotes by aiViewModel.savedNotes.collectAsStateWithLifecycle()
    val savedFlashcards by aiViewModel.savedFlashcards.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    var bookDropdownExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Speech to text voice input launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                inputText = spokenText
            }
        }
    }

    // Text To Speech Initialization
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = if (isUrdu) Locale("ur") else Locale.ENGLISH
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // AI Top Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_baytul_ilm_icon_1784999011685),
                                contentDescription = "AI Scholar",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (isUrdu) "بیت العلم AI اسکالر" else "Baytul Ilm AI Scholar",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isUrdu) "کتب کی بنیاد پر اسلامی علم و تحقیق (Gemini AI)" else "RAG over Uploaded Islamic Books (Gemini 3.5)",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = { aiViewModel.clearHistory() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = if (isUrdu) "چیٹ صاف کریں" else "Clear Chat",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Book Scope Filter Selector
                Box {
                    Card(
                        onClick = { bookDropdownExpanded = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = "Book Scope",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedBook?.let { if (isUrdu) "دائرہ: ${it.title}" else "Scope: ${it.title}" } ?: (if (isUrdu) "دائرہ: تمام کتب" else "Scope: All Library Books"),
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            if (selectedBook != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Book Filter",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { aiViewModel.setSelectedBook(null) }
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = bookDropdownExpanded,
                        onDismissRequest = { bookDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (isUrdu) "تمام کتب" else "All Library Books") },
                            onClick = {
                                aiViewModel.setSelectedBook(null)
                                bookDropdownExpanded = false
                            }
                        )
                        allBooks.forEach { book ->
                            DropdownMenuItem(
                                text = { Text("${book.title} (${book.darja})") },
                                onClick = {
                                    aiViewModel.setSelectedBook(book)
                                    bookDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Mode Selector Tabs (LazyRow)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            items(AiMode.values()) { mode ->
                val label = when (mode) {
                    AiMode.CHAT -> if (isUrdu) "💬 کتاب سے ذہین گفتگو" else "💬 Smart Book Chat"
                    AiMode.ASK_BOOK -> if (isUrdu) "📖 منتخب کتاب" else "📖 Selected Kitab"
                    AiMode.GLOBAL_SEARCH -> if (isUrdu) "🔍 کتب میں تلاش" else "🔍 Global Library Search"
                    AiMode.SUMMARIZER -> if (isUrdu) "📝 کتاب کا خلاصہ" else "📝 Book Summary"
                    AiMode.TRANSLATE -> if (isUrdu) "🌐 ترجمہ نگاری" else "🌐 Translation"
                    AiMode.GRAMMAR -> if (isUrdu) "✒️ قواعد (نحو و صرف)" else "✒️ Grammar (Nahw/Sarf)"
                    AiMode.QUIZ -> if (isUrdu) "❓ AI کوئز (30/50/100 سوالات)" else "❓ AI Quiz (30/50/100 MCQs)"
                    AiMode.FLASHCARDS -> if (isUrdu) "🎴 فلیش کارڈز" else "🎴 Flashcards"
                    AiMode.NOTES -> if (isUrdu) "📌 AI نوٹس" else "📌 AI Notes"
                }

                FilterChip(
                    selected = currentMode == mode,
                    onClick = { aiViewModel.setMode(mode) },
                    label = { Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Mode Content Switcher
        when (currentMode) {
            AiMode.CHAT, AiMode.ASK_BOOK -> {
                AiChatView(
                    aiViewModel = aiViewModel,
                    allBooks = allBooks,
                    messages = messages,
                    isLoading = isLoading,
                    listState = listState,
                    inputText = inputText,
                    onInputTextChange = { inputText = it },
                    onVoiceInput = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (isUrdu) "ur" else "ar")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, if (isUrdu) "عربی، اردو یا انگریزی میں سوال بولیں..." else "Speak question in Arabic, Urdu, or English...")
                        }
                        try {
                            speechLauncher.launch(intent)
                        } catch (e: Exception) {
                            // Fallback if speech intent not available
                        }
                    },
                    onSend = { prompt ->
                        aiViewModel.sendRAGMessage(prompt, allBooks)
                        inputText = ""
                    },
                    onSpeak = { text ->
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    onCopy = { text ->
                        clipboardManager.setText(AnnotatedString(text))
                    },
                    onShare = { text ->
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, text)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, if (isUrdu) "اسلامی جواب شیئر کریں" else "Share Islamic Answer"))
                    },
                    onRegenerate = {
                        aiViewModel.regenerateLastMessage(allBooks)
                    }
                )
            }

            AiMode.GLOBAL_SEARCH -> {
                GlobalSearchView(
                    allBooks = allBooks,
                    isLoading = isLoading,
                    onSearch = { query ->
                        aiViewModel.sendRAGMessage("GLOBAL SEARCH ACROSS ALL BOOKS FOR: $query", allBooks)
                    }
                )
            }

            AiMode.SUMMARIZER -> {
                SummarizerView(
                    allBooks = allBooks,
                    selectedBook = selectedBook,
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    onGenerate = { title, type ->
                        aiViewModel.generateSummary(title, type)
                    }
                )
            }

            AiMode.TRANSLATE -> {
                TranslationView(
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    onTranslate = { text, src, target ->
                        aiViewModel.translateText(text, src, target)
                    }
                )
            }

            AiMode.GRAMMAR -> {
                GrammarView(
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    onAnalyze = { sentence ->
                        aiViewModel.analyzeGrammar(sentence)
                    }
                )
            }

            AiMode.QUIZ -> {
                QuizGenView(
                    allBooks = allBooks,
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    onGenerate = { book, diff, type ->
                        aiViewModel.generateQuiz(book, diff, type)
                    }
                )
            }

            AiMode.FLASHCARDS -> {
                FlashcardsView(
                    allBooks = allBooks,
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    savedFlashcards = savedFlashcards,
                    onGenerate = { book ->
                        aiViewModel.generateFlashcards(book)
                    },
                    onSaveCard = { front, back, book ->
                        aiViewModel.saveFlashcard(front, back, book)
                    },
                    onToggleMastered = { cardId ->
                        aiViewModel.toggleFlashcardMastered(cardId)
                    }
                )
            }

            AiMode.NOTES -> {
                NotesGenView(
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    savedNotes = savedNotes,
                    onGenerate = { topic ->
                        aiViewModel.generateNotes(topic)
                    },
                    onSaveNote = { title, content ->
                        aiViewModel.saveNote(title, content)
                    },
                    onDeleteNote = { noteId ->
                        aiViewModel.deleteNote(noteId)
                    },
                    onExportPdf = { title, content ->
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_SUBJECT, title)
                            putExtra(Intent.EXTRA_TEXT, "$title\n\n$content\n\n-- Generated by Baytul Ilm AI Study Assistant")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, if (isUrdu) "نوٹ ایکسپورٹ / شیئر کریں" else "Export Note PDF / Text"))
                    }
                )
            }
        }
    }
}

@Composable
private fun AiChatView(
    aiViewModel: AiViewModel,
    allBooks: List<BookEntity>,
    messages: List<ChatMessage>,
    isLoading: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onVoiceInput: () -> Unit,
    onSend: (String) -> Unit,
    onSpeak: (String) -> Unit,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    onRegenerate: () -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    val quickPrompts = if (isUrdu) listOf(
        "بیت العلم AI ایپ استعمال کرنے کا طریقہ",
        "حنفی فقہ میں وضو کے فرائض و شرائط بیان کریں",
        "وحی کی تعریف اور اقسام کیا ہیں؟",
        "کوئز ماڈیول میں کیا خصوصیات ہیں؟",
        "علمِ نحو اور علمِ صرف میں کیا فرق ہے؟",
        "ڈویلپر اور رابطہ کی تفصیلات"
    ) else listOf(
        "How to use Baytul Ilm AI app?",
        "Explain conditions of Wudu in Hanafi Fiqh",
        "وحی کی تعریف اور اقسام کیا ہیں؟",
        "What features are in the Quiz module?",
        "Difference between Nahw and Sarf",
        "Developer info & Swabi contact details"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick Suggestions Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { prompt ->
                Card(
                    onClick = { onSend(prompt) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isUser = msg.sender == MessageSender.USER

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                ) {
                    Row(
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
                        modifier = Modifier.fillMaxWidth(0.92f)
                    ) {
                        if (!isUser) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        Card(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 16.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                val displayText = if (isUrdu && (msg.text.startsWith("Assalamu Alaikum") || msg.text.startsWith("السلام علیکم"))) {
                                    "السلام علیکم! میں بیت العلم AI اسکالر ہوں۔ میں آپ کی اپ لوڈ کردہ اسلامی کتب کی لائبریری سے تصدیق شدہ صفحہ نمبر کے حوالہ جات کے ساتھ جواب دیتا ہوں!"
                                } else {
                                    msg.text
                                }

                                Text(
                                    text = displayText,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                                )

                                // RAG Source Citation Card
                                msg.citation?.let { cite ->
                                    Spacer(modifier = Modifier.height(10.dp))
                                    val isGeminiGeneral = cite.isGeminiFallback || cite.sourceMode == "GEMINI_GENERAL"

                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isGeminiGeneral) {
                                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                                            } else {
                                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                            }
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = if (isGeminiGeneral) (if (isUrdu) "⚡ ماخذ: عمومی اسلامی علم (Gemini AI)" else "⚡ Source: Gemini General Islamic Knowledge") else (if (isUrdu) "📖 ماخذ: بیت العلم مکتبہ کتب" else "📖 Source: Uploaded Dars-e-Nizami Library"),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isGeminiGeneral) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                    text = if (isUrdu) "اعتماد: ${cite.confidenceScore}" else "Confidence: ${cite.confidenceScore}",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            if (isGeminiGeneral) {
                                                Text(
                                                    text = if (isUrdu) "یہ جواب اپ لوڈ کی گئی نصابی کتب کے بجائے عمومی AI سے لیا گیا ہے۔" else "This answer is not taken from the uploaded library books.",
                                                    fontSize = 11.sp,
                                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            } else {
                                                Text(
                                                    text = if (isUrdu) "کتاب: ${cite.bookName ?: "ڈیجیٹل لائبریری کتاب"}" else "Book: ${cite.bookName ?: "Digital Library Book"}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = if (isUrdu) "درجہ: ${cite.darja ?: "غیر موجود"} | مضمون: ${cite.subject ?: "غیر موجود"} | ${if (cite.pageNumber != null) "صفحہ ${cite.pageNumber}" else "صفحہ 1"}" else "Darja: ${cite.darja ?: "N/A"} | Subject: ${cite.subject ?: "N/A"} | ${cite.pageNumber ?: "Page 1"}",
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                cite.quotedParagraph?.let { q ->
                                                    if (q.isNotBlank()) {
                                                        Spacer(modifier = Modifier.height(6.dp))
                                                        Row {
                                                            Icon(
                                                                imageVector = Icons.Default.FormatQuote,
                                                                contentDescription = "Quote",
                                                                modifier = Modifier.size(14.dp),
                                                                tint = MaterialTheme.colorScheme.primary
                                                            )
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = "\"$q\"",
                                                                fontSize = 11.sp,
                                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Message Action Buttons
                                if (!isUser) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                        IconButton(
                                            onClick = { onSpeak(msg.text) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "TTS", modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(
                                            onClick = { onCopy(msg.text) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(
                                            onClick = { onShare(msg.text) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(
                                            onClick = { onRegenerate() },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Regenerate Answer", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "بیت العلم AI اسکالر کتب میں تلاش اور جواب تیار کر رہا ہے..." else "Baytul Ilm AI Scholar searching uploaded books & generating response...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Input Field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onVoiceInput() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChange,
                    placeholder = { Text(if (isUrdu) "فقہ، حدیث، نحو یا تفسیر کا سوال پوچھیں..." else "Ask Fiqh, Hadith, Nahw, or Tafseer question...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_prompt_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            onSend(inputText)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag("ai_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun GlobalSearchView(
    allBooks: List<BookEntity>,
    isLoading: Boolean,
    onSearch: (String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var searchQuery by remember { mutableStateOf("حكم المسح على الخفين") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "تمام کتب میں AI تلاش" else "Global AI Search Across All Uploaded Books", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = if (isUrdu) "لائبریری کی تمام ${allBooks.size} کتابوں میں صفحہ وار حوالوں کے ساتھ تلاش کریں۔" else "Search across all ${allBooks.size} books in library with verifiable Book, Darja, Chapter, Page citations.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(if (isUrdu) "تلاش کا لفظ، فقهی مسئلہ یا حدیث درج کریں" else "Search Keyword, Fiqh Mas'alah, or Hadith") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { if (searchQuery.isNotBlank()) onSearch(searchQuery) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onSearch(searchQuery) },
            enabled = !isLoading && searchQuery.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isLoading) (if (isUrdu) "تلاش جاری ہے..." else "Searching All Books...") else (if (isUrdu) "کتب میں تلاش کریں" else "Search Library Books"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = if (isUrdu) "مکتبہ کا احاطہ:" else "Library Coverage:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        allBooks.forEach { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = book.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = if (isUrdu) "درجہ: ${book.darja} • مضمون: ${book.subject}" else "Darja: ${book.darja} • Subject: ${book.subject}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(text = if (isUrdu) "${book.pageCount} صفحات" else "${book.pageCount} Pages", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun SummarizerView(
    allBooks: List<BookEntity>,
    selectedBook: BookEntity?,
    isLoading: Boolean,
    generatedResult: String?,
    onGenerate: (String, String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var bookTitle by remember { mutableStateOf(selectedBook?.title ?: allBooks.firstOrNull()?.title ?: "Al-Hidayah") }
    var summaryType by remember { mutableStateOf(if (isUrdu) "مختصر خلاصہ" else "Short Summary") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "کتاب اور ابواب کا خلاصہ نگار" else "Kitab & Chapter Summarizer", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(if (isUrdu) "کتاب یا باب کا نام" else "Book / Chapter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val summaryOptions = if (isUrdu) listOf("مختصر خلاصہ", "مفصل خلاصہ", "امتحانی خلاصہ", "دہرائی نوٹس") else listOf("Short Summary", "Detailed Summary", "Exam Summary", "Revision Notes")
            summaryOptions.forEach { type ->
                FilterChip(
                    selected = summaryType == type,
                    onClick = { summaryType = type },
                    label = { Text(type, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(bookTitle, summaryType) },
            enabled = !isLoading && bookTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "خلاصہ تیار ہو رہا ہے..." else "Generating Summary...") else (if (isUrdu) "$summaryType تیار کریں" else "Generate $summaryType"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = result, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
private fun TranslationView(
    isLoading: Boolean,
    generatedResult: String?,
    onTranslate: (String, String, String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var textToTranslate by remember { mutableStateOf("المجلس الأول في بيان أصول الفقه") }
    var srcLang by remember { mutableStateOf("Arabic") }
    var targetLang by remember { mutableStateOf("Urdu") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "علمی ترجمہ نگاری ٹول" else "Scholarly Translation Tool", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = srcLang == "Arabic" && targetLang == "Urdu", onClick = { srcLang = "Arabic"; targetLang = "Urdu" }, label = { Text(if (isUrdu) "عربی ← اردو" else "Arabic → Urdu") })
            FilterChip(selected = srcLang == "Arabic" && targetLang == "English", onClick = { srcLang = "Arabic"; targetLang = "English" }, label = { Text(if (isUrdu) "عربی ← انگریزی" else "Arabic → English") })
            FilterChip(selected = srcLang == "Urdu" && targetLang == "English", onClick = { srcLang = "Urdu"; targetLang = "English" }, label = { Text(if (isUrdu) "اردو ← انگریزی" else "Urdu → English") })
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = textToTranslate,
            onValueChange = { textToTranslate = it },
            label = { Text(if (isUrdu) "ترجمہ کے لیے عبارت تحریر کریں" else "Enter Text to Translate") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onTranslate(textToTranslate, srcLang, targetLang) },
            enabled = !isLoading && textToTranslate.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "ترجمہ ہو رہا ہے..." else "Translating...") else (if (isUrdu) "عبارت کا ترجمہ کریں" else "Translate Text"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = result, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
private fun GrammarView(
    isLoading: Boolean,
    generatedResult: String?,
    onAnalyze: (String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var sentence by remember { mutableStateOf("العلم نور والجهل تارك للحق") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "علمِ نحو و صرف (ترکیب و اعراب)" else "Nahw & Sarf Grammar Mode (ترکیب و اعراب)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = if (isUrdu) "الفاظ کی ترکیب، اعراب، صیغہ، مادّہ اور اردو وضاحت" else "Word, Root, Arabic Parsing (تركيب), I'rab (إعراب), Translation & Explanation",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = sentence,
            onValueChange = { sentence = it },
            label = { Text(if (isUrdu) "عربی جملہ تحریر کریں" else "Enter Arabic Sentence") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAnalyze(sentence) },
            enabled = !isLoading && sentence.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "ترکیب کی جا رہی ہے..." else "Analyzing Grammar...") else (if (isUrdu) "ترکیب و اعراب معائن کریں" else "Analyze Grammar (ترکیب و اعراب)"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = result, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
private fun QuizGenView(
    allBooks: List<BookEntity>,
    isLoading: Boolean,
    generatedResult: String?,
    onGenerate: (String, String, String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var bookTitle by remember { mutableStateOf(allBooks.firstOrNull()?.title ?: "Kanz al-Daqaiq") }
    var countOption by remember { mutableStateOf(if (isUrdu) "30 سوالات" else "30 MCQs") }
    var difficulty by remember { mutableStateOf(if (isUrdu) "درمیانی" else "Medium") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "AI کوئز و امتحان جنریٹر" else "AI Quiz & Exam Generator", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = if (isUrdu) "اپ لوڈ کی گئی کتب سے 30، 50 یا 100 سوالات بمعہ حوالہ جات تیار کریں" else "Generate 30, 50, or 100 MCQs directly from uploaded books with correct answers & citations",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(if (isUrdu) "کتاب کا نام" else "Book Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = if (isUrdu) "کوئز کا سائز:" else "Quiz Size:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val counts = if (isUrdu) listOf("30 سوالات", "50 سوالات", "100 سوالات") else listOf("30 MCQs", "50 MCQs", "100 MCQs")
            counts.forEach { count ->
                FilterChip(selected = countOption == count, onClick = { countOption = count }, label = { Text(count) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = if (isUrdu) "مشکل کی سطح:" else "Difficulty Level:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val diffs = if (isUrdu) listOf("آسان", "درمیانی", "مشکل", "امتحانی سطح") else listOf("Easy", "Medium", "Hard", "Exam Level")
            diffs.forEach { diff ->
                FilterChip(selected = difficulty == diff, onClick = { difficulty = diff }, label = { Text(diff) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(bookTitle, difficulty, countOption) },
            enabled = !isLoading && bookTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "کوئز تیار ہو رہا ہے..." else "Generating Quiz...") else (if (isUrdu) "$countOption تیار کریں" else "Generate $countOption"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = result, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
private fun FlashcardsView(
    allBooks: List<BookEntity>,
    isLoading: Boolean,
    generatedResult: String?,
    savedFlashcards: List<SavedFlashcard>,
    onGenerate: (String) -> Unit,
    onSaveCard: (String, String, String) -> Unit,
    onToggleMastered: (String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var bookTitle by remember { mutableStateOf(allBooks.firstOrNull()?.title ?: "Nur al-Idah") }
    var flippedCardId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "تعاملی AI فلیش کارڈز" else "Interactive AI Flashcards", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = if (isUrdu) "کتب سے اہم اصطلاحات، قواعد اور دلائل کا اعادہ کریں" else "Practice key definitions, rules, and daleel from uploaded books",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(if (isUrdu) "کتاب کا نام" else "Kitab Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(bookTitle) },
            enabled = !isLoading && bookTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "فلیش کارڈز بن رہے ہیں..." else "Creating Flashcards...") else (if (isUrdu) "$bookTitle کے فلیش کارڈز بنائیں" else "Generate Flashcards for $bookTitle"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = if (isUrdu) "تیار شدہ AI فلیش کارڈ سیٹ" else "Generated AI Flashcard Set", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = result, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(text = if (isUrdu) "محفوظ شدہ فلیش کارڈز (${savedFlashcards.size})" else "Saved Flashcards Deck (${savedFlashcards.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        savedFlashcards.forEach { card ->
            val isFlipped = flippedCardId == card.id

            Card(
                onClick = { flippedCardId = if (isFlipped) null else card.id },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (card.isMastered) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = card.bookTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        IconButton(onClick = { onToggleMastered(card.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = if (card.isMastered) Icons.Default.CheckCircle else Icons.Default.Circle,
                                contentDescription = "Mastered",
                                tint = if (card.isMastered) Color(0xFF4CAF50) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isFlipped) (if (isUrdu) "جواب و دلیل:\n${card.back}" else "ANSWER / DALEEL:\n${card.back}") else (if (isUrdu) "سوال و مفہوم:\n${card.front}" else "QUESTION / CONCEPT:\n${card.front}"),
                        fontSize = 14.sp,
                        fontWeight = if (isFlipped) FontWeight.Normal else FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isFlipped) (if (isUrdu) "سوال پر واپس جانے کے لیے ٹیپ کریں" else "Tap to flip back to question") else (if (isUrdu) "جواب دیکھنے کے لیے کارڈ پر ٹیپ کریں 🔄" else "Tap card to flip answer 🔄"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun NotesGenView(
    isLoading: Boolean,
    generatedResult: String?,
    savedNotes: List<SavedAiNote>,
    onGenerate: (String) -> Unit,
    onSaveNote: (String, String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onExportPdf: (String, String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    var topic by remember { mutableStateOf("Conditions of Prayer (شروط الصلاة) in Hanafi Fiqh") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = if (isUrdu) "AI مطالعہ نوٹس اور محفوظ مجموعہ" else "AI Study Notes & Saved Collections", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text(if (isUrdu) "نوٹ کا عنوان یا موضوع" else "Note Topic / Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(topic) },
            enabled = !isLoading && topic.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) (if (isUrdu) "نوٹس بن رہے ہیں..." else "Generating Notes...") else (if (isUrdu) "مطالعہ نوٹس تیار کریں" else "Generate Study Notes"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = if (isUrdu) "تیار شدہ نوٹس کا پیش نظارہ" else "Generated Note Preview", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = result, fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onSaveNote(topic, result) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Save")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isUrdu) "نوٹ محفوظ کریں" else "Save Note")
                        }

                        OutlinedButton(
                            onClick = { onExportPdf(topic, result) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Export")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isUrdu) "پی ڈی ایف / شیئر" else "Export PDF / Share")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(text = if (isUrdu) "آپ کے محفوظ کردہ AI نوٹس (${savedNotes.size})" else "Your Saved AI Notes (${savedNotes.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        savedNotes.forEach { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = note.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Row {
                            IconButton(onClick = { onExportPdf(note.title, note.content) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Share, contentDescription = "Share Note", modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { onDeleteNote(note.id) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Note", modifier = Modifier.size(18.dp), tint = Color.Red)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = note.content, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
