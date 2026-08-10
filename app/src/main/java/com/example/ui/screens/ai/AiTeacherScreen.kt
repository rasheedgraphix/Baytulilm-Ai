package com.example.ui.screens.ai

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookEntity
import com.example.ui.viewmodel.AiViewModel
import com.example.ui.viewmodel.MainViewModel
import java.util.Locale

data class TeacherTool(
    val id: String,
    val name: String,
    val icon: String,
    val description: String,
    val category: String,
    val badge: String = "AI Powered"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTeacherScreen(
    aiViewModel: AiViewModel,
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val allBooks by mainViewModel.allBooks.collectAsState()
    val isLoading by aiViewModel.isLoading.collectAsState()
    val generatedResult by aiViewModel.generatedResult.collectAsState()

    var selectedTool by remember { mutableStateOf<TeacherTool?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0: AI Teachers, 1: Voice Learning, 2: Smart Study, 3: Analytics

    var inputPrompt by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("Urdu") }
    var voiceOutputText by remember { mutableStateOf("") }

    // TTS Setup
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(Unit) {
        val speech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS initialized successfully
            }
        }
        tts = speech
        onDispose {
            speech.stop()
            speech.shutdown()
        }
    }

    // Voice Recognizer Launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull() ?: ""
            if (spokenText.isNotBlank()) {
                inputPrompt = spokenText
                selectedTool?.let { tool ->
                    aiViewModel.sendRAGMessage("AI Teacher Mode [${tool.name}]: $spokenText (Language: $selectedLanguage)", allBooks)
                }
            }
        }
    }

    val teacherTools = listOf(
        TeacherTool("quran", "📖 AI Quran Teacher", "📖", "Ayat explanation, word-by-word translation, I'rab, Tafseer & audio reading.", "Quranic Studies", "Interactive"),
        TeacherTool("hadith", "📚 AI Hadith Teacher", "📚", "Hadith explanation, narrators, authenticity grading & reference analysis.", "Hadith Studies", "Authentic"),
        TeacherTool("fiqh", "⚖️ AI Fiqh Teacher", "⚖️", "Fiqh rulings from uploaded Dars-e-Nizami books with Gemini fallback notice.", "Jurisprudence", "RAG First"),
        TeacherTool("aqeedah", "🕌 AI Aqeedah Teacher", "🕌", "Sunni Aqeedah, attributes of Allah, prophethood & Kalam principles.", "Theology", "Scholarly"),
        TeacherTool("tafseer", "📜 AI Tafseer Teacher", "📜", "Verse-by-verse commentary, historical background & classical tafseer.", "Exegesis", "Deep Study"),
        TeacherTool("nahw", "📘 AI Nahw Teacher", "📘", "Arabic grammar, I'rab breakdown, Tarkeeb, verb/noun sentence analysis.", "Grammar", "Interactive"),
        TeacherTool("sarf", "📗 AI Sarf Teacher", "📗", "Morphology, root word detection, verb patterns (Abwab) & derived forms.", "Morphology", "Mastery"),
        TeacherTool("balaghat", "📝 AI Balaghat Teacher", "📝", "Arabic rhetoric, Ma'ani, Badi' & Bayan beauty in Quranic eloquence.", "Rhetoric", "Advanced"),
        TeacherTool("speaking", "🎙️ AI Speaking Teacher", "🎙️", "Practice spoken Arabic & Urdu with real-time AI audio feedback.", "Linguistics", "Voice AI"),
        TeacherTool("revision", "🧠 AI Revision Teacher", "🧠", "Generate summaries, key revision points, expected questions & notes.", "Study Aid", "Smart Exam"),
        TeacherTool("exam", "🎓 AI Exam Teacher", "🎓", "Generate model papers, short/long questions, MCQs & practice tests.", "Exam Prep", "Auto Test")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AI Teacher & Learning System", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Smart Dars-e-Nizami Interactive Tutor", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("AI Teachers", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Voice Tutor", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Smart Study", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Analytics", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            when (selectedTab) {
                0 -> TeachersView(
                    tools = teacherTools,
                    selectedTool = selectedTool,
                    onSelectTool = { selectedTool = it },
                    inputPrompt = inputPrompt,
                    onPromptChange = { inputPrompt = it },
                    isLoading = isLoading,
                    generatedResult = generatedResult,
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    onSend = { prompt ->
                        val activeTool = selectedTool ?: teacherTools.first()
                        aiViewModel.sendRAGMessage(
                            prompt = "AI Teacher Mode [${activeTool.name}]: $prompt (Target Language: $selectedLanguage)",
                            libraryBooks = allBooks
                        )
                    },
                    onVoiceRecord = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your Islamic question...")
                        }
                        try {
                            voiceLauncher.launch(intent)
                        } catch (e: Exception) {
                            // Voice input not supported on device
                        }
                    },
                    onSpeakText = { text ->
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AITeacherTTS")
                    }
                )
                1 -> VoiceTutorView(
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    voiceOutputText = voiceOutputText,
                    onSpeak = { text -> tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "VoiceTutor") },
                    onVoiceRecord = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask your question in $selectedLanguage")
                        }
                        try {
                            voiceLauncher.launch(intent)
                        } catch (e: Exception) {}
                    }
                )
                2 -> SmartStudyModeView()
                3 -> LearningAnalyticsView()
            }
        }
    }
}

@Composable
private fun TeachersView(
    tools: List<TeacherTool>,
    selectedTool: TeacherTool?,
    onSelectTool: (TeacherTool) -> Unit,
    inputPrompt: String,
    onPromptChange: (String) -> Unit,
    isLoading: Boolean,
    generatedResult: String?,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    onSend: (String) -> Unit,
    onVoiceRecord: () -> Unit,
    onSpeakText: (String) -> Unit
) {
    val activeTool = selectedTool ?: tools.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Select AI Teacher Subject", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tools) { tool ->
                    val isSelected = activeTool.id == tool.id
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .clickable { onSelectTool(tool) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(tool.icon, fontSize = 22.sp)
                                Surface(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(tool.badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(tool.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(tool.category, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(activeTool.icon, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(activeTool.name, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(activeTool.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Language:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                        listOf("Urdu", "Arabic", "English", "Roman Urdu").forEach { lang ->
                            FilterChip(
                                selected = selectedLanguage == lang,
                                onClick = { onLanguageChange(lang) },
                                label = { Text(lang, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputPrompt,
                        onValueChange = onPromptChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ask ${activeTool.name} (e.g. explain, analyze, I'rab, references)...") },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = onVoiceRecord) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (inputPrompt.isNotBlank()) {
                                onSend(inputPrompt)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Teacher Consulting Books & Gemini...")
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Consult ${activeTool.name}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        generatedResult?.let { result ->
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("🎓 ${activeTool.name} Lesson", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { onSpeakText(result) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(result, fontSize = 13.sp, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceTutorView(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    voiceOutputText: String,
    onSpeak: (String) -> Unit,
    onVoiceRecord: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onVoiceRecord() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Speak", modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Tap Microphone to Ask Voice Question", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Supports Urdu, Arabic, English & Roman Urdu", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Urdu", "Arabic", "English", "Roman Urdu").forEach { lang ->
                FilterChip(
                    selected = selectedLanguage == lang,
                    onClick = { onLanguageChange(lang) },
                    label = { Text(lang) }
                )
            }
        }

        if (voiceOutputText.isNotBlank()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Voice Response:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(voiceOutputText, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = { onSpeak(voiceOutputText) }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Play Audio")
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartStudyModeView() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Smart Study Mode & Goals", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎯 Personal Learning Goals", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    GoalProgressRow("Daily Reading Goal", 45, 60, "mins")
                    GoalProgressRow("Weekly Quiz Goal", 8, 10, "quizzes")
                    GoalProgressRow("Monthly Revision Target", 18, 20, "chapters")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚠️ Weak Topics Auto-Detected", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Nahw Sentence Tarkeeb (Accuracy 62%) - Recommended: Al-Ajrumiyyah Chapter 3", fontSize = 12.sp)
                    Text("• Fiqh Taharah Water Types (Accuracy 68%) - Recommended: Nur al-Idah Section 2", fontSize = 12.sp)
                    Text("• Hadith Sanad Terminology (Accuracy 71%) - Recommended: Nukhbat al-Fikar", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun GoalProgressRow(title: String, current: Int, target: Int, unit: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("$current / $target $unit", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { current.toFloat() / target.toFloat() },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
private fun LearningAnalyticsView() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Student Learning Analytics", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricStatCard("Reading Time", "42.5 hrs", Icons.Default.Timer, Modifier.weight(1f))
                MetricStatCard("Quiz Accuracy", "88.4%", Icons.Default.Quiz, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricStatCard("Books Completed", "14 Books", Icons.Default.Book, Modifier.weight(1f))
                MetricStatCard("Revision Progress", "92%", Icons.Default.CheckCircle, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MetricStatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
