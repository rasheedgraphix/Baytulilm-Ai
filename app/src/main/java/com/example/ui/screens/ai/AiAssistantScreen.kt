package com.example.ui.screens.ai

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
import com.example.ui.viewmodel.SavedAiNote
import com.example.ui.viewmodel.SavedFlashcard

@Composable
fun AiAssistantScreen(
    aiViewModel: AiViewModel,
    mainViewModel: MainViewModel
) {
    val langCode = LocalAppLanguage.current.code
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
    val remainingAiQuestions by aiViewModel.remainingAiQuestions.collectAsStateWithLifecycle()
    val isLimitExceeded by aiViewModel.isLimitExceeded.collectAsStateWithLifecycle()

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

    // Refresh quota check on entry
    LaunchedEffect(Unit) {
        aiViewModel.refreshDailyUsageQuota()
    }

    // Text To Speech Initialization
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = if (langCode == "ur" || langCode == "ps") Locale("ur") else Locale.ENGLISH
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
                                text = when (langCode) {
                                    "ps" -> "بیت العلم AI سکالر"
                                    "ur" -> "بیت العلم AI اسکالر"
                                    else -> "Baytul Ilm AI Scholar"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = when (langCode) {
                                    "ps" -> "د کتابونو پر بنسټ اسلامي څېړنه (Gemini AI)"
                                    "ur" -> "کتب کی بنیاد پر اسلامی علم و تحقیق (Gemini AI)"
                                    else -> "RAG over Uploaded Islamic Books (Gemini 3.5)"
                                },
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (!isLimitExceeded) Color.White.copy(alpha = 0.22f) else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (!isLimitExceeded) Color.White else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = when (langCode) {
                                        "ps" -> "$remainingAiQuestions/${AiViewModel.MAX_DAILY_AI_QUESTIONS} پوښتنې"
                                        "ur" -> "$remainingAiQuestions/${AiViewModel.MAX_DAILY_AI_QUESTIONS} باقی"
                                        else -> "$remainingAiQuestions/${AiViewModel.MAX_DAILY_AI_QUESTIONS} left"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isLimitExceeded) Color.White else MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        IconButton(onClick = { aiViewModel.clearHistory() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = when (langCode) {
                                    "ps" -> "چټ پاک کړئ"
                                    "ur" -> "چیٹ صاف کریں"
                                    else -> "Clear Chat"
                                },
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
                                text = selectedBook?.let { 
                                    when (langCode) {
                                        "ps" -> "ساحه: ${it.title}"
                                        "ur" -> "دائرہ: ${it.title}"
                                        else -> "Scope: ${it.title}"
                                    } 
                                } ?: (
                                    when (langCode) {
                                        "ps" -> "ساحه: ټول کتابونه"
                                        "ur" -> "دائرہ: تمام کتب"
                                        else -> "Scope: All Library Books"
                                    }
                                ),
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
                            text = { Text(when (langCode) { "ps" -> " ټول کتابونه"; "ur" -> "تمام کتب"; else -> "All Library Books" }) },
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
                    AiMode.CHAT -> when (langCode) { "ps" -> "💬 له کتاب څخه ځیرکه خبرې اترې"; "ur" -> "💬 کتاب سے ذہین گفتگو"; else -> "💬 Smart Book Chat" }
                    AiMode.ASK_BOOK -> when (langCode) { "ps" -> "📖 ټاکل شوی کتاب"; "ur" -> "📖 منتخب کتاب"; else -> "📖 Selected Kitab" }
                    AiMode.GLOBAL_SEARCH -> when (langCode) { "ps" -> "🔍 په کتابونو کې پلټنه"; "ur" -> "🔍 کتب میں تلاش"; else -> "🔍 Global Library Search" }
                    AiMode.SUMMARIZER -> when (langCode) { "ps" -> "📝 د کتاب خلاصه"; "ur" -> "📝 کتاب کا خلاصہ"; else -> "📝 Book Summary" }
                    AiMode.TRANSLATE -> when (langCode) { "ps" -> "🌐 علمي ژباړه"; "ur" -> "🌐 ترجمہ نگاری"; else -> "🌐 Translation" }
                    AiMode.GRAMMAR -> when (langCode) { "ps" -> "✒️ ګرامر (نحو و صرف)"; "ur" -> "✒️ قواعد (نحو و صرف)"; else -> "✒️ Grammar (Nahw/Sarf)" }
                    AiMode.QUIZ -> when (langCode) { "ps" -> "❓ AI کوئز (۳۰/۵۰/۱۰۰ پوښتنې)"; "ur" -> "❓ AI کوئز (30/50/100 سوالات)"; else -> "❓ AI Quiz (30/50/100 MCQs)" }
                    AiMode.FLASHCARDS -> when (langCode) { "ps" -> "🎴 فلش کارډونه"; "ur" -> "🎴 فلیش کارڈز"; else -> "🎴 Flashcards" }
                    AiMode.NOTES -> when (langCode) { "ps" -> "📌 AI یادښتونه"; "ur" -> "📌 AI نوٹس"; else -> "📌 AI Notes" }
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
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (langCode == "ur" || langCode == "ps") langCode else "ar")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, when (langCode) { "ps" -> "په عربي، پښتو یا انګلیسي ژبه پوښتنه وکړئ..."; "ur" -> "عربی، اردو یا انگریزی میں سوال بولیں..."; else -> "Speak question in Arabic, Urdu, or English..." })
                        }
                        try {
                            speechLauncher.launch(intent)
                        } catch (e: Exception) {
                            // Fallback if speech intent not available
                        }
                    },
                    onSend = { prompt, imageBitmap ->
                        aiViewModel.sendRAGMessage(prompt, allBooks, imageBitmap)
                        inputText = ""
                    },
                    onSpeak = { text ->
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    onCopy = { text ->
                        clipboardManager.setText(AnnotatedString(text))
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
    onSend: (String, Bitmap?) -> Unit,
    onSpeak: (String) -> Unit,
    onCopy: (String) -> Unit,
    onRegenerate: () -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    val context = LocalContext.current
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selectedImageBitmap = bitmap
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, when (langCode) { "ps" -> "کمره ونه پرانیستل شوه"; "ur" -> "کیمرہ نہ کھل سکا"; else -> "Could not launch camera" }, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, when (langCode) { "ps" -> "د عکس اخیستلو لپاره د کمرې اجازه اړینه ده"; "ur" -> "تصویر لینے کے لیے کیمرہ اجازت ضروری ہے"; else -> "Camera permission required to take photo" }, Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            runCatching {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageBitmap = bitmap
            }.onFailure {
                Toast.makeText(context, when (langCode) { "ps" -> "عکس پورته نشو"; "ur" -> "تصویر اپ لوڈ نہ ہو سکی"; else -> "Failed to load image" }, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val quickPrompts = when (langCode) {
        "ps" -> listOf(
            "د بیت العلم AI کارولو طریقه",
            "په حنفي فقه کې د اودس فرائض او شرائط",
            "د وحی تعریف او ډولونه څه دي؟",
            "په کوئز ماډیول کې کومې ځانګړتیاوې دي؟",
            "د علم نحو او علم صرف ترمنځ څه توپیر دی؟",
            "د جوړوونکي او اړیکو تفصیلات"
        )
        "ur" -> listOf(
            "بیت العلم AI ایپ استعمال کرنے کا طریقہ",
            "حنفی فقہ میں وضو کے فرائض و شرائط بیان کریں",
            "وحی کی تعریف اور اقسام کیا ہیں؟",
            "کوئز ماڈیول میں کیا خصوصیات ہیں؟",
            "علمِ نحو اور علمِ صرف میں کیا فرق ہے؟",
            "ڈویلپر اور رابطہ کی تفصیلات"
        )
        else -> listOf(
            "How to use Baytul Ilm AI app?",
            "Explain conditions of Wudu in Hanafi Fiqh",
            "Define Wahi and its categories",
            "What features are in the Quiz module?",
            "Difference between Nahw and Sarf",
            "Developer info & Swabi contact details"
        )
    }

    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = {
                Text(
                    text = when (langCode) { "ps" -> "عکس زیات کړئ (Image Input)"; "ur" -> "تصویر شامل کریں (Image Input)"; else -> "Attach Image to AI Scholar" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = when (langCode) { "ps" -> "د کتاب د پاڼې، یادښت یا پوښتنې عکس له کمرې واخلئ یا له ګیلرۍ وټاکئ."; "ur" -> "کتاب کے صفحے، نوٹ یا سوال کی تصویر کیمرے سے لیں یا گیلری سے منتخب کریں۔"; else -> "Take a photo of a book page, handwritten text, or question using camera or choose from gallery." },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Card(
                        onClick = {
                            showImagePickerDialog = false
                            val hasPerm = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            if (hasPerm) {
                                try {
                                    cameraLauncher.launch(null)
                                } catch (e: Exception) {
                                    Toast.makeText(context, when (langCode) { "ps" -> "کمره خلاصه نشوه"; "ur" -> "کیمرہ نہ کھل سکا"; else -> "Camera error" }, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = when (langCode) { "ps" -> "📷 له کمرې عکس واخلئ (Take Photo)"; "ur" -> "📷 کیمرہ سے تصویر لیں (Take Photo)"; else -> "📷 Take Photo (Camera)" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Card(
                        onClick = {
                            showImagePickerDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = when (langCode) { "ps" -> "🖼️ له ګیلرۍ څخه وټاکئ (Select from Gallery)"; "ur" -> "🖼️ گیلری سے منتخب کریں (Select from Gallery)"; else -> "🖼️ Select from Gallery" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImagePickerDialog = false }) {
                    Text(when (langCode) { "ps" -> "لغوه"; "ur" -> "منسوخ"; else -> "Cancel" })
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick Suggestions Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { prompt ->
                Card(
                    onClick = { onSend(prompt, null) },
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
                                msg.imageBitmap?.let { bmp ->
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = "User Attached Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 200.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }

                                val displayText = if ((langCode == "ur" || langCode == "ps") && (msg.text.startsWith("Assalamu Alaikum") || msg.text.startsWith("السلام علیکم"))) {
                                    if (langCode == "ps")
                                        "السلام علیکم! زه د بیت العلم AI سکالر یم. زه ستاسو له پورته شویو کتابونو څخه د دقیقو حوالو سره ځواب درکوم!"
                                    else
                                        "السلام علیکم! میں بیت العلم AI اسکالر ہوں۔ میں آپ کی اپ لوڈ کردہ اسلامی کتب کی لائبریری سے تصدیق شدہ صفحہ نمبر کے حوالہ جات کے ساتھ جواب دیتا ہوں!"
                                } else {
                                    msg.text
                                }

                                if (displayText.isNotBlank()) {
                                    Text(
                                        text = displayText,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }

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
                                                    text = if (isGeminiGeneral) {
                                                        when (langCode) { "ps" -> "⚡ سرچینه: عام اسلامي علم (Gemini AI)"; "ur" -> "⚡ ماخذ: عمومی اسلامی علم (Gemini AI)"; else -> "⚡ Source: Gemini General Islamic Knowledge" }
                                                    } else {
                                                        when (langCode) { "ps" -> "📖 سرچینه: د بیت العلم کتابتون"; "ur" -> "📖 ماخذ: بیت العلم مکتبہ کتب"; else -> "📖 Source: Uploaded Dars-e-Nizami Library" }
                                                    },
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isGeminiGeneral) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                    text = when (langCode) { "ps" -> "باور: ${cite.confidenceScore}"; "ur" -> "اعتماد: ${cite.confidenceScore}"; else -> "Confidence: ${cite.confidenceScore}" },
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            if (isGeminiGeneral) {
                                                Text(
                                                    text = when (langCode) { "ps" -> "دا ځواب له کتابتون پر ځای له عام AI څخه اخیستل شوی دی."; "ur" -> "یہ جواب اپ لوڈ کی گئی نصابی کتب کے بجائے عمومی AI سے لیا گیا ہے۔"; else -> "This answer is not taken from the uploaded library books." },
                                                    fontSize = 11.sp,
                                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            } else {
                                                Text(
                                                    text = when (langCode) { "ps" -> "کتاب: ${cite.bookName ?: "ډیجیټل کتاب"}"; "ur" -> "کتاب: ${cite.bookName ?: "ڈیجیٹل لائبریری کتاب"}"; else -> "Book: ${cite.bookName ?: "Digital Library Book"}" },
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = when (langCode) { "ps" -> "درجه: ${cite.darja ?: "نشته"} | مضمون: ${cite.subject ?: "نشته"} | ${if (cite.pageNumber != null) "پاڼه ${cite.pageNumber}" else "پاڼه ۱"}"; "ur" -> "درجہ: ${cite.darja ?: "غیر موجود"} | مضمون: ${cite.subject ?: "غیر موجود"} | ${if (cite.pageNumber != null) "صفحہ ${cite.pageNumber}" else "صفحہ 1"}"; else -> "Darja: ${cite.darja ?: "N/A"} | Subject: ${cite.subject ?: "N/A"} | ${cite.pageNumber ?: "Page 1"}" },
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
                            text = when (langCode) { "ps" -> "بیت العلم AI سکالر په کتابونو کې د ځواب پلټنه کوي..."; "ur" -> "بیت العلم AI اسکالر کتب میں تلاش اور جواب تیار کر رہا ہے..."; else -> "Baytul Ilm AI Scholar searching uploaded books & generating response..." },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Attached Image Preview Card
        selectedImageBitmap?.let { bmp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Preview Image",
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (langCode) { "ps" -> "📷 عکس ضمیمه شو"; "ur" -> "📷 تصویر منسلک ہے"; else -> "📷 Image Attached" },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = when (langCode) { "ps" -> "بیت العلم AI سکالر به دغه عکس وڅېړي"; "ur" -> "بیت العلم AI اسکالر اس تصویر کا تجزیہ کرے گا"; else -> "AI Scholar will analyze this image" },
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { selectedImageBitmap = null },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Daily Limit Exceeded Notice Banner
        val isChatLimitReached by aiViewModel.isLimitExceeded.collectAsStateWithLifecycle()
        if (isChatLimitReached) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "آپ آج کے ${AiViewModel.MAX_DAILY_AI_QUESTIONS} AI سوالات مکمل کر چکے ہیں۔ کل دوبارہ کوشش کریں۔",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
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
                    onClick = { showImagePickerDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Attach Image",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

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
                    placeholder = { Text(when (langCode) { "ps" -> "عکس واستوئ یا پوښتنه وکړئ..."; "ur" -> "تصویر بھیجیں یا سوال پوچھیں..."; else -> "Send photo or ask question..." }, fontSize = 13.sp) },
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
                        if ((inputText.isNotBlank() || selectedImageBitmap != null) && !isLoading) {
                            onSend(inputText, selectedImageBitmap)
                            selectedImageBitmap = null
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
    val langCode = LocalAppLanguage.current.code
    var searchQuery by remember { mutableStateOf("حكم المسح على الخفين") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("global_search_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = aiText("global_search_sub", langCode, allBooks.size),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(aiText("search_label", langCode)) },
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
            Text(if (isLoading) aiText("searching", langCode) else aiText("search_btn", langCode))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = aiText("lib_coverage", langCode), fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                        Text(text = aiText("darja_subj_fmt", langCode, book.darja, book.subject), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(text = aiText("pages_fmt", langCode, book.pageCount), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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
    val langCode = LocalAppLanguage.current.code
    var bookTitle by remember { mutableStateOf(selectedBook?.title ?: allBooks.firstOrNull()?.title ?: "Al-Hidayah") }
    var summaryType by remember { mutableStateOf(aiText("short_sum", langCode)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("sum_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(aiText("sum_label", langCode)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val summaryOptions = listOf(
                aiText("short_sum", langCode),
                aiText("detailed_sum", langCode),
                aiText("exam_sum", langCode),
                aiText("revision_notes", langCode)
            )
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
            Text(if (isLoading) aiText("gen_sum_ing", langCode) else aiText("gen_sum_btn", langCode, summaryType))
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
    val langCode = LocalAppLanguage.current.code
    var textToTranslate by remember { mutableStateOf("المجلس الأول في بيان أصول الفقه") }
    var srcLang by remember { mutableStateOf("Arabic") }
    var targetLang by remember { mutableStateOf("Urdu") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("trans_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = srcLang == "Arabic" && targetLang == "Urdu", onClick = { srcLang = "Arabic"; targetLang = "Urdu" }, label = { Text(aiText("ar_ur", langCode)) })
            FilterChip(selected = srcLang == "Arabic" && targetLang == "English", onClick = { srcLang = "Arabic"; targetLang = "English" }, label = { Text(aiText("ar_en", langCode)) })
            FilterChip(selected = srcLang == "Urdu" && targetLang == "English", onClick = { srcLang = "Urdu"; targetLang = "English" }, label = { Text(aiText("ur_en", langCode)) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = textToTranslate,
            onValueChange = { textToTranslate = it },
            label = { Text(aiText("trans_label", langCode)) },
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
            Text(if (isLoading) aiText("trans_ing", langCode) else aiText("trans_btn", langCode))
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
    val langCode = LocalAppLanguage.current.code
    var sentence by remember { mutableStateOf("العلم نور والجهل تارك للحق") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("grammar_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = aiText("grammar_sub", langCode),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = sentence,
            onValueChange = { sentence = it },
            label = { Text(aiText("grammar_label", langCode)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAnalyze(sentence) },
            enabled = !isLoading && sentence.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) aiText("grammar_ing", langCode) else aiText("grammar_btn", langCode))
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
    val langCode = LocalAppLanguage.current.code
    var bookTitle by remember { mutableStateOf(allBooks.firstOrNull()?.title ?: "Kanz al-Daqaiq") }
    var countOption by remember { mutableStateOf(aiText("mcq_30", langCode)) }
    var difficulty by remember { mutableStateOf(aiText("medium", langCode)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("quiz_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = aiText("quiz_sub", langCode),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(aiText("book_title_lbl", langCode)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = aiText("quiz_size", langCode), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val counts = listOf(aiText("mcq_30", langCode), aiText("mcq_50", langCode), aiText("mcq_100", langCode))
            counts.forEach { count ->
                FilterChip(selected = countOption == count, onClick = { countOption = count }, label = { Text(count) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = aiText("diff_lbl", langCode), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val diffs = listOf(aiText("easy", langCode), aiText("medium", langCode), aiText("hard", langCode), aiText("exam_level", langCode))
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
            Text(if (isLoading) aiText("quiz_ing", langCode) else aiText("quiz_btn", langCode, countOption))
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
    val langCode = LocalAppLanguage.current.code
    var bookTitle by remember { mutableStateOf(allBooks.firstOrNull()?.title ?: "Nur al-Idah") }
    var flippedCardId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("fc_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = aiText("fc_sub", langCode),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(aiText("fc_label", langCode)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(bookTitle) },
            enabled = !isLoading && bookTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) aiText("fc_ing", langCode) else aiText("fc_btn", langCode, bookTitle))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = aiText("fc_preview", langCode), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = result, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(text = aiText("fc_saved", langCode, savedFlashcards.size), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                        text = if (isFlipped) aiText("fc_ans", langCode, card.back) else aiText("fc_q", langCode, card.front),
                        fontSize = 14.sp,
                        fontWeight = if (isFlipped) FontWeight.Normal else FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isFlipped) aiText("fc_tap_back", langCode) else aiText("fc_tap_flip", langCode),
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
    onDeleteNote: (String) -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    var topic by remember { mutableStateOf("Conditions of Prayer (شروط الصلاة) in Hanafi Fiqh") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = aiText("notes_title", langCode), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text(aiText("note_topic_lbl", langCode)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onGenerate(topic) },
            enabled = !isLoading && topic.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) aiText("notes_ing", langCode) else aiText("notes_btn", langCode))
        }

        Spacer(modifier = Modifier.height(16.dp))

        generatedResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = aiText("note_prev", langCode), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = result, fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onSaveNote(topic, result) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(aiText("save_note", langCode))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(text = aiText("saved_notes_count", langCode, savedNotes.size), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                        IconButton(onClick = { onDeleteNote(note.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note", modifier = Modifier.size(18.dp), tint = Color.Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = note.content, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private fun aiText(key: String, langCode: String, vararg args: Any): String {
    return when (langCode) {
        "ps" -> when (key) {
            "global_search_title" -> "په ټولو کتابونو کې AI پلټنه"
            "global_search_sub" -> "د کتابتون په ټولو ${args[0]} کتابونو کې د صفحو له دقیقو حوالو سره پلتنه وکړئ."
            "search_label" -> "د پلټنې کلیدي کلمه، فقهي مسأله یا حدیث داخل کړئ"
            "searching" -> "په ټولو کتابونو کې پلټنه روانه ده..."
            "search_btn" -> "په کتابونو کې پلټنه وکړئ"
            "lib_coverage" -> "د کتابتون تر پوښښ لاندې:"
            "darja_subj_fmt" -> "درجه: ${args[0]} • مضمون: ${args[1]}"
            "pages_fmt" -> "${args[0]} پاڼې"
            "short_sum" -> "لنډه خلاصه"
            "detailed_sum" -> "مفصله خلاصه"
            "exam_sum" -> "امتحاني خلاصه"
            "revision_notes" -> "د یادښتونو اعاده"
            "sum_title" -> "د کتاب او څپرکو خلاصه کوونکی"
            "sum_label" -> "د کتاب یا څپرکي نوم"
            "gen_sum_ing" -> "خلاصه چمتو کیږي..."
            "gen_sum_btn" -> "${args[0]} چمتو کړئ"
            "trans_title" -> "علمي ژباړې اوزار"
            "ar_ur" -> "عربي ← اردو"
            "ar_en" -> "عربي ← انګلیسي"
            "ur_en" -> "اردو ← انګلیسي"
            "trans_label" -> "د ژباړې لپاره عبارت ولیکئ"
            "trans_ing" -> "ژباړه کیږي..."
            "trans_btn" -> "د عبارت ژباړه وکړئ"
            "grammar_title" -> "د علم نحو او صرف حالت (ترکیب او اعراب)"
            "grammar_sub" -> "د کلمو ترکیب، اعراب، صیغه او پښتو/اردو تشریح"
            "grammar_label" -> "عربي جمله ولیکئ"
            "grammar_ing" -> "ترکیب او تحلیل کیږي..."
            "grammar_btn" -> "ترکیب او اعراب وګورئ"
            "quiz_title" -> "AI کوئز او ازموینې جوړوونکی"
            "quiz_sub" -> "له پورته شویو کتابونو څخه ۳۰، ۵۰ یا ۱۰۰ پوښتنې له حوالو سره جوړې کړئ"
            "book_title_lbl" -> "د کتاب نوم"
            "quiz_size" -> "د کوئز اندازه:"
            "diff_lbl" -> "د سختۍ کچه:"
            "mcq_30" -> "۳۰ پوښتنې"
            "mcq_50" -> "۵۰ پوښتنې"
            "mcq_100" -> "۱۰۰ پوښتنې"
            "easy" -> "اسانه"
            "medium" -> "منځنی"
            "hard" -> "سخت"
            "exam_level" -> "امتحاني کچه"
            "quiz_ing" -> "کوئز جوړیږي..."
            "quiz_btn" -> "${args[0]} جوړ کړئ"
            "fc_title" -> "تعاملي AI فلش کارډونه"
            "fc_sub" -> "له کتابونو څخه د مهمو اصطلاحاتو او دلایلو تمرین وکړئ"
            "fc_label" -> "د کتاب نوم"
            "fc_ing" -> "فلش کارډونه جوړیږي..."
            "fc_btn" -> "د ${args[0]} لپاره فلش کارډونه جوړ کړئ"
            "fc_preview" -> "چمتو شوي AI فلش کارډونه"
            "fc_saved" -> "خوندي شوي فلش کارډونه (${args[0]})"
            "fc_ans" -> "ځواب او دلیل:\n${args[0]}"
            "fc_q" -> "پوښتنه او مفهوم:\n${args[0]}"
            "fc_tap_back" -> "پوښتنې ته د ستنېدو لپاره ټپ کړئ"
            "fc_tap_flip" -> "د ځواب لیدلو لپاره کارت وڅرخوئ 🔄"
            "notes_title" -> "د AI درسي یادښتونه او مجموعه"
            "note_topic_lbl" -> "د یادښت موضوع یا عنوان"
            "notes_ing" -> "یادښتونه جوړیږي..."
            "notes_btn" -> "درسي یادښتونه جوړ کړئ"
            "note_prev" -> "د چمتو شویو یادښتونو مخکتنه"
            "save_note" -> "یادښت خوندي کړئ"
            "pdf_share" -> "پی ډی ایف / شیر کړئ"
            "saved_notes_count" -> "ستاسو خوندي شوي AI یادښتونه (${args[0]})"
            else -> ""
        }
        "ur" -> when (key) {
            "global_search_title" -> "تمام کتب میں AI تلاش"
            "global_search_sub" -> "لائبریری کی تمام ${args[0]} کتابوں میں صفحہ وار حوالوں کے ساتھ تلاش کریں۔"
            "search_label" -> "تلاش کا لفظ، فقهی مسئلہ یا حدیث درج کریں"
            "searching" -> "تلاش جاری ہے..."
            "search_btn" -> "کتب میں تلاش کریں"
            "lib_coverage" -> "مکتبہ کا احاطہ:"
            "darja_subj_fmt" -> "درجہ: ${args[0]} • مضمون: ${args[1]}"
            "pages_fmt" -> "${args[0]} صفحات"
            "short_sum" -> "مختصر خلاصہ"
            "detailed_sum" -> "مفصل خلاصہ"
            "exam_sum" -> "امتحانی خلاصہ"
            "revision_notes" -> "دہرائی نوٹس"
            "sum_title" -> "کتاب اور ابواب کا خلاصہ نگار"
            "sum_label" -> "کتاب یا باب کا نام"
            "gen_sum_ing" -> "خلاصہ تیار ہو رہا ہے..."
            "gen_sum_btn" -> "${args[0]} تیار کریں"
            "trans_title" -> "علمی ترجمہ نگاری ٹول"
            "ar_ur" -> "عربی ← اردو"
            "ar_en" -> "عربی ← انگریزی"
            "ur_en" -> "اردو ← انگریزی"
            "trans_label" -> "ترجمہ کے لیے عبارت تحریر کریں"
            "trans_ing" -> "ترجمہ ہو رہا ہے..."
            "trans_btn" -> "عبارت کا ترجمہ کریں"
            "grammar_title" -> "علمِ نحو و صرف (ترکیب و اعراب)"
            "grammar_sub" -> "الفاظ کی ترکیب، اعراب، صیغہ، مادّہ اور اردو وضاحت"
            "grammar_label" -> "عربی جملہ تحریر کریں"
            "grammar_ing" -> "ترکیب کی جا رہی ہے..."
            "grammar_btn" -> "ترکیب و اعراب معائن کریں"
            "quiz_title" -> "AI کوئز و امتحان جنریٹر"
            "quiz_sub" -> "اپ لوڈ کی گئی کتب سے 30، 50 یا 100 سوالات بمعہ حوالہ جات تیار کریں"
            "book_title_lbl" -> "کتاب کا نام"
            "quiz_size" -> "کوئز کا سائز:"
            "diff_lbl" -> "مشکل کی سطح:"
            "mcq_30" -> "30 سوالات"
            "mcq_50" -> "50 سوالات"
            "mcq_100" -> "100 سوالات"
            "easy" -> "آسان"
            "medium" -> "درمیانی"
            "hard" -> "مشکل"
            "exam_level" -> "امتحانی سطح"
            "quiz_ing" -> "کوئز تیار ہو رہا ہے..."
            "quiz_btn" -> "${args[0]} تیار کریں"
            "fc_title" -> "تعاملی AI فلیش کارڈز"
            "fc_sub" -> "کتب سے اہم اصطلاحات، قواعد اور دلائل کا اعادہ کریں"
            "fc_label" -> "کتاب کا نام"
            "fc_ing" -> "فلیش کارڈز بن رہے ہیں..."
            "fc_btn" -> "${args[0]} کے فلیش کارڈز بنائیں"
            "fc_preview" -> "تیار شدہ AI فلیش کارڈ سیٹ"
            "fc_saved" -> "محفوظ شدہ فلیش کارڈز (${args[0]})"
            "fc_ans" -> "جواب و دلیل:\n${args[0]}"
            "fc_q" -> "سوال و مفہوم:\n${args[0]}"
            "fc_tap_back" -> "سوال پر واپس جانے کے لیے ٹیپ کریں"
            "fc_tap_flip" -> "جواب دیکھنے کے لیے کارڈ پر ٹیپ کریں 🔄"
            "notes_title" -> "AI مطالعہ نوٹس اور محفوظ مجموعہ"
            "note_topic_lbl" -> "نوٹ کا عنوان یا موضوع"
            "notes_ing" -> "نوٹس بن رہے ہیں..."
            "notes_btn" -> "مطالعہ نوٹس تیار کریں"
            "note_prev" -> "تیار شدہ نوٹس کا پیش نظارہ"
            "save_note" -> "نوٹ محفوظ کریں"
            "pdf_share" -> "پی ڈی ایف / شیئر"
            "saved_notes_count" -> "آپ کے محفوظ کردہ AI نوٹس (${args[0]})"
            else -> ""
        }
        else -> when (key) {
            "global_search_title" -> "Global AI Search Across All Uploaded Books"
            "global_search_sub" -> "Search across all ${args[0]} books in library with verifiable Book, Darja, Chapter, Page citations."
            "search_label" -> "Search Keyword, Fiqh Mas'alah, or Hadith"
            "searching" -> "Searching All Books..."
            "search_btn" -> "Search Library Books"
            "lib_coverage" -> "Library Coverage:"
            "darja_subj_fmt" -> "Darja: ${args[0]} • Subject: ${args[1]}"
            "pages_fmt" -> "${args[0]} Pages"
            "short_sum" -> "Short Summary"
            "detailed_sum" -> "Detailed Summary"
            "exam_sum" -> "Exam Summary"
            "revision_notes" -> "Revision Notes"
            "sum_title" -> "Kitab & Chapter Summarizer"
            "sum_label" -> "Book / Chapter Name"
            "gen_sum_ing" -> "Generating Summary..."
            "gen_sum_btn" -> "Generate ${args[0]}"
            "trans_title" -> "Scholarly Translation Tool"
            "ar_ur" -> "Arabic → Urdu"
            "ar_en" -> "Arabic → English"
            "ur_en" -> "Urdu → English"
            "trans_label" -> "Enter Text to Translate"
            "trans_ing" -> "Translating..."
            "trans_btn" -> "Translate Text"
            "grammar_title" -> "Nahw & Sarf Grammar Mode (ترکیب و اعراب)"
            "grammar_sub" -> "Word, Root, Arabic Parsing (تركيب), I'rab (إعراب), Translation & Explanation"
            "grammar_label" -> "Enter Arabic Sentence"
            "grammar_ing" -> "Analyzing Grammar..."
            "grammar_btn" -> "Analyze Grammar (ترکیب و اعراب)"
            "quiz_title" -> "AI Quiz & Exam Generator"
            "quiz_sub" -> "Generate 30, 50, or 100 MCQs directly from uploaded books with correct answers & citations"
            "book_title_lbl" -> "Book Title"
            "quiz_size" -> "Quiz Size:"
            "diff_lbl" -> "Difficulty Level:"
            "mcq_30" -> "30 MCQs"
            "mcq_50" -> "50 MCQs"
            "mcq_100" -> "100 MCQs"
            "easy" -> "Easy"
            "medium" -> "Medium"
            "hard" -> "Hard"
            "exam_level" -> "Exam Level"
            "quiz_ing" -> "Generating Quiz..."
            "quiz_btn" -> "Generate ${args[0]}"
            "fc_title" -> "Interactive AI Flashcards"
            "fc_sub" -> "Practice key definitions, rules, and daleel from uploaded books"
            "fc_label" -> "Kitab Name"
            "fc_ing" -> "Creating Flashcards..."
            "fc_btn" -> "Generate Flashcards for ${args[0]}"
            "fc_preview" -> "Generated AI Flashcard Set"
            "fc_saved" -> "Saved Flashcards Deck (${args[0]})"
            "fc_ans" -> "ANSWER / DALEEL:\n${args[0]}"
            "fc_q" -> "QUESTION / CONCEPT:\n${args[0]}"
            "fc_tap_back" -> "Tap to flip back to question"
            "fc_tap_flip" -> "Tap card to flip answer 🔄"
            "notes_title" -> "AI Study Notes & Saved Collections"
            "note_topic_lbl" -> "Note Topic / Title"
            "notes_ing" -> "Generating Notes..."
            "notes_btn" -> "Generate Study Notes"
            "note_prev" -> "Generated Note Preview"
            "save_note" -> "Save Note"
            "pdf_share" -> "Export PDF / Share"
            "saved_notes_count" -> "Your Saved AI Notes (${args[0]})"
            else -> ""
        }
    }
}
