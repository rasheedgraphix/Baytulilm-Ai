package com.example.data.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BaytulIlmFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM token: $token")
        if (token.isBlank()) return
        try {
            if (isGooglePlayServicesFullyUsable(this)) {
                val instance = FirebaseMessaging.getInstance()
                instance.subscribeToTopic("all_users")
                    .addOnFailureListener { Log.d(TAG, "Topic 'all_users' skipped: ${it.message}") }
                instance.subscribeToTopic("updates")
                    .addOnFailureListener { Log.d(TAG, "Topic 'updates' skipped: ${it.message}") }
            }
        } catch (e: Throwable) {
            Log.d(TAG, "FCM topic subscription skipped on this environment: ${e.message}")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "FCM Message received from: ${remoteMessage.from}")

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "بیت العلم AI — نیا اپڈیٹ"
            
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: remoteMessage.data["message"]
            ?: "نیا ورژن دستیاب ہے!"

        val updateUrl = remoteMessage.data["update_url"]
            ?: remoteMessage.data["url"]
            ?: remoteMessage.data["download_url"]

        showNotification(title, body, updateUrl)
    }

    private fun showNotification(title: String, message: String, updateUrl: String?) {
        val channelId = "baytul_ilm_updates"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "App Updates & Announcements",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new versions, books, and announcements"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = if (!updateUrl.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    companion object {
        private const val TAG = "BaytulIlmFCM"

        fun isGooglePlayServicesFullyUsable(context: Context): Boolean {
            return try {
                // Running on headless / emulator / preview environments where GMS broker lacks package authentication or has registration limits
                val isEmulator = Build.FINGERPRINT.startsWith("generic") ||
                        Build.FINGERPRINT.startsWith("unknown") ||
                        Build.FINGERPRINT.contains("vbox") ||
                        Build.FINGERPRINT.contains("test-keys") ||
                        Build.MODEL.contains("google_sdk") ||
                        Build.MODEL.contains("Emulator") ||
                        Build.MODEL.contains("Android SDK built for x86") ||
                        Build.MODEL.contains("sdk_gphone") ||
                        Build.HARDWARE.contains("goldfish") ||
                        Build.HARDWARE.contains("ranchu") ||
                        Build.PRODUCT.contains("sdk") ||
                        Build.PRODUCT.contains("emulator") ||
                        Build.PRODUCT.contains("simulator") ||
                        Build.BOARD == "QC_Reference_Phone" ||
                        Build.MANUFACTURER.contains("Genymotion") ||
                        Build.HOST.startsWith("Build") ||
                        (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))

                if (isEmulator) {
                    Log.d(TAG, "Running in emulator/preview container; skipping FCM token/topic sync to prevent TOO_MANY_REGISTRATIONS.")
                    return false
                }

                val pm = context.packageManager
                val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo("com.google.android.gms", android.content.pm.PackageManager.PackageInfoFlags.of(0))
                } else {
                    @Suppress("DEPRECATION")
                    pm.getPackageInfo("com.google.android.gms", 0)
                }
                val appInfo = pkgInfo?.applicationInfo
                if (appInfo == null || !appInfo.enabled) return false

                val gApi = GoogleApiAvailability.getInstance()
                gApi.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
            } catch (t: Throwable) {
                Log.d(TAG, "Google Play Services verification skipped: ${t.message}")
                false
            }
        }

        fun initializeFCM(context: Context? = null) {
            try {
                if (context == null) return

                if (!isGooglePlayServicesFullyUsable(context)) {
                    Log.d(TAG, "Google Play Services not usable or running on emulator, FCM initialization safely skipped.")
                    return
                }

                if (com.google.firebase.FirebaseApp.getApps(context).isEmpty()) {
                    Log.d(TAG, "FirebaseApp not initialized, skipping FCM setup.")
                    return
                }
                
                val instance = FirebaseMessaging.getInstance()
                instance.isAutoInitEnabled = true
                instance.token.addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful && !task.result.isNullOrBlank()) {
                            Log.d(TAG, "FCM token retrieved successfully: ${task.result}")
                            instance.subscribeToTopic("all_users")
                                .addOnFailureListener { e -> Log.d(TAG, "FCM all_users subscribe skipped: ${e.message}") }
                            instance.subscribeToTopic("updates")
                                .addOnFailureListener { e -> Log.d(TAG, "FCM updates subscribe skipped: ${e.message}") }
                        } else {
                            Log.d(TAG, "FCM token retrieval skipped or not available on this device: ${task.exception?.message}")
                        }
                    } catch (t: Throwable) {
                        Log.d(TAG, "FCM token task handler caught error: ${t.message}")
                    }
                }
            } catch (t: Throwable) {
                Log.d(TAG, "FCM initialization safely handled: ${t.message}")
            }
        }
    }
}
