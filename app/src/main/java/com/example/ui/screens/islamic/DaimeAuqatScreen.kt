package com.example.ui.screens.islamic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.CityLocation
import com.example.util.DaimeDaySchedule
import com.example.util.LocationHelper
import com.example.util.PrayerTimeCalculator
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaimeAuqatScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentCalendar = remember { Calendar.getInstance() }
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH) + 1
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)

    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedCity by remember {
        mutableStateOf(
            PrayerTimeCalculator.defaultCities.find {
                it.nameEnglish.equals(LocationHelper.getCityName(context), ignoreCase = true)
            } ?: PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Rawalpindi" }
        )
    }

    var isHanafiAsr by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Today, 1: Full Month, 2: Makrooh Times
    var showCityDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isLocating by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            coroutineScope.launch {
                isLocating = true
                val loc = LocationHelper.getCurrentLocation(context)
                if (loc != null) {
                    selectedCity = loc
                    Toast.makeText(context, "مقام اپ ڈیٹ: ${loc.nameUrdu}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "GPS پوزیشن حاصل نہ ہو سکی", Toast.LENGTH_SHORT).show()
                }
                isLocating = false
            }
        } else {
            Toast.makeText(context, "لوکیشن کی اجازت درکار ہے", Toast.LENGTH_SHORT).show()
        }
    }

    // Urdu Month Names
    val monthNamesUrdu = listOf(
        "جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون",
        "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر"
    )

    // Calculate month schedule
    val monthSchedule = remember(selectedCity, selectedYear, selectedMonth) {
        PrayerTimeCalculator.calculateMonthSchedule(
            lat = selectedCity.lat,
            lng = selectedCity.lng,
            year = selectedYear,
            month = selectedMonth
        )
    }

    // Filter by search query
    val filteredSchedule = remember(monthSchedule, searchQuery) {
        if (searchQuery.isBlank()) monthSchedule
        else {
            val q = searchQuery.trim()
            monthSchedule.filter {
                it.dayOfMonth.toString() == q ||
                        it.dayOfWeekUrdu.contains(q, ignoreCase = true) ||
                        it.dateFormatted.contains(q)
            }
        }
    }

    // Today's schedule
    val todaySchedule = remember(selectedCity, currentYear, currentMonth, currentDay) {
        PrayerTimeCalculator.calculateDaySchedule(
            lat = selectedCity.lat,
            lng = selectedCity.lng,
            year = currentYear,
            month = currentMonth,
            day = currentDay
        )
    }

    // Hijri date string
    val hijriDateStr = remember {
        try {
            val h = HijriHelper.getHijriDetails(LocalDate.now(), "ur")
            "${h.day} ${h.monthName} ${h.year}ھ"
        } catch (e: Exception) {
            "۱۴۴۸ ھ"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "دائمی اوقاتِ نماز",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${selectedCity.nameUrdu} • ۳۶۵ دن کا مستند جدول",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Share Button
                    IconButton(onClick = {
                        val shareText = buildString {
                            appendLine("🕌 دائمی اوقاتِ نماز و سحر و افطار")
                            appendLine("📍 مقام: ${selectedCity.nameUrdu} (${selectedCity.nameEnglish})")
                            appendLine("📅 تاریخ: ${todaySchedule.dayOfWeekUrdu}، ${todaySchedule.dateFormatted} (${hijriDateStr})")
                            appendLine("─────────────────────────")
                            appendLine("• صبح صادق / سحر ختم: ${todaySchedule.sehriEnds}")
                            appendLine("• طلوعِ آفتاب: ${todaySchedule.sunrise}")
                            appendLine("• اشراق: ${todaySchedule.ishraq}")
                            appendLine("• زوال (نصف النہار): ${todaySchedule.zawal}")
                            appendLine("• ظہر: ${todaySchedule.dhuhr}")
                            appendLine("• عصر (حنفی): ${todaySchedule.asrHanafi}")
                            appendLine("• عصر (شافعی): ${todaySchedule.asrShafi}")
                            appendLine("• مغرب / افطار: ${todaySchedule.sunsetIftar}")
                            appendLine("• عشاء: ${todaySchedule.isha}")
                            appendLine("─────────────────────────")
                            appendLine("⚠️ اوقاتِ مکروہہ:")
                            appendLine("طلوع: ${todaySchedule.makroohSunrise}")
                            appendLine("زوال: ${todaySchedule.makroohZawal}")
                            appendLine("غروب: ${todaySchedule.makroohSunset}")
                            appendLine("درسِ نظامی لائبریری ایپ")
                        }
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "اوقاتِ نماز شیئر کریں"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            // 1. Top Header Card (Mobile-friendly, Islamic gradient, City + Today)
            item {
                HeaderCardMobile(
                    city = selectedCity,
                    hijriDate = hijriDateStr,
                    isLocating = isLocating,
                    onChangeCity = { showCityDialog = true },
                    onGpsClick = {
                        if (LocationHelper.hasLocationPermission(context)) {
                            coroutineScope.launch {
                                isLocating = true
                                val loc = LocationHelper.getCurrentLocation(context)
                                if (loc != null) {
                                    selectedCity = loc
                                    Toast.makeText(context, "مقام: ${loc.nameUrdu}", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "GPS پوزیشن حاصل نہ ہو سکی", Toast.LENGTH_SHORT).show()
                                }
                                isLocating = false
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
            }

            // 2. Navigation Tabs (آج کے اوقات | ماہانہ جنتری | مکروہ اوقات)
            item {
                TabSelectorMobile(
                    selectedTab = selectedTab,
                    onSelectTab = { selectedTab = it }
                )
            }

            // 3. Tab Contents
            when (selectedTab) {
                0 -> {
                    // TAB 0: TODAY'S TIMINGS (Full mobile friendly view)
                    item {
                        TodayDetailsView(
                            today = todaySchedule,
                            isHanafi = isHanafiAsr,
                            onToggleHanafi = { isHanafiAsr = it }
                        )
                    }
                }
                1 -> {
                    // TAB 1: MONTHLY JANTRI (Mobile optimized cards per day)
                    item {
                        MonthNavigatorMobile(
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear,
                            monthNames = monthNamesUrdu,
                            onSelectMonth = { selectedMonth = it },
                            onPrevYear = { selectedYear-- },
                            onNextYear = { selectedYear++ },
                            isHanafi = isHanafiAsr,
                            onToggleHanafi = { isHanafiAsr = it },
                            searchQuery = searchQuery,
                            onSearchChange = { searchQuery = it }
                        )
                    }

                    if (filteredSchedule.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "کوئی تاریخ نہیں ملی",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(filteredSchedule, key = { "${it.year}-${it.month}-${it.dayOfMonth}" }) { day ->
                            val isToday = (selectedMonth == currentMonth && selectedYear == currentYear && day.dayOfMonth == currentDay)
                            DayCardMobile(
                                day = day,
                                isToday = isToday,
                                isHanafi = isHanafiAsr
                            )
                        }
                    }
                }
                2 -> {
                    // TAB 2: MAKROOH TIMES & RULES (Easy explanation)
                    item {
                        MakroohGuideView(today = todaySchedule)
                    }
                }
            }

            // 4. Shari'i Footer Note
            item {
                FooterNoteCard()
            }
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        CityPickerMobileDialog(
            currentCity = selectedCity,
            onDismiss = { showCityDialog = false },
            onSelect = { city ->
                selectedCity = city
                showCityDialog = false
            }
        )
    }
}

// -------------------------------------------------------------
// COMPONENT 1: Mobile Header Card
// -------------------------------------------------------------

@Composable
private fun HeaderCardMobile(
    city: CityLocation,
    hijriDate: String,
    isLocating: Boolean,
    onChangeCity: () -> Unit,
    onGpsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0D5C33), Color(0xFF1B8A4C))
                    )
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top row: Location + GPS + Change City
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = city.nameUrdu,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${city.nameEnglish} • ${String.format(java.util.Locale.ENGLISH, "%.1f", city.lat)}°N",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // GPS button
                        IconButton(
                            onClick = onGpsClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            if (isLocating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Auto GPS",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Change City button
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFD54F),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onChangeCity() }
                        ) {
                            Text(
                                text = "شہر بدلیں",
                                color = Color(0xFF0D5C33),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))

                // Bottom row: Hijri date + Subtitle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اسلامی ہجری تاریخ:",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = hijriDate,
                        color = Color(0xFFFFD54F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// COMPONENT 2: Tab Selector
// -------------------------------------------------------------

@Composable
private fun TabSelectorMobile(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    val tabs = listOf(
        "آج کے اوقات" to Icons.Default.Today,
        "ماہانہ جنتری" to Icons.Default.CalendarMonth,
        "اوقاتِ مکروہہ" to Icons.Default.WarningAmber
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            tabs.forEachIndexed { index, (label, icon) ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .clickable { onSelectTab(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// COMPONENT 3: Today's Detailed View (TAB 0)
// -------------------------------------------------------------

@Composable
private fun TodayDetailsView(
    today: DaimeDaySchedule,
    isHanafi: Boolean,
    onToggleHanafi: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Asr Fiqh Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "آج کا مکمل نظام الاوقات:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            AssistChip(
                onClick = { onToggleHanafi(!isHanafi) },
                label = {
                    Text(
                        if (isHanafi) "عصر: حنفی (مثلین)" else "عصر: شافعی (مثل اول)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.SwapHoriz,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Big Primary Prayer Cards (2 Columns on Mobile, Perfectly Balanced)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrayerBoxItem(
                title = "سحر / صبح صادق",
                time = today.sehriEnds,
                subtitle = "سحر کا آخری وقت",
                icon = "🌙",
                color = Color(0xFF1E3A8A),
                modifier = Modifier.weight(1f)
            )
            PrayerBoxItem(
                title = "طلوعِ آفتاب",
                time = today.sunrise,
                subtitle = "فجر کا اختتام",
                icon = "🌅",
                color = Color(0xFFB45309),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrayerBoxItem(
                title = "نمازِ ظہر",
                time = today.dhuhr,
                subtitle = "زوال کے بعد",
                icon = "☀️",
                color = Color(0xFF047857),
                modifier = Modifier.weight(1f)
            )
            PrayerBoxItem(
                title = if (isHanafi) "عصر (حنفی)" else "عصر (شافعی)",
                time = if (isHanafi) today.asrHanafi else today.asrShafi,
                subtitle = if (isHanafi) "مثلین پر" else "مثلِ اول پر",
                icon = "🌤️",
                color = Color(0xFFC2410C),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrayerBoxItem(
                title = "مغرب / افطار",
                time = today.sunsetIftar,
                subtitle = "غروبِ آفتاب پر",
                icon = "🌇",
                color = Color(0xFF7C2D12),
                modifier = Modifier.weight(1f)
            )
            PrayerBoxItem(
                title = "نمازِ عشاء",
                time = today.isha,
                subtitle = "شفقِ احمر کے بعد",
                icon = "🌌",
                color = Color(0xFF312E81),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Nawafil Times Card (Ishraq, Chasht, Zawal)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "اوقاتِ نوافل و مستحب اوقات:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("نمازِ اشراق", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(today.ishraq, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("نمازِ چاشت", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(today.chasht, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("وقتِ زوال", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(today.zawal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerBoxItem(
    title: String,
    time: String,
    subtitle: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 18.sp)
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

// -------------------------------------------------------------
// COMPONENT 4: Month Navigator & Search (TAB 1)
// -------------------------------------------------------------

@Composable
private fun MonthNavigatorMobile(
    selectedMonth: Int,
    selectedYear: Int,
    monthNames: List<String>,
    onSelectMonth: (Int) -> Unit,
    onPrevYear: () -> Unit,
    onNextYear: () -> Unit,
    isHanafi: Boolean,
    onToggleHanafi: (Boolean) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Year + Asr switch row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Year switcher
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onPrevYear, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Prev Year")
                }
                Text(
                    text = "$selectedYear ء",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onNextYear, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Next Year")
                }
            }

            // Fiqh toggle
            AssistChip(
                onClick = { onToggleHanafi(!isHanafi) },
                label = {
                    Text(
                        if (isHanafi) "عصر: حنفی" else "عصر: شافعی",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        // Horizontal Month Selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(monthNames.size) { index ->
                val mNum = index + 1
                val isSelected = mNum == selectedMonth
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectMonth(mNum) }
                ) {
                    Text(
                        text = monthNames[index],
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Quick Day Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 6.dp),
            placeholder = { Text("تاریخ یا دن تلاش کریں (مثلاً 15 یا جمعہ)...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

// -------------------------------------------------------------
// COMPONENT 5: Clean Day Card for Mobile (No Horizontal Scrolling)
// -------------------------------------------------------------

@Composable
private fun DayCardMobile(
    day: DaimeDaySchedule,
    isToday: Boolean,
    isHanafi: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isToday) 1.5.dp else 0.5.dp,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isToday) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Card Header: Date + Day + Today Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isToday) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${day.dayOfMonth}",
                            color = if (isToday) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${day.dayOfWeekUrdu} • ${day.dateFormatted}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    if (isToday) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "آج",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "افطار: ${day.sunsetIftar}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timings: 3x2 Grid perfectly fitting any mobile width
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimingsCellMobile("سحر / فجر", day.sehriEnds, Modifier.weight(1f))
                TimingsCellMobile("طلوع", day.sunrise, Modifier.weight(1f))
                TimingsCellMobile("ظہر", day.dhuhr, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimingsCellMobile(if (isHanafi) "عصر (حنفی)" else "عصر (شافعی)", if (isHanafi) day.asrHanafi else day.asrShafi, Modifier.weight(1f))
                TimingsCellMobile("غروب / افطار", day.sunsetIftar, Modifier.weight(1f), isHighlight = true)
                TimingsCellMobile("عشاء", day.isha, Modifier.weight(1f))
            }

            // Expanded extra details (Ishraq, Chasht, Zawal, Makrooh)
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• اشراق: ${day.ishraq}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("• زوال: ${day.zawal}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("• چاشت: ${day.chasht}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "مکروہ اوقات: طلوع (${day.makroohSunrise}) • زوال (${day.makroohZawal}) • غروب (${day.makroohSunset})",
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimingsCellMobile(
    label: String,
    time: String,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = time,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// -------------------------------------------------------------
// COMPONENT 6: Makrooh Times Guide (TAB 2)
// -------------------------------------------------------------

@Composable
private fun MakroohGuideView(today: DaimeDaySchedule) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Warning Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "اوقاتِ مکروہہ کی شرعی تفصیل",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ان تین اوقات میں کوئی بھی فرض، واجب، نفل یا قضاء نماز اور سجدہ تلاوت کرنا مکروہ تحریمی و ناجائز ہے:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Three Specific Times
        MakroohTimeItemCard(
            number = "۱",
            title = "طلوعِ آفتاب کا وقت",
            todayRange = today.makroohSunrise,
            desc = "سورج نکلنے کے وقت سے لے کر جب تک سورج ایک نیزہ بلند نہ ہو جائے (تقریباً ۱۵ منٹ بعد تک) نماز پڑھنا مکروہ ہے۔ اس کے بعد اشراق کا وقت شروع ہوتا ہے۔"
        )

        Spacer(modifier = Modifier.height(8.dp))

        MakroohTimeItemCard(
            number = "۲",
            title = "نصف النہار / استواءِ شمس (وقتِ زوال)",
            todayRange = today.makroohZawal,
            desc = "سورج عین آسمان کے درمیان ہونے کے وقت سے لے کر ڈھلنے تک (تقریباً ۱۰ منٹ)۔ جب سورج ڈھل جائے تو ظہر کا وقت شروع ہو جاتا ہے اور کراہت ختم ہو جاتی ہے۔"
        )

        Spacer(modifier = Modifier.height(8.dp))

        MakroohTimeItemCard(
            number = "۳",
            title = "غروبِ آفتاب کا وقت",
            todayRange = today.makroohSunset,
            desc = "سورج زرد ہونے کے وقت سے لے کر مکمل غروب ہونے تک (تقریباً ۱۵ منٹ)۔ البتہ اگر اسی دن کی نمازِ عصر قضا ہو چکی ہو تو غروب سے پہلے ادا کر لی جائے۔"
        )
    }
}

@Composable
private fun MakroohTimeItemCard(
    number: String,
    title: String,
    todayRange: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "آج: $todayRange",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = desc,
                fontSize = 11.5.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------------------------------------------------
// COMPONENT 7: Footer Information
// -------------------------------------------------------------

@Composable
private fun FooterNoteCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "قواعد و شرعی رہنمائی:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• یہ نقشہ جامعۃ العلوم الاسلامیہ علامہ بنوری ٹاؤن کراچی کے مستند فلکیاتی کلیہ کے مطابق ہے۔\n" +
                        "• سحر کے اختتام پر احتیاطاً ۲ منٹ قبل کھانا پینا روک دیں، اور افطار غروب کا یقین ہونے پر کریں۔",
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------------------------------------------------
// COMPONENT 8: City Picker Dialog
// -------------------------------------------------------------

@Composable
private fun CityPickerMobileDialog(
    currentCity: CityLocation,
    onDismiss: () -> Unit,
    onSelect: (CityLocation) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val cities = remember { PrayerTimeCalculator.defaultCities }

    val filtered = remember(query) {
        if (query.isBlank()) cities
        else {
            val q = query.trim()
            cities.filter {
                it.nameUrdu.contains(q, ignoreCase = true) ||
                        it.nameEnglish.contains(q, ignoreCase = true)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("شہر کا انتخاب کریں", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("شہر تلاش کریں (مثلاً راولپنڈی، لاہور)...", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(filtered, key = { "${it.nameEnglish}-${it.lat}" }) { city ->
                    val isSelected = city.nameEnglish.equals(currentCity.nameEnglish, ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(city) }
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else Color.Transparent
                            )
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = city.nameUrdu,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${city.nameEnglish} (${String.format(java.util.Locale.ENGLISH, "%.1f", city.lat)}°, ${String.format(java.util.Locale.ENGLISH, "%.1f", city.lng)}°)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("بند کریں")
            }
        }
    )
}
