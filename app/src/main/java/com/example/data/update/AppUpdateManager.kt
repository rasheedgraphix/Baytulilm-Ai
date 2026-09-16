package com.example.data.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class AppUpdateInfo(
    val isUpdateAvailable: Boolean = false,
    val isForceUpdate: Boolean = false,
    val latestVersionName: String = "",
    val latestVersionCode: Int = 0,
    val updateTitle: String = "نیا ورژن دستیاب ہے",
    val updateMessage: String = "بہتر کارکردگی اور نئے فیچرز کے لیے براہِ کرم ایپ کو اپ ڈیٹ کریں۔",
    val downloadUrl: String = ""
)

object AppUpdateManager {
    private const val TAG = "AppUpdateManager"
    
    // TODO: Update this URL to point to your actual raw JSON file on GitHub
    private const val GITHUB_JSON_URL = "https://raw.githubusercontent.com/rasheedgraphix/Baytulilm-Ai/refs/heads/main/update_info.json"

    /**
     * Fetches update info from a GitHub JSON file instead of Firestore.
     */
    suspend fun checkForAppUpdate(context: Context): AppUpdateInfo {
        return withContext(Dispatchers.IO) {
            try {
                val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(context.packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0))
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }

                val currentVersionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode.toInt()
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo.versionCode
                }
                val currentVersionName = packageInfo.versionName ?: "1.4.3"

                // Fetch JSON from GitHub
                val connection = URL(GITHUB_JSON_URL).openConnection() as java.net.HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(jsonString)

                val latestVersionCode = json.optInt("latest_version_code", currentVersionCode)
                val latestVersionName = json.optString("latest_version_name", currentVersionName)
                val minSupportedVersionCode = json.optInt("min_version_code", 1)
                val updateTitle = json.optString("title", "نیا ورژن $latestVersionName دستیاب ہے")
                val updateMessage = json.optString("message", "بہتر تجربے اور نئے فیچرز کے لیے ابھی ایپ اپ ڈیٹ کریں۔")
                val downloadUrl = json.optString("download_url", "")

                val hasNewerVersion = latestVersionCode > currentVersionCode
                val isForce = currentVersionCode < minSupportedVersionCode

                AppUpdateInfo(
                    isUpdateAvailable = hasNewerVersion,
                    isForceUpdate = isForce,
                    latestVersionName = latestVersionName,
                    latestVersionCode = latestVersionCode,
                    updateTitle = updateTitle,
                    updateMessage = updateMessage,
                    downloadUrl = downloadUrl
                )
            } catch (e: Exception) {
                Log.e(TAG, "Update check skipped or failed: ${e.message}")
                AppUpdateInfo(isUpdateAvailable = false)
            }
        }
    }

    fun openUpdateUrl(context: Context, url: String) {
        try {
            val targetUrl = if (url.isNotBlank()) url else "https://github.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open update URL: ${e.message}")
        }
    }
}
