package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.util.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * MultiAiEngineManager
 * 
 * Manages 5 silent, high-capacity AI Providers behind the scenes with instant auto-fallback:
 * 1. Groq (Llama 3.3 / 3.1) - 14,400 free requests/day (Ultra Fast)
 * 2. Google Gemini (Gemini Flash) - 1,500 free requests/day (Deep Scholarly Reasoning)
 * 3. Mistral AI (Mistral Small) - 3,000-5,000 free requests/day
 * 4. Cloudflare Workers AI - 10,000 free requests/day
 * 5. OpenRouter / Cohere - 1,000+ free requests/day
 * 
 * User Privacy & Branding Guarantee:
 * - Silent Auto-Fallback: Users NEVER see which provider answered.
 * - Identity Sanitization: Strips external AI names (Meta, Llama, Mistral, Google, etc.)
 *   and presents the response solely under "Baytul Ilm AI Scholar" (بیت العلم AI).
 */
class MultiAiEngineManager(private val context: Context? = null) {

    private val TAG = "MultiAiEngineManager"

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    companion object {
        private const val PREFS_NAME = "baytulilm_settings_prefs"
        const val KEY_GEMINI = "custom_gemini_api_key"
        const val KEY_GROQ = "custom_groq_api_key"
        const val KEY_MISTRAL = "custom_mistral_api_key"
        const val KEY_CLOUDFLARE_AI = "custom_cloudflare_ai_key"
        const val KEY_OPENROUTER = "custom_openrouter_api_key"

        fun getApiKey(context: Context?, keyName: String): String? {
            if (context == null) return null
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val v = prefs.getString(keyName, null)?.trim()
            return if (!v.isNullOrBlank()) v else null
        }

        fun saveApiKey(context: Context?, keyName: String, value: String?) {
            if (context == null) return
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            if (value.isNullOrBlank()) {
                prefs.edit().remove(keyName).apply()
            } else {
                prefs.edit().putString(keyName, value.trim()).apply()
            }
        }

        fun getActiveKeysCount(context: Context?): Int {
            if (context == null) return 0
            val keys = listOf(KEY_GROQ, KEY_GEMINI, KEY_MISTRAL, KEY_CLOUDFLARE_AI, KEY_OPENROUTER)
            return keys.count { !getApiKey(context, it).isNullOrBlank() }
        }

        fun hasAnyCustomKey(context: Context?): Boolean {
            return getActiveKeysCount(context) > 0
        }
    }

    /**
     * Executes the query silently across the 5 engines with automatic fallback.
     * Preserves strict Islamic System Prompt, RAG Library Context, and Citations.
     */
    suspend fun executeWithSilentFallback(
        systemPrompt: String,
        userPrompt: String,
        libraryContext: String,
        history: List<Pair<String, String>>,
        rawGeminiPayload: JSONObject,
        userId: String
    ): Result<String> = withContext(Dispatchers.IO) {

        val groqKey = getApiKey(context, KEY_GROQ) ?: com.example.util.AppConfig.DEFAULT_GROQ_KEY.ifBlank { null }
        val geminiKey = getApiKey(context, KEY_GEMINI) 
            ?: com.example.util.AppConfig.DEFAULT_GEMINI_KEY.ifBlank { null }
            ?: runCatching { com.example.BuildConfig.GEMINI_API_KEY }.getOrNull()?.trim()
        val mistralKey = getApiKey(context, KEY_MISTRAL) ?: com.example.util.AppConfig.DEFAULT_MISTRAL_KEY.ifBlank { null }
        val openRouterKey = getApiKey(context, KEY_OPENROUTER) ?: com.example.util.AppConfig.DEFAULT_OPENROUTER_KEY.ifBlank { null }

        // 1. Try Groq (Llama 3.3 70B / 3.1 8B) - Fastest (~500 tokens/s)
        if (!groqKey.isNullOrBlank()) {
            val groqResult = callGroqApi(groqKey, systemPrompt, userPrompt, libraryContext, history)
            if (groqResult.isSuccess && groqResult.getOrNull()?.isNotBlank() == true) {
                Log.d(TAG, "Engine 1 (Groq) responded successfully.")
                return@withContext Result.success(sanitizeOutput(groqResult.getOrNull()!!))
            } else {
                Log.w(TAG, "Engine 1 (Groq) failed, silently falling back to Engine 2. Error: ${groqResult.exceptionOrNull()?.message}")
            }
        }

        // 2. Try Direct Google Gemini API (Gemini Flash models)
        if (!geminiKey.isNullOrBlank()) {
            val geminiResult = callDirectGemini(geminiKey, rawGeminiPayload)
            if (geminiResult.isSuccess && geminiResult.getOrNull()?.isNotBlank() == true) {
                Log.d(TAG, "Engine 2 (Google Gemini) responded successfully.")
                return@withContext Result.success(sanitizeOutput(geminiResult.getOrNull()!!))
            } else {
                Log.w(TAG, "Engine 2 (Google Gemini) failed, silently falling back to Engine 3. Error: ${geminiResult.exceptionOrNull()?.message}")
            }
        }

        // 3. Try Mistral AI
        if (!mistralKey.isNullOrBlank()) {
            val mistralResult = callMistralApi(mistralKey, systemPrompt, userPrompt, libraryContext, history)
            if (mistralResult.isSuccess && mistralResult.getOrNull()?.isNotBlank() == true) {
                Log.d(TAG, "Engine 3 (Mistral) responded successfully.")
                return@withContext Result.success(sanitizeOutput(mistralResult.getOrNull()!!))
            } else {
                Log.w(TAG, "Engine 3 (Mistral) failed, silently falling back to Engine 4. Error: ${mistralResult.exceptionOrNull()?.message}")
            }
        }

        // 4. Try OpenRouter (Multi-model free tier: Llama, DeepSeek, etc.)
        if (!openRouterKey.isNullOrBlank()) {
            val openRouterResult = callOpenRouterApi(openRouterKey, systemPrompt, userPrompt, libraryContext, history)
            if (openRouterResult.isSuccess && openRouterResult.getOrNull()?.isNotBlank() == true) {
                Log.d(TAG, "Engine 4 (OpenRouter) responded successfully.")
                return@withContext Result.success(sanitizeOutput(openRouterResult.getOrNull()!!))
            } else {
                Log.w(TAG, "Engine 4 (OpenRouter) failed, silently falling back to default Cloudflare proxy. Error: ${openRouterResult.exceptionOrNull()?.message}")
            }
        }

        // 5. Default Free Serverless Proxy (Cloudflare Worker Gemini Proxy)
        Log.d(TAG, "Calling Default Cloudflare Worker Proxy backend...")
        val proxyResult = callCloudflareWorkerProxy(rawGeminiPayload, userId)
        if (proxyResult.isSuccess && proxyResult.getOrNull()?.isNotBlank() == true) {
            return@withContext Result.success(sanitizeOutput(proxyResult.getOrNull()!!))
        }

        Result.failure(proxyResult.exceptionOrNull() ?: Exception("All AI engines are temporarily busy. Switching to internal knowledge."))
    }

    // ==========================================
    // 1. GROQ API IMPLEMENTATION (OpenAI format)
    // ==========================================
    private fun callGroqApi(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        libraryContext: String,
        history: List<Pair<String, String>>
    ): Result<String> {
        val models = listOf("llama-3.3-70b-versatile", "llama-3.1-8b-instant", "mixtral-8x7b-32768")
        for (model in models) {
            try {
                val messagesArray = JSONArray()

                // System message
                messagesArray.put(JSONObject().apply {
                    put("role", "system")
                    put("content", "$systemPrompt\n\n=== RETRIEVED LIBRARY CONTEXT ===\n$libraryContext")
                })

                // History
                history.forEach { (user, modelResp) ->
                    if (user.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "user"); put("content", user) })
                    if (modelResp.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "assistant"); put("content", modelResp) })
                }

                // User prompt
                messagesArray.put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })

                val payload = JSONObject().apply {
                    put("model", model)
                    put("messages", messagesArray)
                    put("temperature", 0.3)
                    put("max_tokens", 2048)
                }

                val req = Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .build()

                val resp = httpClient.newCall(req).execute()
                val body = resp.body?.string() ?: ""
                if (resp.isSuccessful) {
                    val json = JSONObject(body)
                    val choices = json.optJSONArray("choices")
                    val message = choices?.optJSONObject(0)?.optJSONObject("message")
                    val content = message?.optString("content")?.trim() ?: ""
                    if (content.isNotBlank()) return Result.success(content)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Groq call failed with model $model: ${e.message}")
            }
        }
        return Result.failure(Exception("Groq unavailable"))
    }

    // ==========================================
    // 2. DIRECT GOOGLE GEMINI API
    // ==========================================
    private fun callDirectGemini(apiKey: String, payload: JSONObject): Result<String> {
        val candidateModels = listOf("gemini-2.5-flash", "gemini-2.0-flash", "gemini-1.5-flash", "gemini-flash-latest")
        for (model in candidateModels) {
            try {
                val directUrl = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
                val req = Request.Builder()
                    .url(directUrl)
                    .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-goog-api-key", apiKey)
                    .build()

                val resp = httpClient.newCall(req).execute()
                val body = resp.body?.string() ?: ""
                if (resp.isSuccessful) {
                    val parsed = extractTextFromGeminiJson(body)
                    if (parsed.isNotBlank()) return Result.success(parsed)
                } else {
                    Log.w(TAG, "Gemini model $model returned code ${resp.code}: $body")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Direct Gemini call failed with model $model: ${e.message}")
            }
        }
        return Result.failure(Exception("Direct Gemini unavailable"))
    }

    // ==========================================
    // 3. MISTRAL AI IMPLEMENTATION
    // ==========================================
    private fun callMistralApi(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        libraryContext: String,
        history: List<Pair<String, String>>
    ): Result<String> {
        val models = listOf("mistral-small-latest", "open-mistral-7b")
        for (model in models) {
            try {
                val messagesArray = JSONArray()

                messagesArray.put(JSONObject().apply {
                    put("role", "system")
                    put("content", "$systemPrompt\n\n=== RETRIEVED LIBRARY CONTEXT ===\n$libraryContext")
                })

                history.forEach { (user, modelResp) ->
                    if (user.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "user"); put("content", user) })
                    if (modelResp.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "assistant"); put("content", modelResp) })
                }

                messagesArray.put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })

                val payload = JSONObject().apply {
                    put("model", model)
                    put("messages", messagesArray)
                    put("temperature", 0.3)
                    put("max_tokens", 2048)
                }

                val req = Request.Builder()
                    .url("https://api.mistral.ai/v1/chat/completions")
                    .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .build()

                val resp = httpClient.newCall(req).execute()
                val body = resp.body?.string() ?: ""
                if (resp.isSuccessful) {
                    val json = JSONObject(body)
                    val choices = json.optJSONArray("choices")
                    val message = choices?.optJSONObject(0)?.optJSONObject("message")
                    val content = message?.optString("content")?.trim() ?: ""
                    if (content.isNotBlank()) return Result.success(content)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Mistral call failed with model $model: ${e.message}")
            }
        }
        return Result.failure(Exception("Mistral unavailable"))
    }

    // ==========================================
    // 4. OPENROUTER / DEEPSEEK FREE TIER
    // ==========================================
    private fun callOpenRouterApi(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        libraryContext: String,
        history: List<Pair<String, String>>
    ): Result<String> {
        val models = listOf(
            "meta-llama/llama-3.3-70b-instruct:free",
            "deepseek/deepseek-r1:free",
            "google/gemini-2.0-flash-lite-preview-02-05:free"
        )
        for (model in models) {
            try {
                val messagesArray = JSONArray()

                messagesArray.put(JSONObject().apply {
                    put("role", "system")
                    put("content", "$systemPrompt\n\n=== RETRIEVED LIBRARY CONTEXT ===\n$libraryContext")
                })

                history.forEach { (user, modelResp) ->
                    if (user.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "user"); put("content", user) })
                    if (modelResp.isNotBlank()) messagesArray.put(JSONObject().apply { put("role", "assistant"); put("content", modelResp) })
                }

                messagesArray.put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })

                val payload = JSONObject().apply {
                    put("model", model)
                    put("messages", messagesArray)
                    put("temperature", 0.3)
                    put("max_tokens", 2048)
                }

                val req = Request.Builder()
                    .url("https://openrouter.ai/api/v1/chat/completions")
                    .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("HTTP-Referer", "https://baytulilm.app")
                    .addHeader("X-Title", "Baytul Ilm AI")
                    .addHeader("Content-Type", "application/json")
                    .build()

                val resp = httpClient.newCall(req).execute()
                val body = resp.body?.string() ?: ""
                if (resp.isSuccessful) {
                    val json = JSONObject(body)
                    val choices = json.optJSONArray("choices")
                    val message = choices?.optJSONObject(0)?.optJSONObject("message")
                    val content = message?.optString("content")?.trim() ?: ""
                    if (content.isNotBlank()) return Result.success(content)
                }
            } catch (e: Exception) {
                Log.w(TAG, "OpenRouter call failed with model $model: ${e.message}")
            }
        }
        return Result.failure(Exception("OpenRouter unavailable"))
    }

    // ==========================================
    // 5. DEFAULT CLOUDFLARE WORKER PROXY
    // ==========================================
    private fun callCloudflareWorkerProxy(payload: JSONObject, userId: String): Result<String> {
        return try {
            val endpoint = AppConfig.CLOUDFLARE_WORKER_URL.trim().trimEnd('/')
            val req = Request.Builder()
                .url(endpoint)
                .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-App-ID", "com.baytulilmai.app")
                .addHeader("X-User-ID", userId)
                .build()

            val resp = httpClient.newCall(req).execute()
            val body = resp.body?.string() ?: ""
            if (resp.isSuccessful) {
                val parsed = extractTextFromGeminiJson(body)
                if (parsed.isNotBlank()) Result.success(parsed)
                else Result.failure(Exception("خالی جواب موصول ہوا۔"))
            } else {
                Result.failure(Exception("پراکسی سرور نے اسٹیٹس ${resp.code} بھیجا۔"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractTextFromGeminiJson(responseBody: String): String {
        return try {
            val trimmed = responseBody.trim()
            if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) return trimmed

            val json = JSONObject(trimmed)
            if (json.has("error")) {
                return ""
            }

            val candidates = json.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val sb = StringBuilder()
                    for (i in 0 until parts.length()) {
                        val partObj = parts.getJSONObject(i)
                        sb.append(partObj.optString("text", ""))
                    }
                    val text = sb.toString().trim()
                    if (text.isNotBlank() && !text.startsWith("{\"error\"")) return text
                }
            }

            val directText = json.optString("text")
            if (directText.isNotBlank() && !directText.trim().startsWith("{")) return directText

            ""
        } catch (e: Exception) {
            if (!responseBody.trim().startsWith("{") && responseBody.isNotBlank()) responseBody else ""
        }
    }

    /**
     * Sanitizes external AI branding so that user always experiences 100% "بیت العلم AI".
     */
    private fun sanitizeOutput(rawText: String): String {
        var text = rawText
        val patternsToReplace = listOf(
            Regex("(?i)I am (a|an) (AI|language model) (developed|created|trained) by Meta"),
            Regex("(?i)I am (a|an) (AI|language model) (developed|created|trained) by Google"),
            Regex("(?i)I am (a|an) (AI|language model) (developed|created|trained) by Mistral AI"),
            Regex("(?i)I am (a|an) (AI|language model) (developed|created|trained) by OpenAI"),
            Regex("(?i)I am Llama(,? (developed|created|trained) by Meta)?"),
            Regex("(?i)I am Gemini(,? (developed|created|trained) by Google)?"),
            Regex("(?i)I am Mistral(,? (developed|created|trained) by Mistral AI)?"),
            Regex("(?i)As a language model created by (Google|Meta|Mistral|OpenAI)"),
            Regex("(?i)As an AI trained by (Google|Meta|Mistral|OpenAI)")
        )

        for (pattern in patternsToReplace) {
            text = text.replace(pattern, "میں بیت العلم AI کا علمی معاون ہوں")
        }

        return text
    }
}
