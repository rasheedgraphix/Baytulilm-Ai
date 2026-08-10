package com.example.data.repository

import com.example.BuildConfig
import com.example.data.model.BookEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiPart(val text: String? = null)
data class GeminiContent(val parts: List<GeminiPart>, val role: String = "user")
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiCandidate(val content: GeminiContent?)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

class GeminiRepository {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val apiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun askScholarWithRAG(
        userPrompt: String,
        selectedBook: BookEntity?,
        libraryBooks: List<BookEntity>,
        history: List<Pair<String, String>> = emptyList()
    ): Result<RAGResponse> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Gemini API key is missing. Please set your key in the AI Studio Secrets panel."))
        }

        // Search library for context matching user prompt
        val relevantBooks = if (selectedBook != null) {
            listOf(selectedBook)
        } else {
            libraryBooks.filter { book ->
                userPrompt.contains(book.title, ignoreCase = true) ||
                        userPrompt.contains(book.subject, ignoreCase = true) ||
                        userPrompt.contains(book.author, ignoreCase = true) ||
                        userPrompt.contains(book.darja, ignoreCase = true)
            }.ifEmpty { libraryBooks.take(10) }
        }

        val libraryContext = buildString {
            append("RETRIEVED UPLOADED BOOKS FROM APPLICATION DATABASE:\n\n")
            relevantBooks.forEachIndexed { idx, b ->
                append("--- BOOK ${idx + 1} ---\n")
                append("Book Name: ${b.title}\n")
                append("Author: ${b.author}\n")
                append("Darja: ${b.darja}\n")
                append("Subject: ${b.subject}\n")
                append("Language: ${b.language}\n")
                append("Type: ${b.type}\n")
                append("Page Sample Range: Page 1 - Page ${b.pageCount}\n")
                append("Text Content/Overview: ${b.description}\n\n")
            }
            append("APP METADATA & SUPPORT INFO:\n")
            append("App Name: Baytul Ilm AI\n")
            append("Developer: Nouman Ur Rasheed\n")
            append("Location: Swabi, Khyber Pakhtunkhwa, Pakistan\n")
            append("Phone: 0345-5067874 | Email: hafiznoumanrasheed@gmail.com\n")
            append("Core Features: Digital Library (8 Darjat Dars-e-Nizami), Smart Quiz System (50 chapters/level, 20s per question, 90% pass requirement), AI Assistant Scholar with Hybrid RAG & Gemini Fallback, Study Notes Generator, Flashcards, LMS Courses, Offline Storage, Prayer Times & Qibla Direction, Certificate Generator.\n\n")
        }

        val systemPrompt = """
            You are Baytul Ilm AI Scholar, an authentic Islamic Education Assistant integrated into the 'Baytul Ilm AI' application.

            HYBRID KNOWLEDGE SYSTEM MANDATE:
            1. HIGHEST PRIORITY (SOURCE 1 - UPLOADED LIBRARY):
               First, check the RETRIEVED UPLOADED BOOKS context provided below.
               IF the user's question can be answered using the uploaded Dars-e-Nizami books:
               You MUST answer strictly from those uploaded books.
               Output format:
               [SOURCE_TYPE: UPLOADED_LIBRARY]
               [CITATION_START]
               Book Name: <Exact book name from context>
               Darja: <Darja from context>
               Subject: <Subject from context>
               Page Number: <Estimated or sample page number e.g. Page 24>
               Confidence Score: <e.g. 96%>
               Quoted Paragraph: "<Excerpt directly supported by context>"
               [CITATION_END]
               [ANSWER_START]
               <Detailed scholarly explanation in Urdu, Arabic, or English matching user's language>
               [ANSWER_END]

            2. FALLBACK (SOURCE 2 - GEMINI GENERAL ISLAMIC KNOWLEDGE):
               IF the uploaded books do NOT contain the answer (e.g. general Quran, Tafsir, Hadith, Fiqh, Aqeedah, Seerah, Nahw, Sarf, Islamic History, Duas, or questions about how to use the app, developer info, quiz system, etc.):
               Automatically answer using your general Islamic & Application knowledge.
               Output format:
               [SOURCE_TYPE: GEMINI_GENERAL]
               [NOTICE_START]
               Source: Gemini General Islamic Knowledge
               This answer is not taken from the uploaded library books.
               [NOTICE_END]
               [ANSWER_START]
               <Comprehensive, polite answer in Urdu, Arabic, or English matching user's language>
               [ANSWER_END]

            ISLAMIC SAFETY & SCHOLARLY INTEGRITY:
            - Prefer references from the Noble Quran and authentic Hadith (Sahih Bukhari, Sahih Muslim, Sunan Kutub).
            - Do not fabricate references or quote unverified narrations without context.
            - If multiple scholarly opinions exist (e.g. Hanafi, Shafi'i, Maliki, Hanbali schools of thought), explain that there are different valid scholarly views and avoid claiming one opinion is the sole correct one unless universally established.
            - Answer in the user's language (Urdu, Arabic, English, or Roman Urdu).
        """.trimIndent()

        val contentsList = mutableListOf<GeminiContent>()
        contentsList.add(GeminiContent(parts = listOf(GeminiPart(text = libraryContext)), role = "user"))
        for ((user, assistant) in history) {
            contentsList.add(GeminiContent(parts = listOf(GeminiPart(text = user)), role = "user"))
            contentsList.add(GeminiContent(parts = listOf(GeminiPart(text = assistant)), role = "model"))
        }
        contentsList.add(GeminiContent(parts = listOf(GeminiPart(text = userPrompt)), role = "user"))

        val request = GeminiRequest(
            contents = contentsList,
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)), role = "system")
        )

        try {
            val response = apiService.generateContent(apiKey, request)
            val fullText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

            val parsedResponse = parseRAGResponse(fullText, relevantBooks.firstOrNull())
            Result.success(parsedResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeGrammar(arabicSentence: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Perform a comprehensive Dars-e-Nizami Arabic Grammar (Nahw & Sarf) analysis of this Arabic text:
            "$arabicSentence"
            
            Provide structured breakdown with:
            1. Sentence Breakdown (ترکیب نحوی)
            2. Word-by-Word Analysis (اعراب)
            3. Morphological Form (صرفی تجزیہ - باب, صیغہ, وزن)
            4. Urdu & English Translation (ترجمہ)
            5. Grammatical Rules & Notes (تشریح)
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    suspend fun generateSummary(bookTitle: String, summaryType: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Generate a $summaryType for the classical Dars-e-Nizami Islamic text: "$bookTitle".
            
            Include:
            1. Overview & Core Theme
            2. Key Principles & Rules (قواعد و ضوابط)
            3. Essential Definitions (اصطلاحات)
            4. Scholarly Consensus & Sub-topics
            5. Practical Examples for Students
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    suspend fun translateText(text: String, sourceLang: String, targetLang: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Translate the following Islamic scholarly text from $sourceLang to $targetLang with accuracy and respect:
            
            Text:
            "$text"
            
            Provide:
            1. Fluent Translation
            2. Key Technical Terminology (اصطلاحات)
            3. Contextual Explanation
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    suspend fun generateQuiz(bookTitle: String, difficulty: String, questionType: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Create a $difficulty level $questionType quiz based on the Islamic book "$bookTitle".
            
            Generate 5 questions with detailed answer key and references to classical Dars-e-Nizami syllabus.
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    suspend fun generateFlashcards(bookTitle: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Generate 6 study flashcards (Question/Front and Answer/Back) for essential concepts in "$bookTitle".
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    suspend fun generateStudyNotes(topic: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return@withContext Result.failure(Exception("Gemini API key missing"))

        val prompt = """
            Generate comprehensive Islamic study notes for Dars-e-Nizami students on topic: "$topic".
            Include headings, bullet points, classical references, and exam preparation tips.
        """.trimIndent()

        callGeminiDirect(prompt, apiKey)
    }

    private suspend fun callGeminiDirect(prompt: String, apiKey: String): Result<String> {
        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)), role = "user"))
        )

        return try {
            val response = apiService.generateContent(apiKey, request)
            val answer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!answer.isNullOrEmpty()) {
                Result.success(answer)
            } else {
                Result.failure(Exception("No output generated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseRAGResponse(fullText: String, fallbackBook: BookEntity?): RAGResponse {
        val isGeminiGeneral = fullText.contains("[SOURCE_TYPE: GEMINI_GENERAL]") ||
                fullText.contains("Gemini General Islamic Knowledge") ||
                !fullText.contains("[CITATION_START]")

        var bookName: String? = if (isGeminiGeneral) null else (fallbackBook?.title ?: "Digital Library Book")
        var darja: String? = if (isGeminiGeneral) null else fallbackBook?.darja
        var subject: String? = if (isGeminiGeneral) null else fallbackBook?.subject
        var pageNumber: String? = if (isGeminiGeneral) null else "Page 1"
        var confidence = if (isGeminiGeneral) "90%" else "96%"
        var quoted: String? = if (isGeminiGeneral) null else fallbackBook?.description
        var answer = fullText

        try {
            if (fullText.contains("[CITATION_START]") && fullText.contains("[CITATION_END]")) {
                val citationBlock = fullText.substringAfter("[CITATION_START]").substringBefore("[CITATION_END]")
                citationBlock.lines().forEach { line ->
                    when {
                        line.startsWith("Book Name:") -> bookName = line.substringAfter("Book Name:").trim()
                        line.startsWith("Darja:") -> darja = line.substringAfter("Darja:").trim()
                        line.startsWith("Subject:") -> subject = line.substringAfter("Subject:").trim()
                        line.startsWith("Page Number:") -> pageNumber = line.substringAfter("Page Number:").trim()
                        line.startsWith("Confidence Score:") -> confidence = line.substringAfter("Confidence Score:").trim()
                        line.startsWith("Quoted Paragraph:") -> quoted = line.substringAfter("Quoted Paragraph:").trim().removeSurrounding("\"")
                    }
                }
            }

            if (fullText.contains("[ANSWER_START]")) {
                answer = fullText.substringAfter("[ANSWER_START]").substringBefore("[ANSWER_END]").trim()
            }
        } catch (e: Exception) {
            answer = fullText
        }

        // Clean out prompt tags if any remain
        answer = answer
            .replace("[SOURCE_TYPE: UPLOADED_LIBRARY]", "")
            .replace("[SOURCE_TYPE: GEMINI_GENERAL]", "")
            .replace("[NOTICE_START]", "")
            .replace("[NOTICE_END]", "")
            .trim()

        return RAGResponse(
            answer = answer,
            sourceMode = if (isGeminiGeneral) "GEMINI_GENERAL" else "UPLOADED_LIBRARY",
            bookName = bookName,
            darja = darja,
            subject = subject,
            pageNumber = pageNumber,
            quotedParagraph = quoted,
            confidenceScore = confidence,
            isGeminiFallback = isGeminiGeneral
        )
    }
}

data class RAGResponse(
    val answer: String,
    val sourceMode: String = "UPLOADED_LIBRARY",
    val bookName: String? = null,
    val darja: String? = null,
    val subject: String? = null,
    val pageNumber: String? = null,
    val quotedParagraph: String? = null,
    val confidenceScore: String = "95%",
    val isGeminiFallback: Boolean = false
)

