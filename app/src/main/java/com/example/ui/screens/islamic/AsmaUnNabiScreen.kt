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
import com.example.data.model.AsmaUnNabi
import com.example.data.repository.IslamicData
import kotlinx.coroutines.delay

private val NABI_PALETTE = listOf(
    Color(0xFF22C55E), Color(0xFFD4AF37), Color(0xFF10B981), Color(0xFFFACC15),
    Color(0xFF16A34A), Color(0xFFFDE68A), Color(0xFF059669), Color(0xFFFBBF24),
    Color(0xFF34D399), Color(0xFFEAB308), Color(0xFF4ADE80), Color(0xFFFEF08A)
)

private val NABI_GRADIENTS = listOf(
    listOf(Color(0xFF16A34A), Color(0xFF22C55E), Color(0xFFD4AF37), Color(0xFFFDE68A)),
    listOf(Color(0xFF059669), Color(0xFF10B981), Color(0xFFFACC15), Color(0xFFD4AF37)),
    listOf(Color(0xFF22C55E), Color(0xFF4ADE80), Color(0xFFFEF08A), Color(0xFFEAB308)),
    listOf(Color(0xFF047857), Color(0xFF34D399), Color(0xFFFDE68A), Color(0xFFFBBF24)),
    listOf(Color(0xFFD4AF37), Color(0xFFFACC15), Color(0xFF10B981), Color(0xFF065F46)),
    listOf(Color(0xFF065F46), Color(0xFF059669), Color(0xFFFACC15), Color(0xFFFEF08A)),
    listOf(Color(0xFF16A34A), Color(0xFFA3E635), Color(0xFFFDE68A), Color(0xFFD4AF37)),
    listOf(Color(0xFFFBBF24), Color(0xFFD4AF37), Color(0xFF16A34A), Color(0xFF0F766E)),
    listOf(Color(0xFF10B981), Color(0xFF6EE7B7), Color(0xFFFEF9C3), Color(0xFFEAB308)),
    listOf(Color(0xFF14532D), Color(0xFF22C55E), Color(0xFFFDE68A), Color(0xFFFACC15)),
    listOf(Color(0xFFF59E0B), Color(0xFFFDE68A), Color(0xFF22C55E), Color(0xFF047857)),
    listOf(Color(0xFFBBF7D0), Color(0xFF4ADE80), Color(0xFFD4AF37), Color(0xFF92400E))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaUnNabiScreen(onNavigateBack: () -> Unit) {
    val names = IslamicData.ASMA_UN_NABI
    var searchQuery by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var duroodCount by remember { mutableIntStateOf(0) }
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

    // Auto Durood loop
    LaunchedEffect(isAutoPlaying, filteredNames.size) {
        if (isAutoPlaying && filteredNames.isNotEmpty()) {
            while (true) {
                delay(1450)
                autoIndex = (autoIndex + 1) % filteredNames.size
                duroodCount++
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
                            "اسماء النبی ﷺ",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE68A)
                        )
                        Text(
                            "99 Blessed Names of Prophet Muhammad ﷺ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4ADE80)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFDE68A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF050A06)
                )
            )
        },
        containerColor = Color(0xFF050A06)
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
                .background(Color(0xFF050A06))
        ) {
            // Header span
            item(span = { GridItemSpan(maxLineSpan) }) {
                NabiHeaderSection(
                    duroodCount = duroodCount,
                    onResetCount = { duroodCount = 0 },
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
                val gradient = NABI_GRADIENTS[index % NABI_GRADIENTS.size]

                NabiNameCard(
                    name = item,
                    index = index,
                    gradient = gradient,
                    isActive = isCurrentAuto || isSelected,
                    isExpanded = isSelected,
                    onClick = {
                        selectedId = if (selectedId == item.id) null else item.id
                        duroodCount++
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
                NabiFooterSection()
            }
        }
    }
}

@Composable
private fun NabiHeaderSection(
    duroodCount: Int,
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
                .background(Color(0xFF101A11))
                .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.25f), RoundedCornerShape(30.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEAFFF0),
                textAlign = TextAlign.Center
            )
        }

        // Durood Sharif badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF22C55E).copy(alpha = 0.2f), Color(0xFFD4AF37).copy(alpha = 0.2f))
                    )
                )
                .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.35f), RoundedCornerShape(30.dp))
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = "اَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ ﷺ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDE68A),
                textAlign = TextAlign.Center
            )
        }

        // Title
        Text(
            text = "اسماء النبی ﷺ",
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF4ADE80), Color(0xFFFDE68A), Color(0xFFD4AF37))
                ),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "Asma-un-Nabi ﷺ - Hazrat Muhammad ke 99 Naam",
            fontSize = 13.sp,
            letterSpacing = 2.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "نبی کریم ﷺ کے 99 مبارک نام",
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF22C55E), Color(0xFFFDE68A), Color(0xFF16A34A))
                ),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "ہر نام مکمل جڑا ہوا — سبز و سنہری رنگین گریڈینٹ میں، ہر نام کے ساتھ اردو صفت اور درود کی برکت",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Action controls row (Durood counter, Auto Durood, Search)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Durood Counter Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.25f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF14532D)))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("ﷺ", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("DUROOD", fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                    Text(
                        String.format("%03d", duroodCount),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE68A)
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

            // Auto Durood Button
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        if (isAutoPlaying) Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFFD4AF37)))
                        else Brush.horizontalGradient(listOf(Color(0x1AFFFFFF), Color(0x1AFFFFFF)))
                    )
                    .border(
                        1.dp,
                        if (isAutoPlaying) Color(0xFFD4AF37) else Color(0xFF22C55E).copy(alpha = 0.25f),
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
                        .background(if (isAutoPlaying) Color.Black else Color(0xFF22C55E))
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
            placeholder = { Text("تلاش — نام یا صفت / Search Name", fontSize = 13.sp, color = Color.White.copy(alpha = 0.4f)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF4ADE80)) },
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
                focusedBorderColor = Color(0xFF22C55E).copy(alpha = 0.6f),
                unfocusedBorderColor = Color(0xFF22C55E).copy(alpha = 0.2f),
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
                val color = NABI_PALETTE[i % NABI_PALETTE.size]
                val height = if (isAutoPlaying) {
                    val factor = ((Math.sin((autoIndex * 0.8 + i) * 1.3) + 1.0) / 2.0).toFloat()
                    (6 + factor * 16).dp
                } else 4.dp
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(height)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color.copy(alpha = if (isAutoPlaying) 0.95f else 0.25f))
                )
            }
        }
    }
}

@Composable
private fun NabiNameCard(
    name: AsmaUnNabi,
    index: Int,
    gradient: List<Color>,
    isActive: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (isActive) 1.03f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .scale(scaleAnim)
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isActive) Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFFFDE68A), Color(0xFFD4AF37)))
                else Brush.linearGradient(listOf(Color(0x2E22C55E), Color(0x0FFFFFFF)))
            )
            .padding(1.dp) // border thickness
            .clip(RoundedCornerShape(21.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101A11), Color(0xFF080E08))
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
            // Top Header inside card: Number (left) & ﷺ (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", name.id),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE68A)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E).copy(alpha = 0.12f))
                        .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ﷺ",
                        fontSize = 12.sp,
                        color = Color(0xFF4ADE80),
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
                            listOf(Color.Transparent, Color(0xFF22C55E).copy(alpha = 0.35f), Color.Transparent)
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
                    color = Color(0xFF4ADE80).copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Urdu Meaning
            Text(
                text = name.urduMeaning,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFDE68A),
                textAlign = TextAlign.Center
            )

            // Expandable details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF22C55E).copy(alpha = 0.08f))
                        .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✦ یہ مبارک نام حضور ﷺ کی صفت ${name.urduMeaning} کو بیان کرتا ہے۔ اس پر درود پڑھنا باعثِ برکت ہے۔",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    if (!name.reference.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name.reference,
                            fontSize = 10.sp,
                            color = Color(0xFFFDE68A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ",
                        fontSize = 12.sp,
                        color = Color(0xFF4ADE80),
                        fontWeight = FontWeight.Bold
                    )
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
                    val color = NABI_PALETTE[(index + seg) % NABI_PALETTE.size]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(color.copy(alpha = if (isActive) 1f else 0.55f))
                    )
                }
            }
        }
    }
}

@Composable
private fun NabiFooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ — اور ہم نے آپ ﷺ کو تمام جہانوں کے لیے رحمت بنا کر بھیجا (الانبیاء 107)",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Text(
            text = "Asma-un-Nabi ﷺ • 99 Names • Durood Sharif",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.4f),
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
    }
}
