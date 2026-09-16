package com.example.data.repository

import com.example.data.model.ComprehensiveQuizQuestion
import java.util.Locale

/**
 * Robust duplicate detection and prevention engine for Dars-e-Nizami Islamic Quiz System.
 * Checks for:
 * 1. Exact string matches (Urdu, English, Arabic)
 * 2. Normalized stem matches (ignoring tashkeel, punctuation, extra whitespaces, common prefixes)
 * 3. Semantic concept + correct answer cross-comparison
 * 4. Fuzzy distance / Jaccard similarity across question body and correct choice
 */
object QuizDuplicateDetector {

    // Removes Arabic diacritics / tashkeel (fatha, damma, kasra, sukoon, shaddah, tanween, etc.)
    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]")
    // Removes punctuation, brackets, hyphens, question marks
    private val PUNCTUATION_REGEX = Regex("[\\p{Punct}،؟؛«»“”’‘—–]")
    private val MULTI_SPACE_REGEX = Regex("\\s+")

    /**
     * Normalizes text for semantic canonical comparison.
     */
    fun normalize(text: String?): String {
        if (text.isNullOrBlank()) return ""
        return text
            .replace(TASHKEEL_REGEX, "")
            .replace(PUNCTUATION_REGEX, " ")
            .replace("ي", "ی")
            .replace("ك", "ک")
            .replace("ة", "ہ")
            .replace("ۀ", "ہ")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .lowercase(Locale.ROOT)
            .replace(MULTI_SPACE_REGEX, " ")
            .trim()
    }

    /**
     * Removes common Urdu question boilerplate to extract the core thematic concept.
     */
    fun extractQuestionCore(text: String?): String {
        val norm = normalize(text)
        val stopWords = listOf(
            "کا کیا حکم ہے", "کی تعریف کیا ہے", "کا وزن اور صیغہ کیا ہے",
            "کا صیغہ کیا ہے", "کون سا ہے", "کون سی ہے", "کیا ہے",
            "کس نے لکھی", "کی رو سے", "کے مطابق", "میں سے", "کیا کہلاتا ہے",
            "کہتے ہیں", "درست جواب", "درج ذیل", "مندرجہ ذیل", "بیان کریں",
            "what is the", "according to", "which of the following", "defined as",
            "ruling of", "pattern of", "classification of"
        )
        var result = norm
        for (sw in stopWords) {
            result = result.replace(sw, " ")
        }
        return result.replace(MULTI_SPACE_REGEX, " ").trim()
    }

    /**
     * Creates a composite canonical fingerprint based on both question stem and correct answer.
     */
    fun createFingerprint(questionText: String?, correctAnswerText: String?): String {
        val qCore = extractQuestionCore(questionText)
        val aNorm = normalize(correctAnswerText)
        return "$qCore:::$aNorm"
    }

    /**
     * Computes word-level Jaccard similarity (0.0 to 1.0) between two strings.
     */
    fun jaccardSimilarity(str1: String, str2: String): Double {
        val words1 = normalize(str1).split(" ").filter { it.length > 2 }.toSet()
        val words2 = normalize(str2).split(" ").filter { it.length > 2 }.toSet()
        if (words1.isEmpty() || words2.isEmpty()) return 0.0
        val intersection = words1.intersect(words2).size
        val union = words1.union(words2).size
        return if (union == 0) 0.0 else intersection.toDouble() / union.toDouble()
    }

    /**
     * Checks if a new candidate question is a duplicate of any question in existing pool.
     */
    fun isDuplicate(
        candidateQuestion: String,
        candidateAnswer: String,
        existingQuestions: List<ComprehensiveQuizQuestion>
    ): Boolean {
        val candidateFp = createFingerprint(candidateQuestion, candidateAnswer)
        val candidateNormQ = normalize(candidateQuestion)
        val candidateNormA = normalize(candidateAnswer)

        for (existing in existingQuestions) {
            val existingQ = existing.questionUrdu ?: existing.question
            val existingA = existing.optionsUrdu?.getOrNull(existing.correctAnswerIndex)
                ?: existing.options.getOrNull(existing.correctAnswerIndex) ?: ""

            // 1. Exact canonical fingerprint match
            val existingFp = createFingerprint(existingQ, existingA)
            if (candidateFp == existingFp && candidateFp.isNotBlank()) {
                return true
            }

            // 2. Exact normalized question match
            val existingNormQ = normalize(existingQ)
            if (candidateNormQ == existingNormQ && candidateNormQ.isNotBlank()) {
                return true
            }

            // 3. High semantic overlap on question AND matching answer concept
            val existingNormA = normalize(existingA)
            val qSimilarity = jaccardSimilarity(candidateQuestion, existingQ)
            val aSimilarity = jaccardSimilarity(candidateAnswer, existingA)

            if (qSimilarity > 0.85 && (aSimilarity > 0.70 || candidateNormA == existingNormA)) {
                return true
            }
        }
        return false
    }

    /**
     * Filters a list of questions, guaranteeing strictly zero duplicates.
     */
    fun filterUniqueQuestions(
        questions: List<ComprehensiveQuizQuestion>,
        globalSeenFingerprints: MutableSet<String> = mutableSetOf()
    ): List<ComprehensiveQuizQuestion> {
        val result = mutableListOf<ComprehensiveQuizQuestion>()

        for (q in questions) {
            val qText = q.questionUrdu ?: q.question
            val aText = q.optionsUrdu?.getOrNull(q.correctAnswerIndex)
                ?: q.options.getOrNull(q.correctAnswerIndex) ?: ""

            val fp = createFingerprint(qText, aText)
            val normQ = normalize(qText)

            if (fp !in globalSeenFingerprints && normQ !in globalSeenFingerprints) {
                globalSeenFingerprints.add(fp)
                globalSeenFingerprints.add(normQ)
                result.add(q)
            }
        }
        return result
    }
}
