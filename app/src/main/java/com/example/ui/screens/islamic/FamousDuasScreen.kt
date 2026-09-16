package com.example.ui.screens.islamic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamousDua
import com.example.data.repository.FamousDuasRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamousDuasScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("سب") }
    var bookmarkedDuaIds by remember { mutableStateOf(setOf<Int>()) }

    val filteredDuas = remember(searchQuery, selectedCategory) {
        val query = searchQuery.trim().lowercase()
        FamousDuasRepository.DUAS.filter { dua ->
            val matchesCategory = selectedCategory == "سب" || dua.category == selectedCategory
            val matchesSearch = query.isEmpty() ||
                    dua.title.lowercase().contains(query) ||
                    dua.arabic.contains(query) ||
                    dua.transliteration.lowercase().contains(query) ||
                    dua.urdu.contains(query) ||
                    dua.reference.lowercase().contains(query)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "۱۰۰ مشہور دعائیں",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE8D9A8)
                        )
                        Text(
                            text = "روزمرہ زندگی کے لیے ۱۰۰ دعائیں",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFC5A253)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("famous_duas_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "واپس جائیں",
                            tint = Color(0xFFE8D9A8)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F2E26)
                ),
                actions = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "تلاش صاف کریں",
                                tint = Color(0xFFE8D9A8)
                            )
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFFDF8ED)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            // Hero Header Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F2E26))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Color(0xFFC5A253).copy(alpha = 0.4f)),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(Color(0xFFC5A253), CircleShape)
                                )
                                Text(
                                    text = "١٠٠ دُعَاءٍ مَشْهُورَةٍ",
                                    color = Color(0xFFE8D9A8),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Text(
                            text = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ",
                            color = Color(0xFFE8D9A8),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "۱۰۰ مشہور دعائیں",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "عربی دعا • اردو ترجمہ • تلفظ • مستند حوالہ",
                            color = Color(0xFFC5A253),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_duas_input"),
                            placeholder = {
                                Text(
                                    text = "دعا تلاش کریں... جیسے نیند، سفر، کھانا",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color(0xFFC5A253)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.12f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                                focusedBorderColor = Color(0xFFC5A253),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }
            }

            // Categories Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDF8ED))
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FamousDuasRepository.CATEGORIES.forEach { category ->
                        val isSelected = category == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = category },
                            label = {
                                Text(
                                    text = category,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFC5A253),
                                selectedLabelColor = Color(0xFF0F2E26),
                                containerColor = Color.White,
                                labelColor = Color(0xFF1A2E26)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFFC5A253) else Color(0xFFE8D9A8)
                            ),
                            modifier = Modifier.testTag("dua_category_$category")
                        )
                    }
                }
            }

            // Results count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredDuas.size} دعائیں دستیاب ہیں",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6D5A2F)
                    )
                    Text(
                        text = "زمرہ: $selectedCategory",
                        fontSize = 12.sp,
                        color = Color(0xFF8A7A5A)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Empty state
            if (filteredDuas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = Color(0xFFC5A253),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "کوئی دعا نہیں ملی",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F2E26)
                            )
                            Text(
                                text = "براہ کرم دوسرا لفظ تلاش کریں",
                                fontSize = 13.sp,
                                color = Color(0xFF8A7A5A)
                            )
                        }
                    }
                }
            }

            // List of Duas
            items(filteredDuas, key = { it.id }) { dua ->
                val isBookmarked = bookmarkedDuaIds.contains(dua.id)
                DuaCardItem(
                    dua = dua,
                    isBookmarked = isBookmarked,
                    onToggleBookmark = {
                        bookmarkedDuaIds = if (isBookmarked) {
                            bookmarkedDuaIds - dua.id
                        } else {
                            bookmarkedDuaIds + dua.id
                        }
                    },
                    onCopyDua = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val textToCopy = "${dua.title}\n\n${dua.arabic}\n\nترجمہ: ${dua.urdu}\n\nحوالہ: ${dua.reference}"
                        val clip = ClipData.newPlainText("Dua", textToCopy)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "دعا کاپی ہو گئی!", Toast.LENGTH_SHORT).show()
                    },
                    onShareDua = {
                        val shareText = "✨ ${dua.title} ✨\n\n${dua.arabic}\n\nتلفظ:\n${dua.transliteration}\n\nترجمہ:\n${dua.urdu}\n\n📖 حوالہ: ${dua.reference}\n\n(بیت العلم - ۱۰۰ مشہور دعائیں)"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, dua.title)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "دعا شیئر کریں"))
                    }
                )
            }

            // Bottom Quran Ayah Inspiration Card
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0F2E26)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "وَقَالَ رَبُّكُمُ ادْعُونِي أَسْتَجِبْ لَكُمْ",
                            color = Color(0xFFE8D9A8),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "اور تمہارے رب نے فرمایا: مجھ سے دعا کرو میں تمہاری دعا قبول کروں گا۔",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "القرآن: سورة غافر (40:60)",
                            color = Color(0xFFC5A253),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DuaCardItem(
    dua: FamousDua,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onCopyDua: () -> Unit,
    onShareDua: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("dua_card_${dua.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE8D9A8).copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Gradient Top Accent Line (Red -> Gold -> Green)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFC62828),
                                Color(0xFFC5A253),
                                Color(0xFF2E7D32)
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Number Circle Badge
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F2E26),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "%02d".format(dua.id),
                                    color = Color(0xFFE8D9A8),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column {
                            Text(
                                text = dua.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F2E26)
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFDF8ED),
                                border = BorderStroke(1.dp, Color(0xFFE8D9A8)),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = dua.category,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    color = Color(0xFF8A6D2B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Bookmark action icon
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFC5A253) else Color(0xFF8A7A5A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Arabic Container (Blue Styled Box matching user artifact)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFDBEAFE),
                    border = BorderStroke(1.5.dp, Color(0xFF93C5FD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.9f),
                                border = BorderStroke(1.dp, Color(0xFF93C5FD))
                            ) {
                                Text(
                                    text = "عربی دعا",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                            }

                            Text(
                                text = "﷽",
                                fontSize = 13.sp,
                                color = Color(0xFF2563EB).copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = dua.arabic,
                            fontSize = 22.sp,
                            lineHeight = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Urdu Translation Container (Green Styled Box matching user artifact)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFDDFFDD),
                    border = BorderStroke(1.5.dp, Color(0xFF81C784)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.9f),
                                border = BorderStroke(1.dp, Color(0xFF81C784))
                            ) {
                                Text(
                                    text = "اردو ترجمہ",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dua.urdu,
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F2E26),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Transliteration Container
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFDF8ED),
                    border = BorderStroke(1.dp, Color(0xFFF0E6C8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "تلفظ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA08A5A)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = dua.transliteration,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF4A5A52),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Footer & Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reference Tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFDF8ED),
                        border = BorderStroke(1.dp, Color(0xFFF0E6C8))
                    ) {
                        Text(
                            text = "حوالہ: ${dua.reference}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = Color(0xFF6D5A2F),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Copy & Share buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = onCopyDua,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "کاپی کریں",
                                tint = Color(0xFF1B5E20),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onShareDua,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "شیئر کریں",
                                tint = Color(0xFF1E40AF),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
