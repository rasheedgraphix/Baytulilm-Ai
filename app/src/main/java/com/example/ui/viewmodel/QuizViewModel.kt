package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChapterProgress
import com.example.data.model.ComprehensiveQuizQuestion
import com.example.data.model.DifficultyLevel
import com.example.data.repository.ChapterResultOutcome
import com.example.data.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class QuizViewState {
    DIFFICULTY_SELECTION,
    CHAPTER_LIST,
    ACTIVE_QUIZ,
    QUIZ_RESULT
}

class QuizViewModel : ViewModel() {

    private val repository = QuizRepository()

    val chapterProgressMap: StateFlow<Map<String, List<ChapterProgress>>> = repository.chapterProgressMap

    private val _viewState = MutableStateFlow(QuizViewState.DIFFICULTY_SELECTION)
    val viewState: StateFlow<QuizViewState> = _viewState.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow(DifficultyLevel.BEGINNER)
    val selectedDifficulty: StateFlow<DifficultyLevel> = _selectedDifficulty.asStateFlow()

    private val _selectedChapter = MutableStateFlow(1)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _chapterQuestions = MutableStateFlow<List<ComprehensiveQuizQuestion>>(emptyList())
    val chapterQuestions: StateFlow<List<ComprehensiveQuizQuestion>> = _chapterQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _questionTimeRemaining = MutableStateFlow(20) // 20 seconds timer
    val questionTimeRemaining: StateFlow<Int> = _questionTimeRemaining.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered: StateFlow<Boolean> = _isAnswered.asStateFlow()

    private val _userAnswers = MutableStateFlow<Map<Int, Int?>>(emptyMap())
    val userAnswers: StateFlow<Map<Int, Int?>> = _userAnswers.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _totalQuizTimeSpentSecs = MutableStateFlow(0L)
    val totalQuizTimeSpentSecs: StateFlow<Long> = _totalQuizTimeSpentSecs.asStateFlow()

    private val _lastChapterOutcome = MutableStateFlow<ChapterResultOutcome?>(null)
    val lastChapterOutcome: StateFlow<ChapterResultOutcome?> = _lastChapterOutcome.asStateFlow()

    private var questionTimerJob: Job? = null
    private var autoAdvanceJob: Job? = null

    fun selectDifficulty(difficulty: DifficultyLevel) {
        _selectedDifficulty.value = difficulty
        _viewState.value = QuizViewState.CHAPTER_LIST
    }

    fun goBackToDifficultySelection() {
        questionTimerJob?.cancel()
        autoAdvanceJob?.cancel()
        _viewState.value = QuizViewState.DIFFICULTY_SELECTION
    }

    fun goBackToChapterList() {
        questionTimerJob?.cancel()
        autoAdvanceJob?.cancel()
        _viewState.value = QuizViewState.CHAPTER_LIST
    }

    fun selectChapter(chapterNumber: Int) {
        _selectedChapter.value = chapterNumber
        val questions = repository.getQuestionsForChapter(_selectedDifficulty.value.id, chapterNumber)
        _chapterQuestions.value = questions
        _currentQuestionIndex.value = 0
        _score.value = 0
        _totalQuizTimeSpentSecs.value = 0L
        _userAnswers.value = emptyMap()
        _viewState.value = QuizViewState.ACTIVE_QUIZ

        startQuestionFlow()
    }

    private fun startQuestionFlow() {
        questionTimerJob?.cancel()
        autoAdvanceJob?.cancel()

        _selectedOption.value = null
        _isAnswered.value = false
        _questionTimeRemaining.value = 20

        questionTimerJob = viewModelScope.launch {
            while (_questionTimeRemaining.value > 0 && !_isAnswered.value) {
                delay(1000L)
                _questionTimeRemaining.value -= 1
                _totalQuizTimeSpentSecs.value += 1
            }

            if (_questionTimeRemaining.value <= 0 && !_isAnswered.value) {
                // Time expired for this question
                _isAnswered.value = true
                _selectedOption.value = null
                _userAnswers.value = _userAnswers.value + (_currentQuestionIndex.value to null)

                // Wait 2.5 seconds showing answer feedback then auto-advance
                autoAdvanceJob = viewModelScope.launch {
                    delay(2500L)
                    advanceToNextQuestion()
                }
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        if (_isAnswered.value) return

        questionTimerJob?.cancel()
        _selectedOption.value = optionIndex
        _isAnswered.value = true
        _userAnswers.value = _userAnswers.value + (_currentQuestionIndex.value to optionIndex)

        val currentQ = _chapterQuestions.value.getOrNull(_currentQuestionIndex.value)
        if (currentQ != null && optionIndex == currentQ.correctAnswerIndex) {
            _score.value += 1
        }

        autoAdvanceJob = viewModelScope.launch {
            delay(2500L)
            advanceToNextQuestion()
        }
    }

    fun advanceToNextQuestion() {
        autoAdvanceJob?.cancel()
        questionTimerJob?.cancel()

        if (_currentQuestionIndex.value + 1 < _chapterQuestions.value.size) {
            _currentQuestionIndex.value += 1
            startQuestionFlow()
        } else {
            finishChapterQuiz()
        }
    }

    fun retryChapter() {
        selectChapter(_selectedChapter.value)
    }

    private fun finishChapterQuiz() {
        questionTimerJob?.cancel()
        autoAdvanceJob?.cancel()

        val outcome = repository.recordChapterResult(
            difficulty = _selectedDifficulty.value.id,
            chapterNumber = _selectedChapter.value,
            score = _score.value,
            totalQuestions = _chapterQuestions.value.size.coerceAtLeast(1),
            timeTakenSecs = _totalQuizTimeSpentSecs.value
        )

        _lastChapterOutcome.value = outcome
        _viewState.value = QuizViewState.QUIZ_RESULT
    }

    fun getChapterProgressList(difficultyId: String): List<ChapterProgress> {
        return repository.getChapterProgressList(difficultyId)
    }
}
