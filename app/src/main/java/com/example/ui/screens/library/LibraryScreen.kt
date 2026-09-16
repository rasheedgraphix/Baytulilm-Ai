package com.example.ui.screens.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DarjaDoc
import com.example.ui.components.DarjaClassIconBadge
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.DarsNizamiMatcher
import com.example.util.LocalAppLanguage
import com.example.util.lStr

@Composable
fun LibraryScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val darjatList by viewModel.firebaseRepository.darjatListStream.collectAsStateWithLifecycle()
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    
    // 0 = اصل کتب (Original Books/Matan), 1 = شروحات و حواشی (Commentaries)
    var selectedTab by remember { mutableIntStateOf(0) }
    val currentMode = if (selectedTab == 0) "books" else "shuroohat"

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterCategory by remember { mutableStateOf("all") }

    val lang = LocalAppLanguage.current
    val langCode = lang.code

    // Pre-calculate counts based on mode
    val totalOriginalBooksCount = remember(allBooks) {
        allBooks.count { DarsNizamiMatcher.isOriginalBook(it.title, it.type, it.description) }
    }
    val totalShuroohatCount = remember(allBooks) {
        allBooks.count { DarsNizamiMatcher.isSharah(it.title, it.type, it.description) }
    }

    val filteredDarjat = darjatList.filter { darja ->
        val matchesSearch = searchQuery.isBlank() ||
                darja.name.contains(searchQuery, ignoreCase = true) ||
                darja.arabicName.contains(searchQuery, ignoreCase = true) ||
                darja.urduName.contains(searchQuery, ignoreCase = true) ||
                darja.description.contains(searchQuery, ignoreCase = true)

        val matchesCategory = when (selectedFilterCategory) {
            "early" -> darja.order in 1..3
            "middle" -> darja.order in 4..6
            "advanced" -> darja.order in 7..8
            else -> true
        }

        matchesSearch && matchesCategory
    }

    val classStats = remember(allBooks, filteredDarjat, selectedTab) {
        filteredDarjat.associate { darja ->
            val matchedAll = DarsNizamiMatcher.getBooksForClass(allBooks, darja.id, darja.order)
            val matchedFiltered = if (selectedTab == 0) {
                matchedAll.filter { DarsNizamiMatcher.isOriginalBook(it.title, it.type, it.description) }
            } else {
                matchedAll.filter { DarsNizamiMatcher.isSharah(it.title, it.type, it.description) }
            }
            val distinctSubjects = matchedFiltered.map { DarsNizamiMatcher.normalizeSubject(it.subject) }.filter { it.isNotBlank() }.distinct()
            val displayBookCount = matchedFiltered.size
            val displaySubjectCount = if (distinctSubjects.isNotEmpty()) distinctSubjects.size else darja.subjectCount
            darja.id to Pair(displayBookCount, displaySubjectCount)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Scholarly Header with Mode Tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF094D31),
                                Color(0xFF0E6945),
                                Color(0xFF147A52)
                            )
                        )
                    )
                    .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 18.dp)
            ) {
                Column {
                    // Top Sub-Badge: Academic Category Tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFD4AF37).copy(alpha = 0.22f),
                            border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.45f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = if (selectedTab == 0) Icons.Default.MenuBook else Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = Color(0xFFFFDF78),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (selectedTab == 0) "نصابی کتب (متون)" else "شروحات و حواشی",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFDF78)
                                )
                            }
                        }

                        // Total Count Badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (selectedTab == 0) "$totalOriginalBooksCount+ اصل کتب" else "$totalShuroohatCount+ شروحات",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Title & Subtitle
                    Text(
                        text = if (selectedTab == 0) "مکتبہ نصابی کتب" else "مکتبہ شروحات و تراجم",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (selectedTab == 0)
                            "درسِ نظامی کے تمام ۸ درجات کی اصل درسی کتب اور مستند متون"
                        else
                            "درسِ نظامی کے تمام ۸ درجات کی مفصل شروحات، حواشی، دروس اور تقاریر",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.88f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // TWO MAIN TABS: کتب (Books) vs شروحات (Shuroohat)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.Black.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Tab 1: Books (اصل کتب)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedTab == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedTab = 0 }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (langCode == "ur" || langCode == "ps") "کتب (متون)" else "Books (Matan)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.White
                                    )
                                }
                            }

                            // Tab 2: Shuroohat (شروحات)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedTab == 1) MaterialTheme.colorScheme.surface else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedTab = 1 }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (langCode == "ur" || langCode == "ps") "شروحات و حواشی" else "Shuroohat",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Enhanced Modern Floating Search Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = if (selectedTab == 0) "درجہ یا نصابی کتاب کا نام تلاش کریں..." else "درجہ یا شرح کا نام تلاش کریں...",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = lStr("search"),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = lStr("cancel"),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            // Quick Category Filters (8 Classes breakdown)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilterCategory == "all",
                        onClick = { selectedFilterCategory = "all" },
                        label = {
                            Text(
                                text = "تمام ۸ درجات",
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilterCategory == "all") FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilterCategory == "early",
                        onClick = { selectedFilterCategory = "early" },
                        label = {
                            Text(
                                text = if (langCode == "ur" || langCode == "ps") "اولیٰ تا ثالثہ (1-3)" else "Years 1 - 3",
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilterCategory == "early") FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilterCategory == "middle",
                        onClick = { selectedFilterCategory = "middle" },
                        label = {
                            Text(
                                text = if (langCode == "ur" || langCode == "ps") "رابعہ تا سادسہ (4-6)" else "Years 4 - 6",
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilterCategory == "middle") FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilterCategory == "advanced",
                        onClick = { selectedFilterCategory = "advanced" },
                        label = {
                            Text(
                                text = if (langCode == "ur" || langCode == "ps") "سابعہ و دورہ حدیث (7-8)" else "Years 7 - 8 (Daura)",
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilterCategory == "advanced") FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Section Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Default.MenuBook else Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedTab == 0) "۸ درجات — صرف نصابی کتب (متون)" else "۸ درجات — صرف شروحات و حواشی",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${filteredDarjat.size} درجات",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Darjat Cards List (Showing either pure Books or pure Shuroohat)
            if (filteredDarjat.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = lStr("no_books_found"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "کوئی درجہ تلاش نہیں ہو سکا۔ برائے مہربانی نام درست درج کریں۔",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredDarjat, key = { it.id }) { darja ->
                        val stats = classStats[darja.id] ?: Pair(darja.bookCount, darja.subjectCount)

                        DarjaCardItem(
                            darja = darja,
                            mode = currentMode,
                            calculatedBookCount = stats.first,
                            calculatedSubjectCount = stats.second,
                            onOpenClick = {
                                onNavigate(Screen.DarjaDetail.createRoute(darja.id, currentMode))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DarjaCardItem(
    darja: DarjaDoc,
    mode: String = "books",
    calculatedBookCount: Int,
    calculatedSubjectCount: Int = darja.subjectCount,
    onOpenClick: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val langCode = lang.code

    val isBooksMode = mode == "books"
    val countBadgeLabel = if (isBooksMode) "کتب" else "شروحات"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Main Top Row (Thumbnail + Content)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Prominent Arabic Class Icon Badge (e.g. الصف الأول)
                DarjaClassIconBadge(
                    classId = darja.id,
                    className = darja.name,
                    order = darja.order,
                    size = 76.dp
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Title, Arabic Tag, Year & Description
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = darja.getDisplayName(langCode),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Mode Tag (کتب / شروحات)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isBooksMode) Color(0xFF0E6945).copy(alpha = 0.15f) else Color(0xFFD4AF37).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, if (isBooksMode) Color(0xFF0E6945).copy(alpha = 0.4f) else Color(0xFFD4AF37).copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = if (isBooksMode) "نصابی کتب" else "شروحات",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBooksMode) Color(0xFF0E6945) else Color(0xFFC99700),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Secondary Subtitle
                    val secondarySubtitle = if (langCode == "ur" || langCode == "ps") darja.arabicName else darja.name
                    if (secondarySubtitle.isNotBlank()) {
                        Text(
                            text = secondarySubtitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isBooksMode)
                            "${darja.getDisplayName(langCode)} کی اصل درسی کتب اور بنیادی متون۔"
                        else
                            "${darja.getDisplayName(langCode)} کی تمام درسی و غیر درسی شروحات، حواشی اور دروس۔",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metadata Chips (Books Count, Subjects Count, Verified Badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Books/Shuroohat Count Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = if (isBooksMode) Icons.Default.MenuBook else Icons.Default.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$calculatedBookCount $countBadgeLabel",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Subjects Count Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$calculatedSubjectCount ${lStr("subjects")}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Action Button "Open Class"
                Button(
                    onClick = onOpenClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = if (isBooksMode) "کتب کھولیں" else "شروحات کھولیں",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = lStr("open_class"),
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
