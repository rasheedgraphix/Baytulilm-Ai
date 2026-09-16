package com.example.ui.screens.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChapterProgress
import com.example.data.model.ComprehensiveQuizQuestion
import com.example.data.model.DifficultyLevel
import com.example.data.repository.ChapterResultOutcome
import com.example.data.repository.DarsENizamiQuizGenerator
import com.example.ui.viewmodel.QuizViewModel
import com.example.ui.viewmodel.QuizViewState
import com.example.util.LocalAppLanguage

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onNavigate: (String) -> Unit = {}
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
    val chapterProgressMap by viewModel.chapterProgressMap.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (viewState) {
            QuizViewState.DIFFICULTY_SELECTION -> {
                DifficultySelectionView(
                    chapterProgressMap = chapterProgressMap,
                    onSelectDifficulty = { difficulty ->
                        viewModel.selectDifficulty(difficulty)
                    }
                )
            }

            QuizViewState.CHAPTER_LIST -> {
                val progressList = chapterProgressMap[selectedDifficulty.id] ?: viewModel.getChapterProgressList(selectedDifficulty.id)
                ChapterListView(
                    difficulty = selectedDifficulty,
                    chapters = progressList,
                    onChapterClick = { chapterNum ->
                        viewModel.selectChapter(chapterNum)
                    },
                    onBackClick = {
                        viewModel.goBackToDifficultySelection()
                    }
                )
            }

            QuizViewState.ACTIVE_QUIZ -> {
                ActiveQuizView(
                    viewModel = viewModel,
                    onBackClick = {
                        viewModel.goBackToChapterList()
                    }
                )
            }

            QuizViewState.QUIZ_RESULT -> {
                val outcome by viewModel.lastChapterOutcome.collectAsStateWithLifecycle()
                QuizResultView(
                    viewModel = viewModel,
                    outcome = outcome,
                    onRetryClick = {
                        viewModel.retryChapter()
                    },
                    onNextChapterClick = {
                        val currentCh = viewModel.selectedChapter.value
                        viewModel.selectChapter(currentCh + 1)
                    },
                    onBackToChaptersClick = {
                        viewModel.goBackToChapterList()
                    }
                )
            }
        }
    }
}

/**
 * 1. Difficulty Selection View
 * Displays strictly 4 cards: Beginner, Medium, Advanced, Expert. No Darjat, No Books.
 */
@Composable
private fun DifficultySelectionView(
    chapterProgressMap: Map<String, List<ChapterProgress>>,
    onSelectDifficulty: (DifficultyLevel) -> Unit
) {
    val langCode = LocalAppLanguage.current.code

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = "Quiz System",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = quizText("title", langCode),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = quizText("sub", langCode),
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = quizText("desc", langCode),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = quizText("select_level", langCode),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 4 Cards strictly
        val levels = DifficultyLevel.entries.toTypedArray()
        levels.forEach { level ->
            val chapters = chapterProgressMap[level.id] ?: emptyList()
            val unlockedCount = chapters.count { it.isUnlocked }
            val completedCount = chapters.count { it.isCompleted }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .clickable { onSelectDifficulty(level) }
                    .testTag("quiz_difficulty_${level.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(level.colorHex).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.iconEmoji,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = level.getDisplayName(langCode),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = level.getDescription(langCode),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = "Unlocked Chapters",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = quizText("unlocked_fmt", langCode, unlockedCount),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (completedCount > 0) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = quizText("passed_fmt", langCode, completedCount),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 2. Chapter List View
 * Displays 50 Chapters for the chosen difficulty level.
 */
@Composable
private fun ChapterListView(
    difficulty: DifficultyLevel,
    chapters: List<ChapterProgress>,
    onChapterClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    val unlockedCount = chapters.count { it.isUnlocked }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = difficulty.iconEmoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${difficulty.getDisplayName(langCode)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = quizText("chapters_unlocked_fmt", langCode, unlockedCount),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LinearProgressIndicator(
            progress = { unlockedCount.toFloat() / 10f },
            modifier = Modifier.fillMaxWidth(),
            color = Color(difficulty.colorHex),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chapters) { item ->
                ChapterCardItem(
                    chapter = item,
                    difficultyId = difficulty.id,
                    onClick = {
                        if (item.isUnlocked) {
                            onChapterClick(item.chapterNumber)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ChapterCardItem(
    chapter: ChapterProgress,
    difficultyId: String,
    onClick: () -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    val cardBg = if (chapter.isUnlocked) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    val meta = remember(difficultyId, chapter.chapterNumber) {
        DarsENizamiQuizGenerator.chaptersDirectory.find {
            it.stepId == difficultyId.lowercase() && it.chapterNumber == chapter.chapterNumber
        }
    }

    val chapterTitle = when (langCode) {
        "ur" -> meta?.titleUrdu ?: "باب ${chapter.chapterNumber}"
        "ps" -> meta?.titleUrdu ?: "څپرکی ${chapter.chapterNumber}"
        else -> meta?.titleEn ?: "Chapter ${chapter.chapterNumber}"
    }

    val bookInfo = meta?.let {
        if (langCode == "en") "${it.bookName} • ${it.assignedDarjatEn}"
        else "${it.bookName} • ${it.assignedDarjatUrdu}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = chapter.isUnlocked) { onClick() }
            .testTag("chapter_card_${chapter.chapterNumber}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (chapter.isUnlocked) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            chapter.isCompleted -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                            chapter.isUnlocked -> MaterialTheme.colorScheme.primaryContainer
                            else -> Color.Gray.copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (chapter.isUnlocked) {
                    Text(
                        text = "${chapter.chapterNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (chapter.isCompleted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = chapterTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (chapter.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    if (chapter.isCompleted) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF4CAF50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = quizText("passed_pct", langCode, chapter.highestPercentage.toInt()),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (chapter.isUnlocked) {
                        Text(
                            text = quizText("unlocked", langCode),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = quizText("locked", langCode),
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }

                if (bookInfo != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "📖 $bookInfo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (chapter.isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (chapter.attempts > 0) {
                    Text(
                        text = quizText("stats_fmt", langCode, chapter.highestScore, chapter.attempts, chapter.accuracyPercentage.toInt()),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = quizText("ch_req", langCode),
                        fontSize = 11.sp,
                        color = if (chapter.isUnlocked) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * 3. Active Quiz View
 * 50 questions, 20-sec timer per question, immediate feedback with citation & explanation.
 */
@Composable
private fun ActiveQuizView(
    viewModel: QuizViewModel,
    onBackClick: () -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    val questions by viewModel.chapterQuestions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val timerSeconds by viewModel.questionTimeRemaining.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val isAnswered by viewModel.isAnswered.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
    val chapterNum by viewModel.selectedChapter.collectAsStateWithLifecycle()

    val currentQ = questions.getOrNull(currentIndex) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit Quiz"
                    )
                }
                Column {
                    Text(
                        text = quizText("level_ch_fmt", langCode, selectedDifficulty.getDisplayName(langCode), chapterNum),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = quizText("q_num_fmt", langCode, currentIndex + 1, questions.size),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 20s Countdown Timer Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (timerSeconds <= 5) Color(0xFFF44336) else MaterialTheme.colorScheme.primary
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = quizText("sec_fmt", langCode, timerSeconds),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Question Progress Bar
        LinearProgressIndicator(
            progress = { if (questions.isNotEmpty()) (currentIndex + 1).toFloat() / questions.size.toFloat() else 0f },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentQ.subject,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = currentQ.bookName,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!currentQ.arabicText.isNull_or_blank_compat()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = currentQ.arabicText ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = currentQ.getDisplayQuestion(langCode),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options (4 Options)
            val displayOptions = currentQ.getDisplayOptions(langCode)
            displayOptions.forEachIndexed { optIdx, optionText ->
                val isSelected = selectedOption == optIdx
                val isCorrect = optIdx == currentQ.correctAnswerIndex

                val cardColors = when {
                    isAnswered && isCorrect -> CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    isAnswered && isSelected && !isCorrect -> CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    isSelected -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    else -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                }

                val borderModifier = when {
                    isAnswered && isCorrect -> Modifier.border(1.5.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                    isAnswered && isSelected && !isCorrect -> Modifier.border(1.5.dp, Color(0xFFF44336), RoundedCornerShape(12.dp))
                    else -> Modifier
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .then(borderModifier)
                        .clickable(enabled = !isAnswered) {
                            viewModel.selectOption(optIdx)
                        }
                        .testTag("quiz_option_$optIdx"),
                    shape = RoundedCornerShape(12.dp),
                    colors = cardColors,
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val optionLetter = when (optIdx) {
                            0 -> "A"
                            1 -> "B"
                            2 -> "C"
                            else -> "D"
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isAnswered && isCorrect -> Color(0xFF4CAF50)
                                        isAnswered && isSelected -> Color(0xFFF44336)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionLetter,
                                fontWeight = FontWeight.Bold,
                                color = if (isAnswered && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (isAnswered) {
                            if (isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = Color(0xFF4CAF50)
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Incorrect",
                                    tint = Color(0xFFF44336)
                                )
                            }
                        }
                    }
                }
            }

            // Immediate Feedback & Citation Section (Appears when answered or timed out)
            AnimatedVisibility(
                visible = isAnswered,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption == currentQ.correctAnswerIndex) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (selectedOption == currentQ.correctAnswerIndex) Icons.Default.CheckCircle else Icons.Default.MenuBook,
                                    contentDescription = "Feedback",
                                    tint = if (selectedOption == currentQ.correctAnswerIndex) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedOption == currentQ.correctAnswerIndex) quizText("correct_ans_title", langCode) else quizText("exp_title", langCode),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (selectedOption == currentQ.correctAnswerIndex) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = quizText("correct_ans_lbl", langCode, displayOptions.getOrNull(currentQ.correctAnswerIndex) ?: ""),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentQ.getDisplayExplanation(langCode),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Exact Citation Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.7f))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = "Source",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = currentQ.getDisplayCitation(langCode),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.advanceToNextQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("quiz_next_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentIndex + 1 < questions.size) quizText("next_q", langCode) else quizText("view_res", langCode),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper extension for null or blank string
 */
private fun String?.isNull_or_blank_compat(): Boolean = this == null || this.trim().isEmpty()

/**
 * 4. Quiz Result View
 * Displays final percentage, pass/fail threshold (90%), next chapter unlock notification, and Retry button.
 */
@Composable
private fun QuizResultView(
    viewModel: QuizViewModel,
    outcome: ChapterResultOutcome?,
    onRetryClick: () -> Unit,
    onNextChapterClick: () -> Unit,
    onBackToChaptersClick: () -> Unit
) {
    val langCode = LocalAppLanguage.current.code
    val score by viewModel.score.collectAsStateWithLifecycle()
    val chapterNum by viewModel.selectedChapter.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
    val totalTimeSecs by viewModel.totalQuizTimeSpentSecs.collectAsStateWithLifecycle()

    val questions by viewModel.chapterQuestions.collectAsStateWithLifecycle()
    val totalQ = if (questions.isNotEmpty()) questions.size else 20
    val percentage = outcome?.percentage ?: ((score.toFloat() / totalQ.toFloat()) * 100f)
    val isPassed = outcome?.isPassed ?: (percentage >= 90f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Result Icon Header
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    if (isPassed) Color(0xFF4CAF50).copy(alpha = 0.15f) else Color(0xFFF44336).copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPassed) Icons.Default.EmojiEvents else Icons.Default.Refresh,
                contentDescription = "Result",
                tint = if (isPassed) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isPassed) quizText("ch_passed", langCode, chapterNum) else quizText("ch_incomp", langCode, chapterNum),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = quizText("level_50_mcq", langCode, selectedDifficulty.getDisplayName(langCode), totalQ),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Score Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${percentage.toInt()}%",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isPassed) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )

                Text(
                    text = quizText("correct_count_fmt", langCode, score, totalQ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(quizText("accuracy", langCode), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${percentage.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(quizText("time_taken", langCode), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatDuration(totalTimeSecs), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(quizText("pass_target", langCode), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("90%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unlock Notification Banner
        if (isPassed) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Unlocked",
                        tint = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = quizText("next_unlocked", langCode),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = quizText("score_90_desc", langCode, chapterNum + 1),
                            fontSize = 12.sp,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = quizText("keep_practicing", langCode),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = quizText("need_90_desc", langCode, chapterNum + 1),
                            fontSize = 12.sp,
                            color = Color(0xFFBF360C)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        if (isPassed && chapterNum < 10) {
            Button(
                onClick = onNextChapterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = quizText("start_next_ch", langCode, chapterNum + 1),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = onRetryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("retry_quiz_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = quizText("retry_ch", langCode, chapterNum),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBackToChaptersClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = quizText("back_to_all", langCode),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun quizText(key: String, langCode: String, vararg args: Any): String {
    return when (langCode) {
        "ps" -> when (key) {
            "title" -> "اسلامي ازموینې سیسټم"
            "sub" -> "د درسِ نظامي هراړخیز نصاب"
            "desc" -> "د ۱۰ څپرکو خلاصولو لپاره د کچې انتخاب وکړئ. د بل څپرکي خلاصولو لپاره ۹۰٪ نمرې ترلاسه کړئ."
            "select_level" -> "د ازموینې کچه وټاکئ"
            "unlocked_fmt" -> "${args[0]} / ۱۰ څپرکي خلاص دي"
            "passed_fmt" -> "• ${args[0]} بریالي"
            "chapters_unlocked_fmt" -> "۱۰ څپرکي • ${args[0]} خلاص دي"
            "ch_num" -> "څپرکی ${args[0]}"
            "passed_pct" -> "بریالی (${args[0]}%)"
            "unlocked" -> "خلاص دی"
            "locked" -> "بند دی"
            "stats_fmt" -> "غوره نمره: ${args[0]} • هڅې: ${args[1]} • دقت: ${args[2]}%"
            "ch_req" -> "۱۰ پوښتنې • ۲۰ ثانیې فی پوښتنه • د بل څپرکي لپاره ۹۰٪ اړین دي"
            "level_ch_fmt" -> "${args[0]} - ${args[1]} څپرکی"
            "q_num_fmt" -> "پوښتنه ${args[0]} له ${args.getOrElse(1) { "۱۰" }} څخه"
            "sec_fmt" -> "${args[0]} ثانیې"
            "correct_ans_title" -> "سم ځواب!"
            "exp_title" -> "تشریح او سرچینه"
            "correct_ans_lbl" -> "سم ځواب: ${args[0]}"
            "next_q" -> "بله پوښتنه"
            "view_res" -> "پایلې وګورئ"
            "ch_passed" -> "څپرکی ${args[0]} بریالی شو!"
            "ch_incomp" -> "څپرکی ${args[0]} ناپشپړ دی"
            "level_50_mcq" -> "کچه ${args[0]} • ${args.getOrElse(1) { "۱۰" }} پوښتنې"
            "correct_count_fmt" -> "${args.getOrElse(1) { "۱۰" }}/ ${args[0]} پوښتنې سمې دي"
            "accuracy" -> "دقت او درستی"
            "time_taken" -> "تېر شوی وخت"
            "pass_target" -> "د بریا هدف"
            "next_unlocked" -> "بل څپرکی خلاص شو! 🎉"
            "score_90_desc" -> "تاسو ۹۰٪+ نمرې ترلاسه کړې! ${args[0]} څپرکی اوس خلاص دی."
            "keep_practicing" -> "تمرین او هڅه جاري وساتئ!"
            "need_90_desc" -> "د ${args[0]} څپرکي خلاصولو لپاره ۹۰٪ نمرو ته اړتیا لرئ."
            "start_next_ch" -> "${args[0]} څپرکی پیل کړئ"
            "retry_ch" -> "${args[0]} څپرکی بیا و ازمویئ"
            "back_to_all" -> "ټولو څپرکو ته بېرته ستانه شئ"
            else -> ""
        }
        "ur" -> when (key) {
            "title" -> "اسلامی کوئز سسٹم"
            "sub" -> "درسِ نظامی جامع نصاب"
            "desc" -> "10 ابواب ان لاک کرنے کے لیے اپنی مشکل کی سطح منتخب کریں۔ اگلا باب کھولنے کے لیے ہر باب میں 90% یا اس سے زائد اسکور حاصل کریں۔"
            "select_level" -> "مشکل کی سطح منتخب کریں"
            "unlocked_fmt" -> "${args[0]} / 10 ابواب ان لاک ہیں"
            "passed_fmt" -> "• ${args[0]} کامیاب"
            "chapters_unlocked_fmt" -> "10 ابواب • ${args[0]} ان لاک"
            "ch_num" -> "باب نمبر ${args[0]}"
            "passed_pct" -> "کامیاب (${args[0]}%)"
            "unlocked" -> "ان لاک"
            "locked" -> "لاک ہے"
            "stats_fmt" -> "بہترین اسکور: ${args[0]} • کوششیں: ${args[1]} • درستگی: ${args[2]}%"
            "ch_req" -> "10 سوالات • 20 سیکنڈ فی سوال • اگلا باب کھولنے کے لیے 90%+ درکار"
            "level_ch_fmt" -> "${args[0]} - باب ${args[1]}"
            "q_num_fmt" -> "سوال ${args[0]} از ${args.getOrElse(1) { 10 }}"
            "sec_fmt" -> "${args[0]} سیکنڈ"
            "correct_ans_title" -> "درست جواب!"
            "exp_title" -> "وضاحت و حوالہ"
            "correct_ans_lbl" -> "درست جواب: ${args[0]}"
            "next_q" -> "اگلا سوال"
            "view_res" -> "نتائج دیکھیں"
            "ch_passed" -> "باب ${args[0]} میں کامیابی!"
            "ch_incomp" -> "باب ${args[0]} نامکمل"
            "level_50_mcq" -> "لیول ${args[0]} • ${args.getOrElse(1) { 10 }} سوالات"
            "correct_count_fmt" -> "${args[0]} / ${args.getOrElse(1) { 10 }} سوالات درست"
            "accuracy" -> "درستگی"
            "time_taken" -> "کل وقت"
            "pass_target" -> "کامیابی کا ہدف"
            "next_unlocked" -> "اگلا باب ان لاک ہو گیا! 🎉"
            "score_90_desc" -> "آپ نے 90%+ اسکور حاصل کیا! باب ${args[0]} اب آپ کے لیے دستیاب ہے۔"
            "keep_practicing" -> "مشق جاری رکھیں!"
            "need_90_desc" -> "باب ${args[0]} کھولنے کے لیے آپ کو 90% درست اسکور درکار ہے۔ اسکور بہتر بنانے کے لیے دوبارہ کوشش کریں!"
            "start_next_ch" -> "باب ${args[0]} شروع کریں"
            "retry_ch" -> "باب ${args[0]} دوبارہ کیجیے"
            "back_to_all" -> "تمام ابواب پر واپس جائیں"
            else -> ""
        }
        else -> when (key) {
            "title" -> "Islamic Quiz System"
            "sub" -> "Dars-e-Nizami Master Curriculum"
            "desc" -> "Select your difficulty level to unlock 10 chapters per level. Complete each chapter with 90%+ score to unlock the next chapter."
            "select_level" -> "Select Difficulty Level"
            "unlocked_fmt" -> "${args[0]} / 10 Chapters Unlocked"
            "passed_fmt" -> "• ${args[0]} Passed"
            "chapters_unlocked_fmt" -> "10 Chapters • ${args[0]} Unlocked"
            "ch_num" -> "Chapter ${args[0]}"
            "passed_pct" -> "PASSED (${args[0]}%)"
            "unlocked" -> "Unlocked"
            "locked" -> "Locked"
            "stats_fmt" -> "Highest Score: ${args[0]} • Attempts: ${args[1]} • Acc: ${args[2]}%"
            "ch_req" -> "10 MCQs • 20s per question • 90%+ to unlock next"
            "level_ch_fmt" -> "${args[0]} - Chapter ${args[1]}"
            "q_num_fmt" -> "Question ${args[0]} of ${args.getOrElse(1) { 10 }}"
            "sec_fmt" -> "${args[0]}s"
            "correct_ans_title" -> "Correct Answer!"
            "exp_title" -> "Explanation & Reference"
            "correct_ans_lbl" -> "Correct Answer: ${args[0]}"
            "next_q" -> "Next Question"
            "view_res" -> "View Results"
            "ch_passed" -> "Chapter ${args[0]} Passed!"
            "ch_incomp" -> "Chapter ${args[0]} Incomplete"
            "level_50_mcq" -> "${args[0]} Level • ${args.getOrElse(1) { 10 }} MCQs"
            "correct_count_fmt" -> "${args[0]} / ${args.getOrElse(1) { 10 }} Questions Correct"
            "accuracy" -> "Accuracy"
            "time_taken" -> "Time Taken"
            "pass_target" -> "Pass Target"
            "next_unlocked" -> "Next Chapter Unlocked! 🎉"
            "score_90_desc" -> "You scored 90%+! Chapter ${args[0]} is now ready for you."
            "keep_practicing" -> "Keep Practicing!"
            "need_90_desc" -> "You need 90% to unlock Chapter ${args[0]}. Try again to improve your score!"
            "start_next_ch" -> "Start Chapter ${args[0]}"
            "retry_ch" -> "Retry Chapter ${args[0]}"
            "back_to_all" -> "Back to All Chapters"
            else -> ""
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
}
