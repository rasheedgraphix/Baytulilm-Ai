package com.example.ui.screens.islamic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AsmaUlHusna
import com.example.data.repository.IslamicData
import kotlinx.coroutines.delay

private val HUSNA_PALETTE = listOf(
    Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFF45B7D1), Color(0xFFFFA600),
    Color(0xFF96CEB4), Color(0xFFFFEAA7), Color(0xFFDDA0DD), Color(0xFF98D8C8),
    Color(0xFFF7DC6F), Color(0xFFBB8FCE), Color(0xFFFF9A9E), Color(0xFFA8E6CF)
)

private val HUSNA_GRADIENTS = listOf(
    listOf(Color(0xFFFF6B6B), Color(0xFFFFA600), Color(0xFF4ECDC4), Color(0xFF45B7D1), Color(0xFFBB8FCE)),
    listOf(Color(0xFFFFD86F), Color(0xFFFF9A9E), Color(0xFFFF6B6B), Color(0xFFDDA0DD)),
    listOf(Color(0xFF4ECDC4), Color(0xFF2ECC9A), Color(0xFFFFEAA7), Color(0xFFFFA600)),
    listOf(Color(0xFF45B7D1), Color(0xFF6C5CE7), Color(0xFFFD79A8), Color(0xFFFFA600)),
    listOf(Color(0xFF00B894), Color(0xFF55EFC4), Color(0xFFFFEAA7), Color(0xFFFDCB6E)),
    listOf(Color(0xFFE17055), Color(0xFFFFA600), Color(0xFFFFEAA7), Color(0xFF74B9FF)),
    listOf(Color(0xFFA29BFE), Color(0xFF81ECEC), Color(0xFFFFEAA7), Color(0xFFFF7675)),
    listOf(Color(0xFFFF9A9E), Color(0xFFFAD390), Color(0xFF6AB8FF), Color(0xFFA8E6CF)),
    listOf(Color(0xFFF7DC6F), Color(0xFFF39C12), Color(0xFFFF6B6B), Color(0xFFBB8FCE)),
    listOf(Color(0xFF0984E3), Color(0xFF4ECDC4), Color(0xFFFFEAA7), Color(0xFFFF6B6B)),
    listOf(Color(0xFFDDA0DD), Color(0xFFFF6B6B), Color(0xFFFFA600), Color(0xFF55EFC4)),
    listOf(Color(0xFFFFEAA7), Color(0xFFF7DC6F), Color(0xFF4ECDC4), Color(0xFF45B7D1), Color(0xFF9B59B6))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaUlHusnaScreen(onNavigateBack: () -> Unit) {
    val names = IslamicData.ASMA_UL_HUSNA
    var searchQuery by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var tasbeehCount by remember { mutableIntStateOf(0) }
    var isAutoPlaying by remember { mutableStateOf(false) }
    var autoIndex by remember { mutableIntStateOf(0) }

    val gridState = rememberLazyGridState()

    val filteredNames = remember(searchQuery) {
        if (searchQuery.isBlank()) names
        else names.filter {
            it.arabicName.contains(searchQuery, ignoreCase = true) ||
            it.urduMeaning.contains(searchQuery, ignoreCase = true) ||
            it.transliteration.contains(searchQuery, ignoreCase = true) ||
            it.id.toString() == searchQuery.trim()
        }
    }

    // Auto Zikr loop
    LaunchedEffect(isAutoPlaying, filteredNames.size) {
        if (isAutoPlaying && filteredNames.isNotEmpty()) {
            while (true) {
                delay(1400)
                autoIndex = (autoIndex + 1) % filteredNames.size
                tasbeehCount++
                if (autoIndex < filteredNames.size) {
                    gridState.animateScrollToItem(autoIndex)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "اسماء الحسنیٰ",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFEAA7)
                        )
                        Text(
                            "99 Blessed Names of Allah Almighty",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4ECDC4)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFFEAA7)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF08070B)
                )
            )
        },
        containerColor = Color(0xFF08070B)
    ) { padding ->
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 165.dp),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF08070B))
        ) {
            // Header span
            item(span = { GridItemSpan(maxLineSpan) }) {
                HusnaHeaderSection(
                    tasbeehCount = tasbeehCount,
                    onResetCount = { tasbeehCount = 0 },
                    isAutoPlaying = isAutoPlaying,
                    onToggleAuto = { isAutoPlaying = !isAutoPlaying },
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    autoIndex = autoIndex
                )
            }

            // Cards
            itemsIndexed(filteredNames, key = { _, item -> item.id }) { index, item ->
                val isCurrentAuto = isAutoPlaying && index == autoIndex
                val isSelected = selectedId == item.id
                val gradient = HUSNA_GRADIENTS[index % HUSNA_GRADIENTS.size]

                HusnaNameCard(
                    name = item,
                    index = index,
                    gradient = gradient,
                    isActive = isCurrentAuto || isSelected,
                    isExpanded = isSelected,
                    onClick = {
                        selectedId = if (selectedId == item.id) null else item.id
                        tasbeehCount++
                        autoIndex = index
                    }
                )
            }

            // Empty state
            if (filteredNames.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "کوئی نام نہیں ملا — تلاش بدل کر دیکھیں",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Footer span
            item(span = { GridItemSpan(maxLineSpan) }) {
                HusnaFooterSection()
            }
        }
    }
}

@Composable
private fun HusnaHeaderSection(
    tasbeehCount: Int,
    onResetCount: () -> Unit,
    isAutoPlaying: Boolean,
    onToggleAuto: () -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    autoIndex: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bismillah badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFD4AF37).copy(alpha = 0.15f), Color(0xFF4ECDC4).copy(alpha = 0.15f))
                    )
                )
                .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.25f), RoundedCornerShape(30.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5E6B8),
                textAlign = TextAlign.Center
            )
        }

        // Title
        Text(
            text = "اسماء الحسنیٰ",
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFFFD86F), Color(0xFFFFEAA7), Color(0xFFD4AF37))
                ),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "Asma ul Husna",
            fontSize = 15.sp,
            letterSpacing = 2.5.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "اللہ کے 99 نام",
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF4ECDC4), Color(0xFFFFEAA7), Color(0xFFFF6B6B))
                ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "ہر نام مکمل جڑا ہوا — عام عربی خط میں رنگین گریڈینٹ، ہر نام کے ساتھ اردو معنی",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Action controls row (Tasbeeh counter, Auto Zikr, Search)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tasbeeh Counter Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFF8A6A18)))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("ﷻ", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("TASBEEH", fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                    Text(
                        String.format("%03d", tasbeehCount),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable(onClick = onResetCount)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("صفر", fontSize = 10.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Auto Zikr Button
            val infiniteTransition = rememberInfiniteTransition(label = "pulseHusna")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlphaHusna"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        if (isAutoPlaying) Color(0xFFD4AF37)
                        else Color(0x1AFFFFFF)
                    )
                    .border(
                        1.dp,
                        if (isAutoPlaying) Color(0xFFD4AF37) else Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(30.dp)
                    )
                    .clickable(onClick = onToggleAuto)
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(if (isAutoPlaying) pulseAlpha else 1f)
                        .clip(CircleShape)
                        .background(if (isAutoPlaying) Color.Black else Color(0xFF4ECDC4))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAutoPlaying) "روک دیں — Pause" else "ذکر شروع — Auto",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isAutoPlaying) Color.Black else Color.White
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(top = 2.dp),
            placeholder = { Text("تلاش — نام یا معنی / Search Name", fontSize = 13.sp, color = Color.White.copy(alpha = 0.4f)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF4ECDC4)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0x0FFFFFFF),
                unfocusedContainerColor = Color(0x0AFFFFFF),
                focusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.6f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        // Visualizer audio bars
        Row(
            modifier = Modifier
                .height(24.dp)
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val count = 28
            for (i in 0 until count) {
                val color = HUSNA_PALETTE[i % HUSNA_PALETTE.size]
                val height = if (isAutoPlaying) {
                    val factor = ((Math.sin((autoIndex * 0.9 + i) * 1.2) + 1.0) / 2.0).toFloat()
                    (6 + factor * 16).dp
                } else 4.dp
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(height)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color.copy(alpha = if (isAutoPlaying) 0.9f else 0.25f))
                )
            }
        }
    }
}

@Composable
private fun HusnaNameCard(
    name: AsmaUlHusna,
    index: Int,
    gradient: List<Color>,
    isActive: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (isActive) 1.03f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "husnaScale"
    )

    Box(
        modifier = Modifier
            .scale(scaleAnim)
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isActive) Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFFFFEAA7), Color(0xFFD4AF37)))
                else Brush.linearGradient(listOf(Color(0x22FFFFFF), Color(0x08FFFFFF)))
            )
            .padding(1.dp) // border thickness
            .clip(RoundedCornerShape(21.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF15131C), Color(0xFF0E0D13))
                )
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Header inside card: Number (left) & ﷻ (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", name.id),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFEAA7)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.06f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ﷻ",
                        fontSize = 12.sp,
                        color = Color(0xFFFFEAA7),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Name in Vibrant Multi-Color Gradient
            Text(
                text = name.arabicName,
                style = TextStyle(
                    brush = Brush.linearGradient(gradient),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Divider gradient line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.18f), Color.Transparent)
                        )
                    )
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Transliteration
            if (name.transliteration.isNotBlank()) {
                Text(
                    text = name.transliteration.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Urdu Meaning
            Text(
                text = name.urduMeaning,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFF7E7B5),
                textAlign = TextAlign.Center
            )

            // Expandable details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✦ یہ نام اللہ کی صفت ${name.urduMeaning} کو ظاہر کرتا ہے۔ اس کا ذکر دل کو سکون دیتا ہے۔",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    if (name.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name.description,
                            fontSize = 10.sp,
                            color = Color(0xFFFFEAA7)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom 6-segment color stripe
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                for (seg in 0 until 6) {
                    val color = HUSNA_PALETTE[(index + seg) % HUSNA_PALETTE.size]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(color.copy(alpha = if (isActive) 1f else 0.45f))
                    )
                }
            }
        }
    }
}

@Composable
private fun HusnaFooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.25f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا — اور اللہ کے اچھے نام ہیں، انہی سے اسے پکارو (الاعراف 180)",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Text(
            text = "Made with نور • 99 Names • Colorful Harf",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.4f),
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
    }
}
