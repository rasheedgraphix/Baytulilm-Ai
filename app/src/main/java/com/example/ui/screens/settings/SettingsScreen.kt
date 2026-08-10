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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel? = null
) {
    val context = LocalContext.current
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

