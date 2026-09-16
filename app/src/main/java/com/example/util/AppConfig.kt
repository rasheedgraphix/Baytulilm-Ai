package com.example.util

import android.content.Context
import android.content.Intent

object AppConfig {
    /**
     * Cloudflare Workers Free Serverless Proxy URL for Gemini AI
     * Deployed Worker URL
     */
    const val CLOUDFLARE_WORKER_URL = "https://baytul-ilm-ai-proxy.hafiznoumanurrasheed4.workers.dev/"

    /**
     * Direct API Keys for Silent AI Engines (No UI prompt needed)
     */
    const val DEFAULT_GROQ_KEY: String = "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"
    const val DEFAULT_GEMINI_KEY: String = "AQ.Ab8RN6LTkYc5LsZrkhhNQSVvE5-7rI637sImctpM6vfPhHWvDg"
    const val DEFAULT_MISTRAL_KEY: String = "CZxC0Kk5wBeJv1xcRLnlCN60eYeaIwbX"
    const val DEFAULT_OPENROUTER_KEY: String = ""

    const val OFFICIAL_WEBSITE_URL = "https://rasheedgraphix.github.io/baytul-ilm-website/"
    const val APP_NAME_URDU = "بیت العلم AI"
    const val APP_TAGLINE_URDU = "اسلامی تعلیم کا جدید پلیٹ فارم"

    const val SHARE_MESSAGE = """بیت العلم AI — اسلامی تعلیم کا جدید پلیٹ فارم

دینی کتب، قرآن، دعائیں، Quiz، AI Assistant اور دیگر اسلامی تعلیمی سہولیات ایک ہی app میں۔

Website:
$OFFICIAL_WEBSITE_URL"""

    fun shareAppWithWebsite(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "$APP_NAME_URDU — $APP_TAGLINE_URDU")
                putExtra(Intent.EXTRA_TEXT, SHARE_MESSAGE)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
