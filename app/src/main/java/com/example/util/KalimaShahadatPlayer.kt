package com.example.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

object KalimaShahadatPlayer {
    private const val TAG = "SalawatAudioPlayer"
    private const val PREFS_NAME = "baytul_ilm_audio_prefs"
    private const val KEY_PLAY_ON_LAUNCH = "play_salawat_on_launch"
    private const val UTTERANCE_ID = "SalawatMaleUtterance"

    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    private var hasPlayedThisSession = false

    fun isAutoPlayEnabled(context: Context): Boolean {
        val prefs = getPrefs(context)
        return prefs.getBoolean(KEY_PLAY_ON_LAUNCH, true)
    }

    fun setAutoPlayEnabled(context: Context, enabled: Boolean) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_PLAY_ON_LAUNCH, enabled).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Plays the beautiful recitation of Durood Sharif (صَلَّى اللّٰهُ عَلٰى مُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمْ).
     * Strictly plays once ONLY when opening the app (cold launch).
     */
    fun playOnAppLaunch(context: Context) {
        if (hasPlayedThisSession) {
            Log.d(TAG, "Durood Sharif has already played for this app open session.")
            return
        }
        if (!isAutoPlayEnabled(context)) {
            Log.d(TAG, "Auto play on launch is disabled by user.")
            return
        }
        hasPlayedThisSession = true
        playSalawat(context)
    }

    fun playKalima(context: Context) {
        playSalawat(context)
    }

    fun playSalawat(context: Context) {
        stop()
        _currentText.value = "صَلَّى اللّٰهُ عَلٰى مُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمْ"

        // Play the recorded Durood recitation directly via MediaPlayer
        playMediaAudio(context)
    }

    private fun playMediaAudio(context: Context) {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            val mp = MediaPlayer.create(context, R.raw.sallu_alan_nabi, audioAttributes, 0)
            if (mp != null) {
                mp.setVolume(1.0f, 1.0f)
                mp.setOnCompletionListener {
                    _isPlaying.value = false
                    releasePlayer()
                }
                mediaPlayer = mp
                mp.start()
                _isPlaying.value = true
            } else {
                playWithMaleVoice(context)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Media playback error: ${e.message}", e)
            playWithMaleVoice(context)
        }
    }

    private fun playWithMaleVoice(context: Context) {
        try {
            if (tts == null) {
                tts = TextToSpeech(context.applicationContext) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        isTtsReady = true
                        setupUtteranceListener()
                        setupTtsMaleVoice()
                        speakSalawatMale()
                    }
                }
            } else {
                setupUtteranceListener()
                if (isTtsReady) {
                    setupTtsMaleVoice()
                    speakSalawatMale()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Male voice TTS initialization error: ${e.message}", e)
            playFallbackMedia(context)
        }
    }

    private fun setupUtteranceListener() {
        try {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isPlaying.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
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

            // Search for available male voice in system TTS engine
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
                    Log.d(TAG, "Selected Male voice: ${maleArabicVoice.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring male voice: ${e.message}")
        }

        // 0.78f pitch creates a rich, deep masculine Qari voice tone
        t.setPitch(0.78f)
        t.setSpeechRate(0.82f)
    }

    private fun speakSalawatMale() {
        val text = "صَلُّوا عَلَى النَّبِيِّ ﷺ ، اَللّٰهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ عَلَىٰ سَيِّدِنَا مُحَمَّدٍ"
        _isPlaying.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
    }

    private fun playFallbackMedia(context: Context) {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            val mp = MediaPlayer.create(context, R.raw.sallu_alan_nabi, audioAttributes, 0)
            if (mp != null) {
                mp.setVolume(1.0f, 1.0f)
                mp.setOnCompletionListener {
                    _isPlaying.value = false
                    releasePlayer()
                }
                mediaPlayer = mp
                mp.start()
                _isPlaying.value = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fallback media playback error: ${e.message}")
            _isPlaying.value = false
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

        _isPlaying.value = false
    }

    private fun releasePlayer() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing player: ${e.message}")
        }
    }
}
