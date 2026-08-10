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
                val progressList = viewModel.getChapterProgressList(selectedDifficulty.id)
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
    val isUrdu = LocalAppLanguage.current.code == "ur"

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
                            text = if (isUrdu) "اسلامی کوئز سسٹم" else "Islamic Quiz System",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isUrdu) "درسِ نظامی جامع نصاب" else "Dars-e-Nizami Master Curriculum",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isUrdu) "50 ابواب ان لاک کرنے کے لیے اپنی مشکل کی سطح منتخب کریں۔ اگلا باب کھولنے کے لیے ہر باب میں 90% یا اس سے زائد اسکور حاصل کریں۔" else "Select your difficulty level to unlock 50 chapters per level. Complete each chapter with 90%+ score to unlock the next chapter.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isUrdu) "مشکل کی سطح منتخب کریں" else "Select Difficulty Level",
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
                                text = level.getDisplayName(isUrdu),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = level.getDescription(isUrdu),
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
                                text = if (isUrdu) "$unlockedCount / 50 ابواب ان لاک ہیں" else "$unlockedCount / 50 Chapters Unlocked",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (completedCount > 0) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isUrdu) "• $completedCount کامیاب" else "• $completedCount Passed",
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
    val isUrdu = LocalAppLanguage.current.code == "ur"
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
                    text = if (isUrdu) "لیول ${difficulty.getDisplayName(isUrdu)}" else "${difficulty.displayName} Level",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isUrdu) "50 ابواب • $unlockedCount ان لاک" else "50 Chapters • $unlockedCount Unlocked",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LinearProgressIndicator(
            progress = { unlockedCount.toFloat() / 50f },
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
    onClick: () -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
    val cardBg = if (chapter.isUnlocked) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                        text = if (isUrdu) "باب نمبر ${chapter.chapterNumber}" else "Chapter ${chapter.chapterNumber}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (chapter.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )

                    if (chapter.isCompleted) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF4CAF50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isUrdu) "کامیاب (${chapter.highestPercentage.toInt()}%)" else "PASSED (${chapter.highestPercentage.toInt()}%)",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (chapter.isUnlocked) {
                        Text(
                            text = if (isUrdu) "ان لاک" else "Unlocked",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = if (isUrdu) "لاک ہے" else "Locked",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (chapter.attempts > 0) {
                    Text(
                        text = if (isUrdu) "بہترین اسکور: ${chapter.highestScore}/50 • کوششیں: ${chapter.attempts} • درستگی: ${chapter.accuracyPercentage.toInt()}%" else "Highest Score: ${chapter.highestScore}/50 • Attempts: ${chapter.attempts} • Acc: ${chapter.accuracyPercentage.toInt()}%",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = if (isUrdu) "50 سوالات • 20 سیکنڈ فی سوال • اگلا باب کھولنے کے لیے 90%+ درکار" else "50 MCQs • 20s per question • 90%+ to unlock next",
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
    val isUrdu = LocalAppLanguage.current.code == "ur"
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
                        text = if (isUrdu) "${selectedDifficulty.getDisplayName(isUrdu)} - باب $chapterNum" else "${selectedDifficulty.displayName} - Chapter $chapterNum",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isUrdu) "سوال ${currentIndex + 1} از 50" else "Question ${currentIndex + 1} of 50",
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
                        text = if (isUrdu) "${timerSeconds} سیکنڈ" else "${timerSeconds}s",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Question Progress Bar
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / 50f },
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
                        text = currentQ.question,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options (4 Options)
            currentQ.options.forEachIndexed { optIdx, optionText ->
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
                                    text = if (isUrdu) (if (selectedOption == currentQ.correctAnswerIndex) "درست جواب!" else "وضاحت و حوالہ") else (if (selectedOption == currentQ.correctAnswerIndex) "Correct Answer!" else "Explanation & Reference"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (selectedOption == currentQ.correctAnswerIndex) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isUrdu) "درست جواب: ${currentQ.options.getOrNull(currentQ.correctAnswerIndex) ?: ""}" else "Correct Answer: ${currentQ.options.getOrNull(currentQ.correctAnswerIndex) ?: ""}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentQ.explanation,
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
                                        text = if (isUrdu) "${currentQ.bookName} • ${currentQ.chapter} (صفحہ ${currentQ.pageNumber})" else "${currentQ.bookName} • ${currentQ.chapter} (Page ${currentQ.pageNumber})",
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
                            text = if (isUrdu) (if (currentIndex + 1 < questions.size) "اگلا سوال" else "نتائج دیکھیں") else (if (currentIndex + 1 < questions.size) "Next Question" else "View Results"),
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
    val isUrdu = LocalAppLanguage.current.code == "ur"
    val score by viewModel.score.collectAsStateWithLifecycle()
    val chapterNum by viewModel.selectedChapter.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
    val totalTimeSecs by viewModel.totalQuizTimeSpentSecs.collectAsStateWithLifecycle()

    val percentage = outcome?.percentage ?: ((score.toFloat() / 50f) * 100f)
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
            text = if (isUrdu) (if (isPassed) "باب $chapterNum میں کامیابی!" else "باب $chapterNum نامکمل") else (if (isPassed) "Chapter $chapterNum Passed!" else "Chapter $chapterNum Incomplete"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = if (isUrdu) "لیول ${selectedDifficulty.getDisplayName(isUrdu)} • 50 سوالات" else "${selectedDifficulty.displayName} Level • 50 MCQs",
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
                    text = if (isUrdu) "50/ $score سوالات درست" else "$score / 50 Questions Correct",
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
                        Text(if (isUrdu) "درستگی" else "Accuracy", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${percentage.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isUrdu) "کل وقت" else "Time Taken", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatDuration(totalTimeSecs), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isUrdu) "کامیابی کا ہدف" else "Pass Target", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                            text = if (isUrdu) "اگلا باب ان لاک ہو گیا! 🎉" else "Next Chapter Unlocked! 🎉",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = if (isUrdu) "آپ نے 90%+ اسکور حاصل کیا! باب ${chapterNum + 1} اب آپ کے لیے دستیاب ہے۔" else "You scored 90%+! Chapter ${chapterNum + 1} is now ready for you.",
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
                            text = if (isUrdu) "مشق جاری رکھیں!" else "Keep Practicing!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = if (isUrdu) "باب ${chapterNum + 1} کھولنے کے لیے آپ کو 90% (45/50 درست) اسکور درکار ہے۔ اسکور بہتر بنانے کے لیے دوبارہ کوشش کریں!" else "You need 90% (45/50 correct) to unlock Chapter ${chapterNum + 1}. Try again to improve your score!",
                            fontSize = 12.sp,
                            color = Color(0xFFBF360C)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        if (isPassed && chapterNum < 50) {
            Button(
                onClick = onNextChapterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = if (isUrdu) "باب ${chapterNum + 1} شروع کریں" else "Start Chapter ${chapterNum + 1}",
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
                text = if (isUrdu) "باب $chapterNum دوبارہ کیجیے" else "Retry Chapter $chapterNum",
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
                text = if (isUrdu) "تمام ابواب پر واپس جائیں" else "Back to All Chapters",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
}
