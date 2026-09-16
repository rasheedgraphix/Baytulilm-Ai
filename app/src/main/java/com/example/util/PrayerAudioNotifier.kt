package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrayerAudioNotifier {
    private const val TAG = "PrayerAudioNotifier"
    private const val PREFS_NAME = "baytul_ilm_audio_prefs"
    private const val KEY_PRAYER_SOUND_ENABLED = "play_allahu_akbar_on_prayer"
    private const val KEY_LAST_TRIGGERED_PREFIX = "last_triggered_prayer_"
    private const val CHANNEL_ID = "prayer_time_channel"
    private const val UTTERANCE_ID = "PrayerAllahuAkbarMaleUtterance"

    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _isPlayingAllahuAkbar = MutableStateFlow(false)
    val isPlayingAllahuAkbar: StateFlow<Boolean> = _isPlayingAllahuAkbar.asStateFlow()

    private val _currentPrayerAlert = MutableStateFlow<String?>(null)
    val currentPrayerAlert: StateFlow<String?> = _currentPrayerAlert.asStateFlow()

    fun isPrayerSoundEnabled(context: Context): Boolean {
        val prefs = getPrefs(context)
        return prefs.getBoolean(KEY_PRAYER_SOUND_ENABLED, true)
    }

    fun setPrayerSoundEnabled(context: Context, enabled: Boolean) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_PRAYER_SOUND_ENABLED, enabled).apply()
        if (!enabled) {
            dismissAlert(context)
        }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Checks list of calculated prayer times and plays Allahu Akbar if a prayer time has just started.
     */
    fun checkAndTriggerPrayerTime(context: Context, prayerTimes: List<PrayerTimeData>) {
        if (!isPrayerSoundEnabled(context)) return

        val now = System.currentTimeMillis()
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date(now))

        // Exclude sunrise since it is not a prayer entry time
        val actualPrayers = prayerTimes.filter { it.id != "sunrise" }

        for (prayer in actualPrayers) {
            val prayerTimeMs = prayer.dateObj.time
            // Check if current time is within [prayerTimeMs, prayerTimeMs + 70 seconds]
            val diff = now - prayerTimeMs
            if (diff in 0..75_000) {
                val key = "${KEY_LAST_TRIGGERED_PREFIX}${todayStr}_${prayer.id}"
                val alreadyTriggered = getPrefs(context).getBoolean(key, false)
                if (!alreadyTriggered) {
                    getPrefs(context).edit().putBoolean(key, true).apply()
                    triggerPrayerAlert(context, prayer.nameUrdu, prayer.nameEnglish, prayer.id)
                    break
                }
            }
        }
    }

    /**
     * Triggers the full alert: Plays Allahu Akbar audio, updates UI banner, and posts system notification.
     */
    fun triggerPrayerAlert(
        context: Context,
        prayerNameUrdu: String,
        prayerNameEnglish: String = "",
        prayerId: String = ""
    ) {
        if (!isPrayerSoundEnabled(context)) return

        if (prayerId.isNotBlank()) {
            val todayStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
            val key = "${KEY_LAST_TRIGGERED_PREFIX}${todayStr}_${prayerId}"
            getPrefs(context).edit().putBoolean(key, true).apply()
        }

        _currentPrayerAlert.value = "وقتِ نماز: $prayerNameUrdu ($prayerNameEnglish)"
        playAllahuAkbar(context, prayerNameUrdu)
        showPrayerNotification(context, prayerNameUrdu)
    }

    fun playAllahuAkbar(context: Context, prayerNameUrdu: String = "") {
        stop()

        // 1. Try playing custom raw Azan audio (azan_audio.mp3) if available
        if (playAzanRawAudio(context)) {
            return
        }

        // 2. Fallback to Arabic Qari Text-to-Speech
        playTtsOrFallback(context, prayerNameUrdu)
    }

    private fun playAzanRawAudio(context: Context): Boolean {
        return try {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val mp = MediaPlayer.create(context, R.raw.azan_audio, audioAttributes, 0)
            if (mp != null && mp.duration > 0) {
                mp.setVolume(1.0f, 1.0f)
                mp.setOnCompletionListener {
                    _isPlayingAllahuAkbar.value = false
                    releasePlayer()
                }
                mp.setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "azan_audio MediaPlayer error: $what, $extra. Falling back to TTS.")
                    _isPlayingAllahuAkbar.value = false
                    releasePlayer()
                    playTtsOrFallback(context, "")
                    true
                }
                mediaPlayer = mp
                mp.start()
                _isPlayingAllahuAkbar.value = true
                Log.d(TAG, "Successfully playing azan_audio.mp3")
                true
            } else {
                mp?.release()
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error trying to play azan_audio.mp3: ${e.message}")
            false
        }
    }

    private fun playTtsOrFallback(context: Context, prayerNameUrdu: String) {
        try {
            if (tts == null) {
                tts = TextToSpeech(context.applicationContext) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        isTtsReady = true
                        setupUtteranceListener()
                        setupTtsMaleVoice()
                        speakAllahuAkbarMale(context, prayerNameUrdu)
                    } else {
                        playFallbackMedia(context)
                    }
                }
            } else {
                setupUtteranceListener()
                if (isTtsReady) {
                    setupTtsMaleVoice()
                    speakAllahuAkbarMale(context, prayerNameUrdu)
                } else {
                    playFallbackMedia(context)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Male voice recitation error: ${e.message}", e)
            playFallbackMedia(context)
        }
    }

    private fun setupUtteranceListener() {
        try {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlayingAllahuAkbar.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isPlayingAllahuAkbar.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isPlayingAllahuAkbar.value = false
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error setting utterance listener: ${e.message}")
        }
    }

    private fun setupTtsMaleVoice() {
        val t = tts ?: return
        try {
            val arLocale = Locale("ar")
            val langResult = t.setLanguage(arLocale)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                t.language = Locale.getDefault()
            }

            val voices: Set<Voice>? = t.voices
            if (!voices.isNullOrEmpty()) {
                val maleArabicVoice = voices.find { voice ->
                    val name = voice.name.lowercase()
                    val isAr = voice.locale.language == "ar"
                    val isMale = name.contains("male") ||
                            name.contains("man") ||
                            name.contains("masc") ||
                            name.contains("#male") ||
                            name.contains("ar-xa-x-arz-local") ||
                            name.contains("ar_xa") ||
                            voice.features.any { it.contains("male", ignoreCase = true) }
                    isAr && isMale
                } ?: voices.find { voice ->
                    val name = voice.name.lowercase()
                    voice.locale.language == "ar" && !name.contains("female") && !name.contains("woman")
                } ?: voices.find { voice ->
                    val name = voice.name.lowercase()
                    name.contains("male") || name.contains("man")
                }

                if (maleArabicVoice != null) {
                    t.voice = maleArabicVoice
                    Log.d(TAG, "Selected Male voice for Allahu Akbar: ${maleArabicVoice.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring male voice for Allahu Akbar: ${e.message}")
        }

        // 0.76f gives a deep, sacred Qari tone for Allahu Akbar recitation
        t.setPitch(0.76f)
        t.setSpeechRate(0.80f)
    }

    private fun speakAllahuAkbarMale(context: Context, prayerNameUrdu: String) {
        val text = "اَللّٰهُ أَكْبَرُ اَللّٰهُ أَكْبَرُ ، اَللّٰهُ أَكْبَرُ اَللّٰهُ أَكْبَرُ ، حَيَّ عَلَى الصَّلَاةِ"
        _isPlayingAllahuAkbar.value = true
        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
        if (result == TextToSpeech.ERROR) {
            playFallbackMedia(context)
        }
    }

    private fun playFallbackMedia(context: Context) {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val mp = MediaPlayer.create(context, R.raw.sallu_alan_nabi, audioAttributes, 0)
            if (mp != null) {
                mp.setVolume(1.0f, 1.0f)
                mp.setOnCompletionListener {
                    _isPlayingAllahuAkbar.value = false
                    releasePlayer()
                }
                mp.setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: $what, $extra")
                    _isPlayingAllahuAkbar.value = false
                    releasePlayer()
                    true
                }
                mediaPlayer = mp
                mp.start()
                _isPlayingAllahuAkbar.value = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to play fallback Allahu Akbar audio: ${e.message}", e)
            _isPlayingAllahuAkbar.value = false
        }
    }

    fun dismissAlert(context: Context? = null) {
        stop()
        _currentPrayerAlert.value = null
        if (context != null) {
            try {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                notificationManager?.cancel(1001)
            } catch (e: Exception) {
                Log.e(TAG, "Error cancelling prayer notification: ${e.message}")
            }
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping media player: ${e.message}")
        }

        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping TTS: ${e.message}")
        }

        _isPlayingAllahuAkbar.value = false
    }

    private fun releasePlayer() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing player: ${e.message}")
        }
    }

    private fun showPrayerNotification(context: Context, prayerNameUrdu: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Prayer Time Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when a prayer time begins with Allahu Akbar audio"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_STOP_AZAN
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1002,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("الله أكبر - وقتِ نماز ($prayerNameUrdu)")
            .setContentText("نماز کا وقت داخل ہو گیا ہے۔ حی علی الصلاۃ!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(stopPendingIntent)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "خاموش کریں (Stop / Mute)",
                stopPendingIntent
            )
            .build()

        notificationManager.notify(1001, notification)
    }

    fun schedulePrayerAlarms(context: Context, prayerTimes: List<PrayerTimeData>) {
        if (!isPrayerSoundEnabled(context)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? android.app.AlarmManager ?: return
        val now = System.currentTimeMillis()

        prayerTimes.filter { it.id != "sunrise" }.forEachIndexed { index, prayer ->
            val triggerTime = prayer.dateObj.time
            if (triggerTime > now) {
                val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                    putExtra("prayer_name_urdu", prayer.nameUrdu)
                    putExtra("prayer_name_english", prayer.nameEnglish)
                    putExtra("prayer_id", prayer.id)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    200 + index,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            android.app.AlarmManager.RTC_WAKEUP,
                            triggerTime,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            android.app.AlarmManager.RTC_WAKEUP,
                            triggerTime,
                            pendingIntent
                        )
                    }
                    Log.d(TAG, "Scheduled alarm for ${prayer.nameEnglish} at $triggerTime")
                } catch (e: SecurityException) {
                    Log.w(TAG, "Exact alarm permission not granted, fallback to set(): ${e.message}")
                    alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to schedule alarm: ${e.message}")
                }
            }
        }
    }
}
