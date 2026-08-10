package com.example.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseSecurityAndAnalytics {

    private const val TAG = "FirebaseSecurity"

    fun initAppCheck(context: Context) {
        try {
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            if (BuildConfig.DEBUG) {
                firebaseAppCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "App Check initialized with DebugAppCheckProviderFactory")
            } else {
                firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "App Check initialized with PlayIntegrityAppCheckProviderFactory")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase App Check", e)
            FirebaseCrashlyticsLogger.logException(e, "AppCheckInitialization")
        }
    }
}

object FirebaseAnalyticsHelper {
    private fun getAnalytics(context: Context): FirebaseAnalytics? {
        return runCatching { FirebaseAnalytics.getInstance(context) }.getOrNull()
    }

    fun logEvent(context: Context, eventName: String, params: Map<String, Any> = emptyMap()) {
        try {
            val analytics = getAnalytics(context) ?: return
            val bundle = Bundle().apply {
                params.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
            analytics.logEvent(eventName, bundle)
        } catch (e: Exception) {
            Log.e("Analytics", "Failed to log event: $eventName", e)
        }
    }

    fun logLogin(context: Context, method: String) = logEvent(context, FirebaseAnalytics.Event.LOGIN, mapOf(FirebaseAnalytics.Param.METHOD to method))
    fun logRegistration(context: Context, method: String) = logEvent(context, FirebaseAnalytics.Event.SIGN_UP, mapOf(FirebaseAnalytics.Param.METHOD to method))
    fun logBookOpen(context: Context, bookId: String, title: String) = logEvent(context, "book_open", mapOf("book_id" to bookId, "title" to title))
    fun logPdfRead(context: Context, pdfId: String, pageNumber: Int) = logEvent(context, "pdf_read", mapOf("pdf_id" to pdfId, "page_number" to pageNumber))
    fun logDownload(context: Context, itemId: String, itemType: String) = logEvent(context, "download", mapOf("item_id" to itemId, "item_type" to itemType))
    fun logAiChat(context: Context, topic: String) = logEvent(context, "ai_chat", mapOf("topic" to topic))
    fun logQuizStart(context: Context, quizId: String) = logEvent(context, "quiz_start", mapOf("quiz_id" to quizId))
    fun logQuizComplete(context: Context, quizId: String, score: Int) = logEvent(context, "quiz_complete", mapOf("quiz_id" to quizId, "score" to score))
    fun logPrayerTime(context: Context, city: String) = logEvent(context, "prayer_time", mapOf("city" to city))
    fun logQiblaUsage(context: Context) = logEvent(context, "qibla_usage")
    fun logLanguageChange(context: Context, language: String) = logEvent(context, "language_change", mapOf("language" to language))
    fun logThemeChange(context: Context, theme: String) = logEvent(context, "theme_change", mapOf("theme" to theme))
}

object FirebaseCrashlyticsLogger {
    private val crashlytics: FirebaseCrashlytics?
        get() = runCatching { FirebaseCrashlytics.getInstance() }.getOrNull()

    fun logException(throwable: Throwable, category: String, keyValues: Map<String, String> = emptyMap()) {
        try {
            val instance = crashlytics ?: return
            instance.setCustomKey("error_category", category)
            keyValues.forEach { (key, value) ->
                instance.setCustomKey(key, value)
            }
            instance.recordException(throwable)
        } catch (e: Exception) {
            Log.e("CrashlyticsLogger", "Failed to record exception", e)
        }
    }

    fun logAuthFailure(throwable: Throwable, email: String) {
        logException(throwable, "AuthenticationFailure", mapOf("email" to email))
    }

    fun logAiError(throwable: Throwable, promptSnippet: String) {
        logException(throwable, "AiError", mapOf("prompt_snippet" to promptSnippet.take(50)))
    }

    fun logPdfCrash(throwable: Throwable, pdfPath: String) {
        logException(throwable, "PdfReaderCrash", mapOf("pdf_path" to pdfPath))
    }

    fun logFirebaseException(throwable: Throwable, service: String) {
        logException(throwable, "FirebaseException", mapOf("service" to service))
    }

    fun logNetworkFailure(throwable: Throwable, url: String) {
        logException(throwable, "NetworkFailure", mapOf("url" to url))
    }
}
