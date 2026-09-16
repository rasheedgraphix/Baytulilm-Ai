package com.example.ui.screens.islamic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.repository.IslamicData
import com.example.util.LanguageManager
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicCalendarScreen(
    onNavigateBack: () -> Unit,
    viewModel: IslamicCalendarViewModel = viewModel()
) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentLang by LanguageManager.currentLanguage.collectAsState()
    val events = IslamicData.ISLAMIC_EVENTS

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val daysInMonth = (1..currentMonth.lengthOfMonth()).map { day ->
        currentMonth.atDay(day)
    }

    val firstDayOfMonth = currentMonth.atDay(1)
    // dayOfWeek: 1 = Monday, 2 = Tuesday, ..., 7 = Sunday
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value
    val emptyCellsCount = dayOfWeek - 1
    val emptyCells = List(emptyCellsCount) { null }
    val allCells = emptyCells + daysInMonth

    val hijriMonthSpan = remember(currentMonth, currentLang.code) {
        HijriHelper.getHijriMonthSpan(currentMonth, currentLang.code)
    }

    val todayHijri = remember(currentLang.code) {
        HijriHelper.getTodayHijriDate(currentLang.code)
    }

    val selectedHijriDetails = remember(selectedDate, currentLang.code) {
        HijriHelper.getHijriDetails(selectedDate, currentLang.code)
    }

    // Weekdays localized: Mon to Sun in standard sequential order
    val weekdays = when (currentLang.code) {
        "ps" -> listOf("دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه", "یکشنبه")
        "en" -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        else -> listOf("پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ", "اتوار")
    }

    val titleText = when (currentLang.code) {
        "ps" -> "🗓️ اسلامي کلیز"
        "en" -> "🗓️ Islamic Calendar"
        else -> "🗓️ اسلامی کیلنڈر"
    }

    val todayBtnText = when (currentLang.code) {
        "ps" -> "نن ورځ"
        "en" -> "Today"
        else -> "آج کی تاریخ"
    }

    val monthEvents = remember(currentMonth) {
        events.filter { event ->
            try {
                val eventDate = LocalDate.parse(event.gregorianDate)
                eventDate.year == currentMonth.year && eventDate.monthValue == currentMonth.monthValue
            } catch (e: Exception) {
                false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = {
                            viewModel.today()
                            selectedDate = LocalDate.now()
                        },
                        modifier = Modifier.padding(end = 8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(todayBtnText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Month & Hijri Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.previousMonth() }) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month", modifier = Modifier.size(28.dp))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Gregorian Month & Year
                                val gregorianMonthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                Text(
                                    text = "$gregorianMonthName ${currentMonth.year}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Clearly display the current Hijri month(s)
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                                ) {
                                    Text(
                                        text = "🌙 $hijriMonthSpan",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.nextMonth() }) {
                                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month", modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }

            // Calendar Section (Enforced LTR so numbers flow sequentially left-to-right from Mon to Sun)
            item {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 8.dp)
                    ) {
                        // Weekdays Header (Mon to Sun)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            weekdays.forEachIndexed { index, dayName ->
                                val isFriday = index == 4 // Juma
                                val isSunday = index == 6
                                Text(
                                    text = dayName,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (dayName.length > 5) 10.sp else 12.sp,
                                    color = if (isFriday) Color(0xFF10B981) else if (isSunday) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Divider(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )

                        // 7-Column Calendar Days Grid
                        val totalRows = (allCells.size + 6) / 7
                        Column(modifier = Modifier.fillMaxWidth()) {
                            for (rowIndex in 0 until totalRows) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (colIndex in 0 until 7) {
                                        val cellIndex = rowIndex * 7 + colIndex
                                        val date = allCells.getOrNull(cellIndex)

                                        if (date != null) {
                                            val isToday = date == LocalDate.now()
                                            val isSelected = date == selectedDate
                                            val hijriInfo = remember(date, currentLang.code) {
                                                HijriHelper.getHijriDetails(date, currentLang.code)
                                            }
                                            val eventForDay = events.find { it.gregorianDate == date.toString() }

                                            val bgColor = when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                isToday -> MaterialTheme.colorScheme.primaryContainer
                                                eventForDay != null -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                            }

                                            val textColor = when {
                                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                                isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                                                else -> MaterialTheme.colorScheme.onSurface
                                            }

                                            val subTextColor = when {
                                                isSelected -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                                                isToday -> MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(bgColor)
                                                    .clickable { selectedDate = date },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    // Event star indicator
                                                    if (eventForDay != null) {
                                                        Icon(
                                                            Icons.Default.Star,
                                                            contentDescription = "Event",
                                                            modifier = Modifier.size(9.dp),
                                                            tint = if (isSelected) Color(0xFFFFD700) else MaterialTheme.colorScheme.tertiary
                                                        )
                                                    }

                                                    // Gregorian day (e.g. 25)
                                                    Text(
                                                        text = "${date.dayOfMonth}",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                        color = textColor
                                                    )

                                                    // Hijri day & month (e.g. 12 صفر)
                                                    Text(
                                                        text = hijriInfo.shortFormatted,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = subTextColor,
                                                        textAlign = TextAlign.Center,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        } else {
                                            Spacer(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Selected Date Details Card
            item {
                val isSelectedToday = selectedDate == LocalDate.now()
                val eventForSelectedDay = events.find { it.gregorianDate == selectedDate.toString() }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isSelectedToday) {
                                    if (currentLang.code == "ps") "📍 د نن ورځې معلومات" else "📍 آج کی منتخب تاریخ"
                                } else {
                                    if (currentLang.code == "ps") "📅 ټاکل شوې نیټه" else "📅 منتخب تاریخ کی تفصیل"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (isSelectedToday) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = todayBtnText,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Full Hijri Date Display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌙 ", fontSize = 18.sp)
                            Column {
                                Text(
                                    text = if (currentLang.code == "ps") "هجري نیټه:" else "ہجری تاریخ:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = selectedHijriDetails.formattedFull,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Full Gregorian Date Display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🗓️ ", fontSize = 18.sp)
                            Column {
                                Text(
                                    text = if (currentLang.code == "ps") "عیسوي نیټه:" else "عیسوی تاریخ:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val dayName = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                val monthName = selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                Text(
                                    text = "$dayName، ${selectedDate.dayOfMonth} $monthName ${selectedDate.year}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Islamic Event if any
                        if (eventForSelectedDay != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = eventForSelectedDay.eventName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        Text(
                                            text = eventForSelectedDay.description,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Month's Islamic Events List (if any)
            if (monthEvents.isNotEmpty()) {
                item {
                    Text(
                        text = if (currentLang.code == "ps") "✨ په دې میاشت کې مهمې اسلامي پیښې" else "✨ اس مہینے کی اہم اسلامی مناسبات",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                items(monthEvents.size) { idx ->
                    val ev = monthEvents[idx]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ev.eventName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${ev.hijriDate} • ${ev.description}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
