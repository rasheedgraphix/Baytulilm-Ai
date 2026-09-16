package com.example.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.update.AppUpdateInfo
import com.example.data.update.AppUpdateManager
import com.example.ui.components.AppUpdateDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.BookEntity
import com.example.data.repository.VerifiedAyah
import com.example.data.repository.VerifiedHadith
import com.example.ui.components.BookCoverThumbnailView
import com.example.ui.components.GenericBookCover
import com.example.ui.components.DarjaClassIconBadge
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CityLocation
import com.example.util.DarsNizamiMatcher
import com.example.util.LocalAppLanguage
import com.example.util.PrayerTimeCalculator
import com.example.util.PrayerTimeData
import com.example.util.lStr

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val recentReadings by viewModel.recentReadings.collectAsStateWithLifecycle()
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val prayerTimes by viewModel.prayerTimes.collectAsStateWithLifecycle()
    val dailyHadith = viewModel.dailyHadith
    val dailyAyah = viewModel.dailyAyah
    val currentLang = LocalAppLanguage.current
    val currentCityLabel = when (currentLang.code) {
        "en" -> selectedCity.nameEnglish
        "ps" -> selectedCity.nameUrdu
        else -> {
            val urdu = selectedCity.nameUrdu.trim()
            val eng = selectedCity.nameEnglish.trim()
            if (urdu.equals(eng, ignoreCase = true) || urdu.isBlank()) {
                eng.ifBlank { urdu }
            } else if (eng.isBlank()) {
                urdu
            } else {
                "$urdu ($eng)"
            }
        }
    }

    var showCityPicker by remember { mutableStateOf(false) }
    var updateInfoState by remember { mutableStateOf<AppUpdateInfo?>(null) }
    val locationPrefs = remember { context.getSharedPreferences("location_prompt_prefs", Context.MODE_PRIVATE) }
    var showLocationPrompt by remember { mutableStateOf(false) }
    var isDetectingLocation by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isDetectingLocation = true
            viewModel.detectAndSetCurrentLocation(context) { success, cityName ->
                isDetectingLocation = false
                if (success) {
                    android.widget.Toast.makeText(
                        context,
                        if (currentLang.code == "en") "Location set: $cityName" else "مقام سیٹ ہو گیا: $cityName",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = com.example.util.LocationHelper.hasLocationPermission(context)
        val promptShown = locationPrefs.getBoolean("prompt_shown", false)
        if (!hasPermission && !promptShown) {
            showLocationPrompt = true
        } else if (hasPermission) {
            viewModel.detectAndSetCurrentLocation(context)
        }

        try {
            val info = AppUpdateManager.checkForAppUpdate(context)
            if (info.isUpdateAvailable) {
                updateInfoState = info
            }
        } catch (e: Exception) {
            // Ignored safely
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Top Greeting & Search Header - Prestigious Scholarly Academic Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Greeting & Hijri Date Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = lStr("assalamu_alaikum"),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lStr("app_name"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Refined Hijri Date Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🌙",
                                fontSize = 12.sp
                            )
                            Text(
                                text = com.example.ui.screens.islamic.HijriHelper.getTodayHijriDate(currentLang.code),
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Clean Modern Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.setSearchQuery(it)
                        if (it.isNotBlank()) {
                            onNavigate(Screen.Search.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_bar"),
                    placeholder = {
                        Text(
                            text = lStr("search_placeholder"),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // LOCATION-BASED PRAYER TIMES SECTION (AT THE TOP)
        SectionHeader(title = lStr("prayer_times"))
        LocationPrayerTimesCard(
            locationLabel = currentCityLabel,
            prayerTimes = prayerTimes,
            isDetectingLocation = isDetectingLocation,
            onOpenCityPicker = { showCityPicker = true },
            onAutoDetectLocation = {
                if (com.example.util.LocationHelper.hasLocationPermission(context)) {
                    isDetectingLocation = true
                    viewModel.detectAndSetCurrentLocation(context) { success, cityName ->
                        isDetectingLocation = false
                        if (success) {
                            android.widget.Toast.makeText(
                                context,
                                if (currentLang.code == "en") "Location updated: $cityName" else "مقام اپ ڈیٹ ہو گیا: $cityName",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ISLAMIC SERVICES & TOOLS - 2-COLUMN ISLAMIC CARDS (MATCHING USER REFERENCE DESIGN)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = lStr("islamic_information"),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "🌙",
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: Asma-ul-Husna & Asma-un-Nabi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = lStr("names_of_allah"),
                    iconEmoji = "🌙",
                    onClick = { onNavigate(Screen.AsmaUlHusna.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("asma_un_nabi"),
                    calligraphyText = "ﷺ",
                    onClick = { onNavigate(Screen.AsmaUnNabi.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: Haramain Live & Islamic Calendar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = lStr("haramain_live"),
                    iconEmoji = "🕋",
                    onClick = { onNavigate(Screen.HaramainLive.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("islamic_calendar"),
                    iconEmoji = "📅",
                    onClick = { onNavigate(Screen.IslamicCalendar.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3: Quran Pak & Tafaseer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = lStr("quran_pak"),
                    iconEmoji = "📖",
                    onClick = { onNavigate(Screen.QuranPak.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("tafaseer"),
                    iconEmoji = "📚",
                    onClick = { onNavigate(Screen.Tafaseer.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 4: Lughat (Dictionaries) & Fatawa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = if (currentLang.code == "en") "Lughat & Dictionaries" else if (currentLang.code == "ps") "لغت او قاموس" else "لغات و ڈکشنری",
                    iconEmoji = "🔤",
                    testTag = "lughat_shortcut_tile",
                    onClick = { onNavigate(Screen.Lughat.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("fatawa"),
                    iconEmoji = "⚖️",
                    onClick = { onNavigate(Screen.Fatawa.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 5: Qibla Compass & Digital Tasbeeh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = lStr("qibla"),
                    iconEmoji = "🧭",
                    onClick = { onNavigate(Screen.QiblaCompass.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("tasbeeh"),
                    iconEmoji = "📿",
                    onClick = { onNavigate(Screen.Tasbeeh.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 6: 100 Famous Duas & Islamic Quiz
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = if (currentLang.code == "en") "100 Famous Duas" else "۱۰۰ مشہور دعائیں",
                    iconEmoji = "🤲",
                    onClick = { onNavigate(Screen.FamousDuas.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = lStr("quiz"),
                    iconEmoji = "💡",
                    onClick = { onNavigate(Screen.Quiz.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 7: All Darjat & Islamic Library
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IslamicServiceCard(
                    title = if (currentLang.code == "en") "All Darjat" else "تمام درجات",
                    iconEmoji = "🎓",
                    onClick = { onNavigate(Screen.Darjat.route) },
                    modifier = Modifier.weight(1f)
                )
                IslamicServiceCard(
                    title = if (currentLang.code == "en") "Islamic Library" else "کتب خانہ و لائبریری",
                    iconEmoji = "🏛️",
                    onClick = { onNavigate(Screen.Library.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. DAILY AUTHENTIC HADITH SECTION
        SectionHeader(title = lStr("daily_hadith"))
        DailyHadithSection(hadith = dailyHadith)

        Spacer(modifier = Modifier.height(20.dp))

        // 3. DAILY QURAN AYAH SECTION
        SectionHeader(
            title = lStr("daily_quran"),
            actionText = "تمام نسخے",
            onActionClick = { onNavigate(Screen.QuranPak.route) }
        )
        DailyAyahSection(
            ayah = dailyAyah,
            onOpenQuran = { onNavigate(Screen.QuranPak.route) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Continue Reading Card
        if (recentReadings.isNotEmpty()) {
            val lastBook = recentReadings.first()
            SectionHeader(title = lStr("recent_reading")) {
                onNavigate(Screen.Recent.route)
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigate(Screen.BookViewer.createRoute(lastBook.bookId)) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GenericBookCover(
                        title = lastBook.bookTitle,
                        tag = "جاری مطالعہ",
                        badgeText = "صفحہ ${lastBook.pageNumber}",
                        author = lastBook.author,
                        themeColor = Color(0xFF047857),
                        width = 48.dp,
                        height = 68.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lastBook.bookTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${lStr("question_progress")} ${lastBook.pageNumber} / ${lastBook.totalPages} • ${lastBook.author}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = { onNavigate(Screen.BookViewer.createRoute(lastBook.bookId)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(lStr("read_now"), fontSize = 12.sp)
                    }
                }
            }
        }
    }

    if (showCityPicker) {
        CityPickerDialog(
            onCitySelected = { city ->
                viewModel.selectCity(city)
            },
            onAutoDetectLocation = {
                if (com.example.util.LocationHelper.hasLocationPermission(context)) {
                    isDetectingLocation = true
                    viewModel.detectAndSetCurrentLocation(context) { _, _ ->
                        isDetectingLocation = false
                    }
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            onDismiss = { showCityPicker = false }
        )
    }

    if (showLocationPrompt) {
        AlertDialog(
            onDismissRequest = {
                locationPrefs.edit().putBoolean("prompt_shown", true).apply()
                showLocationPrompt = false
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            title = {
                Text(
                    text = if (currentLang.code == "en") "Auto-Detect Prayer Times" else "مقام اور اوقاتِ نماز",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = if (currentLang.code == "en") {
                        "Allow location access to automatically calculate exact prayer times, Qibla direction, and sunrise for your current city."
                    } else if (currentLang.code == "ps") {
                        "د لمانځه د کره وختونو، قبلې او سپوږمۍ لیدلو لپاره د خپل ګرځنده موقعیت (Location) ته اجازه ورکړئ."
                    } else {
                        "درست ترین اوقاتِ نماز، اذان اور قبلہ رخ کے خودکار تعین کے لیے موبائل کے مقام (Location) کی اجازت دیں۔"
                    },
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        locationPrefs.edit().putBoolean("prompt_shown", true).apply()
                        showLocationPrompt = false
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (currentLang.code == "en") "Allow & Set Times" else "اجازت دیں اور اوقات سیٹ کریں",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        locationPrefs.edit().putBoolean("prompt_shown", true).apply()
                        showLocationPrompt = false
                    }
                ) {
                    Text(
                        text = if (currentLang.code == "en") "Choose Manually" else "دستی منتخب کریں"
                    )
                }
            }
        )
    }

    updateInfoState?.let { updateInfo ->
        AppUpdateDialog(
            updateInfo = updateInfo,
            onDismiss = { updateInfoState = null }
        )
    }
}

@Composable
private fun LocationPrayerTimesCard(
    locationLabel: String,
    prayerTimes: List<PrayerTimeData>,
    isDetectingLocation: Boolean = false,
    onOpenCityPicker: () -> Unit,
    onAutoDetectLocation: () -> Unit = {}
) {
    val langCode = LocalAppLanguage.current.code
    val context = androidx.compose.ui.platform.LocalContext.current
    val sunriseData = prayerTimes.firstOrNull { it.id == "sunrise" }
    // 5 Farz prayers to display simultaneously without scrolling
    val fivePrayers = prayerTimes.filter { it.id != "sunrise" }
    val nextPrayer = fivePrayers.firstOrNull { it.isNext }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // 1. TOP HEADER: City Name + Location Actions (Clean, spacious, uncrowded)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // City info with icon (clickable to change city)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onOpenCityPicker() }
                        .padding(vertical = 2.dp, horizontal = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "Selected City",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = lStr("selected_city"),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = locationLabel,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Action buttons: GPS Detect + Change City
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Quick GPS auto-detect button
                    IconButton(
                        onClick = onAutoDetectLocation,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    ) {
                        if (isDetectingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Auto Detect Location",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Change city button
                    FilledTonalButton(
                        onClick = onOpenCityPicker,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search City",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = lStr("change_city"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. SUB-BAR: Sunrise time & Next Prayer Status Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (sunriseData != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌅", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${if (langCode == "en") "Sunrise" else if (langCode == "ps") "لمر خاته" else "طلوعِ آفتاب"}: ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "\u200E${sunriseData.timeFormatted}\u200E",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (nextPrayer != null) {
                    val nextPrayerLocalizedName = when (langCode) {
                        "en" -> nextPrayer.nameEnglish
                        "ps" -> when (nextPrayer.id) {
                            "fajr" -> "سحر"
                            "dhuhr" -> "ماسپښین"
                            "asr" -> "مازیګر"
                            "maghrib" -> "ماښام"
                            "isha" -> "ماسخوتن"
                            else -> nextPrayer.nameUrdu
                        }
                        else -> nextPrayer.nameUrdu
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${if (langCode == "en") "Next" else if (langCode == "ps") "راتلونکې" else "اگلی نماز"}: $nextPrayerLocalizedName",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 5 DAILY PRAYERS: Uniform height, clean typography, LTR-safe time format
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                fivePrayers.forEach { prayer ->
                    val isNext = prayer.isNext
                    val prayerLocalizedName = when (langCode) {
                        "en" -> prayer.nameEnglish
                        "ps" -> when (prayer.id) {
                            "fajr" -> "سحر"
                            "dhuhr" -> "ماسپښین"
                            "asr" -> "مازیګر"
                            "maghrib" -> "ماښام"
                            "isha" -> "ماسخوتن"
                            else -> prayer.nameUrdu
                        }
                        else -> prayer.nameUrdu
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isNext) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f)
                            }
                        ),
                        border = if (isNext) {
                            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = prayerLocalizedName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = prayer.nameArabic,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Format with LTR mark to prevent BiDi inversion in Urdu (e.g. "08:27 AM" not "AM 08:27")
                            Text(
                                text = "\u200E${prayer.timeFormatted}\u200E",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Fixed-height container (18.dp) so all 5 cards have strictly identical height
                            if (isNext) {
                                Box(
                                    modifier = Modifier
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (langCode == "en") "Next" else if (langCode == "ps") "راتلونکې" else "اگلی",
                                        fontSize = 8.5.sp,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Method Tag: University of Islamic Sciences, Karachi (Banuri Town)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕌 ",
                    fontSize = 11.sp
                )
                Text(
                    text = when (langCode) {
                        "en" -> "Univ. of Islamic Sciences, Karachi (Hanafi, 18°)"
                        "ps" -> "جامعۃ العلوم الاسلامیہ بنوري ټاؤن کراچۍ (حنفي، ۱۸ درجې)"
                        else -> "جامعۃ العلوم الاسلامیہ علامہ بنوری ٹاؤن کراچی (حنفی، ۱۸°)"
                    },
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun DailyHadithSection(hadith: VerifiedHadith) {
    val langCode = LocalAppLanguage.current.code
    val hadithTranslation = when (langCode) {
        "en" -> hadith.textEnglish.ifBlank { hadith.textUrdu }
        "ps" -> hadith.textPashto.ifBlank { hadith.textUrdu }
        else -> hadith.textUrdu
    }
    val verificationTag = if (langCode == "en") hadith.verificationTagEnglish else hadith.verificationTag
    val bookName = if (langCode == "en" && hadith.bookNameEnglish.isNotBlank()) hadith.bookNameEnglish else hadith.bookName
    val hadithNum = if (langCode == "en" && hadith.hadithNumberEnglish.isNotBlank()) hadith.hadithNumberEnglish else hadith.hadithNumber

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Daily Hadith",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = lStr("daily_hadith_badge"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEDE9FE))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = verificationTag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D28D9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = hadith.textArabic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                lineHeight = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "\"$hadithTranslation\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$bookName • $hadithNum",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = lStr("verified_source"),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun DailyAyahSection(
    ayah: VerifiedAyah,
    onOpenQuran: () -> Unit = {}
) {
    val langCode = LocalAppLanguage.current.code
    val ayahTranslation = when (langCode) {
        "en" -> ayah.textEnglish.ifBlank { ayah.textUrdu }
        "ps" -> ayah.textPashto.ifBlank { ayah.textUrdu }
        else -> ayah.textUrdu
    }
    val surahBadge = if (langCode == "en" && ayah.surahNameEnglish.isNotBlank()) {
        "${ayah.surahNameEnglish} [Verse ${ayah.ayahNumber}]"
    } else {
        "${ayah.surahName} [آیت ${ayah.ayahNumber}]"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onOpenQuran() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Daily Quran Ayah",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = lStr("daily_ayah_badge"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = surahBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ayah.textArabic,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                lineHeight = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "\"$ayahTranslation\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lStr("verified_quran_text"),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenQuran() }
                ) {
                    Text(
                        text = "قرآن مجید کھولیں",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CityPickerDialog(
    onCitySelected: (CityLocation) -> Unit,
    onAutoDetectLocation: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCities = PrayerTimeCalculator.defaultCities.filter {
        it.nameUrdu.contains(searchQuery, ignoreCase = true) ||
        it.nameEnglish.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = lStr("select_city_dialog_title"),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
            ) {
                // Auto Detect Button inside dialog
                FilledTonalButton(
                    onClick = {
                        onAutoDetectLocation()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "GPS Auto Detect",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (LocalAppLanguage.current.code == "en") "Auto-Detect My Location (GPS)" else "📍 موجودہ مقام خودکار حاصل کریں (GPS)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(lStr("search_city_placeholder"), fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredCities) { city ->
                        val langCode = LocalAppLanguage.current.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCitySelected(city)
                                    onDismiss()
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (langCode == "en") city.nameEnglish else city.nameUrdu,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (langCode == "en") city.nameUrdu else city.nameEnglish,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(lStr("cancel"), fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
private fun IslamicServiceCard(
    title: String,
    iconEmoji: String? = null,
    iconVector: ImageVector? = null,
    iconTint: Color = Color.Unspecified,
    calligraphyText: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                calligraphyText != null -> {
                    Text(
                        text = calligraphyText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
                iconEmoji != null -> {
                    Text(
                        text = iconEmoji,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                }
                iconVector != null -> {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = title,
                        tint = if (iconTint != Color.Unspecified) iconTint else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class HomeDarjaInfo(
    val id: String,
    val name: String,
    val year: String,
    val subjects: String,
    val accentColor: Color
)

@Composable
private fun HomeDarjaCard(
    darja: HomeDarjaInfo,
    bookCount: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(160.dp)
            .height(154.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.2.dp, darja.accentColor.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DarjaClassIconBadge(
                    classId = darja.id,
                    size = 42.dp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = darja.accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = darja.year,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = darja.accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column {
                Text(
                    text = darja.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = darja.subjects,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$bookCount کتب",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = darja.accentColor
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = darja.accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeBookCard(
    book: BookEntity,
    onRead: () -> Unit
) {
    Card(
        onClick = onRead,
        modifier = Modifier
            .width(140.dp)
            .height(205.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                BookCoverThumbnailView(
                    book = book,
                    thumbnail = null,
                    width = 82.dp,
                    height = 115.dp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = book.title,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                    Text(
                        text = book.author,
                        fontSize = 9.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "مطالعہ کریں",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

private data class SubjectChipInfo(
    val title: String,
    val color: Color
)

@Composable
private fun IslamicFeatureItem(
    title: String,
    icon: String,
    onClick: () -> Unit,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
