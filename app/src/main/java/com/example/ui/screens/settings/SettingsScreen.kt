package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.components.ThemePickerDialog
import com.example.ui.viewmodel.AuthViewModel
import com.example.util.LanguageManager
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel? = null,
    onNavigate: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
    var pushNotifications by remember { mutableStateOf(true) }
    var offlineAutoSync by remember { mutableStateOf(true) }

    var showLanguagePicker by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    val currentLang by LanguageManager.currentLanguage.collectAsState()
    val currentTheme by LanguageManager.currentTheme.collectAsState()
    val userProfile = authViewModel?.userState?.collectAsState()?.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = LanguageManager.getString("settings", currentLang.code),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Configure reader preferences, language, theme and offline storage",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language & Theme Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${LanguageManager.getString("app_language", currentLang.code)} & ${LanguageManager.getString("theme_settings", currentLang.code)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // App Language Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLanguagePicker = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LanguageManager.getString("app_language", currentLang.code),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${currentLang.flagEmoji} ${currentLang.nativeName} (${currentLang.englishName})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Theme Settings Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showThemePicker = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ColorLens,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LanguageManager.getString("theme_settings", currentLang.code),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = when (currentTheme) {
                                    "Light" -> LanguageManager.getString("light_theme", currentLang.code)
                                    "Dark" -> LanguageManager.getString("dark_theme", currentLang.code)
                                    else -> LanguageManager.getString("system_theme", currentLang.code)
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Notifications & Sync",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Push Notifications (FCM)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Daily Hadith, Prayer alerts & announcements", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = pushNotifications, onCheckedChange = { pushNotifications = it })
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Offline Sync", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Sync bookmarks and reading progress with Room database", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = offlineAutoSync, onCheckedChange = { offlineAutoSync = it })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Islamic Recitation & Startup Audio Settings Card
        var playKalimaOnLaunch by remember { mutableStateOf(com.example.util.KalimaShahadatPlayer.isAutoPlayEnabled(context)) }
        val isKalimaPlaying by com.example.util.KalimaShahadatPlayer.isPlaying.collectAsState()

        var playPrayerAudio by remember { mutableStateOf(com.example.util.PrayerAudioNotifier.isPrayerSoundEnabled(context)) }
        val isPrayerPlaying by com.example.util.PrayerAudioNotifier.isPlayingAllahuAkbar.collectAsState()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Audio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "اسلامی تلاوت و اذکار (Islamic Audio)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ایپ کھلتے وقت درود پاک (صلوا علی النبی)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "صَلُّوا عَلَى النَّبِيِّ ﷺ - اَللّٰهُمَّ صَلِّ وَسَلِّمْ عَلَىٰ سَيِّدِنَا مُحَمَّدٍ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = playKalimaOnLaunch,
                        onCheckedChange = {
                            playKalimaOnLaunch = it
                            com.example.util.KalimaShahadatPlayer.setAutoPlayEnabled(context, it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (isKalimaPlaying) {
                                com.example.util.KalimaShahadatPlayer.stop()
                            } else {
                                com.example.util.KalimaShahadatPlayer.playKalima(context)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isKalimaPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isKalimaPlaying) "بند کریں (Stop)" else "درود پاک سنیں (Listen Now)", fontSize = 12.sp)
                    }
                }

                androidx.compose.material3.HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                // Prayer Time Allahu Akbar Option
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "نماز کا وقت داخل ہونے پر اللہ اکبر کی آواز",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "اللهُ أَكْبَرُ، اللهُ أَكْبَرُ - ہر نماز کے شروع ہوتے ہی تکبیر کی آواز",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = playPrayerAudio,
                        onCheckedChange = {
                            playPrayerAudio = it
                            com.example.util.PrayerAudioNotifier.setPrayerSoundEnabled(context, it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (isPrayerPlaying) {
                                com.example.util.PrayerAudioNotifier.stop()
                            } else {
                                com.example.util.PrayerAudioNotifier.playAllahuAkbar(context, "ٹیسٹ (Test)")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPrayerPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isPrayerPlaying) "بند کریں (Stop)" else "اللہ اکبر کی آواز سنیں (Test)", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cache & Local Storage",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Database: Room Database (SQLite)\nCache Engine: Local Storage",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Clear cache logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear Temporary Cache", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Version & Update Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ایپ ورژن اور اپ ڈیٹس (App Updates)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                val packageInfo = remember {
                    try {
                        context.packageManager.getPackageInfo(context.packageName, 0)
                    } catch (e: Exception) {
                        null
                    }
                }
                val currentVerName = packageInfo?.versionName ?: "1.3.8"
                val currentVerCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    packageInfo?.longVersionCode?.toInt() ?: 10
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo?.versionCode ?: 10
                }

                Text(
                    text = "Current Installed Version: $currentVerName (Build $currentVerCode)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                var isCheckingUpdate by remember { mutableStateOf(false) }
                var updateResultState by remember { mutableStateOf<com.example.data.update.AppUpdateInfo?>(null) }
                var showNoUpdateToast by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://rasheedgraphix.github.io/baytul-ilm-website/"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Update, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Check for Update")
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        isCheckingUpdate = true
                        showNoUpdateToast = false
                        coroutineScope.launch {
                            try {
                                val info = com.example.data.update.AppUpdateManager.checkForAppUpdate(context)
                                isCheckingUpdate = false
                                if (info.isUpdateAvailable) {
                                    updateResultState = info
                                } else {
                                    showNoUpdateToast = true
                                }
                            } catch (e: Exception) {
                                isCheckingUpdate = false
                                showNoUpdateToast = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isCheckingUpdate) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("چیک کیا جا رہا ہے...")
                    } else {
                        Icon(Icons.Default.SystemUpdate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("اپ ڈیٹس چیک کریں (Check for In-App Updates)")
                    }
                }

                if (showNoUpdateToast) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ آپ کا ورژن $currentVerName بالکل نیا اور اپ ٹو ڈیٹ ہے!",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                updateResultState?.let { updateInfo ->
                    com.example.ui.components.AppUpdateDialog(
                        updateInfo = updateInfo,
                        onDismiss = { updateResultState = null }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Policy Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Privacy Policy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "رازداری کی پالیسی (Privacy Policy)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "ہماری رازداری کی مکمل شرائط، ڈیٹا کا تحفظ اور صارف کے حقوق ملاحظہ فرمائیں۔",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onNavigate(com.example.ui.navigation.Screen.PrivacyPolicy.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("پالیسی پڑھیں (Read Privacy Policy)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showLanguagePicker) {
        LanguagePickerDialog(
            onDismissRequest = { showLanguagePicker = false },
            onLanguageSelected = { selectedLang ->
                if (userProfile != null && authViewModel != null) {
                    authViewModel.updateProfile(
                        name = userProfile.name,
                        phone = userProfile.phone,
                        country = userProfile.country,
                        province = userProfile.province,
                        city = userProfile.city,
                        language = selectedLang.englishName,
                        theme = userProfile.theme
                    )
                }
            }
        )
    }

    if (showThemePicker) {
        ThemePickerDialog(
            onDismissRequest = { showThemePicker = false },
            onThemeSelected = { selectedTheme ->
                if (userProfile != null && authViewModel != null) {
                    authViewModel.updateProfile(
                        name = userProfile.name,
                        phone = userProfile.phone,
                        country = userProfile.country,
                        province = userProfile.province,
                        city = userProfile.city,
                        language = userProfile.language,
                        theme = selectedTheme
                    )
                }
            }
        )
    }
}

