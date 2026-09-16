package com.example.ui.screens.islamic

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LughatItem
import com.example.data.repository.LughatRepository
import com.example.ui.navigation.Screen
import com.example.ui.components.GenericBookCover
import com.example.util.PdfManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LughatScreen(
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val lughatList = remember { LughatRepository.getAllLughat() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }

    val filters = remember {
        listOf(
            "all" to "تمام لغات",
            "arabic" to "عربی و قرآنی معاجم",
            "farsi" to "فارسی لغات",
            "urdu" to "اردو لغات"
        )
    }

    val filteredList = remember(searchQuery, selectedFilter, lughatList) {
        lughatList.filter { item ->
            val matchesFilter = when (selectedFilter) {
                "all" -> true
                "arabic" -> item.languageTypeUrdu.contains("عربی") || item.languageTypeUrdu.contains("قرآنی")
                "farsi" -> item.languageTypeUrdu.contains("فارسی")
                "urdu" -> item.languageTypeUrdu.contains("اردو")
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                item.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.compilerUrdu.contains(searchQuery, ignoreCase = true) ||
                item.languageTypeUrdu.contains(searchQuery, ignoreCase = true) ||
                item.descriptionUrdu.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    val context = LocalContext.current
    val pdfManager = remember { PdfManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "لغات و معاجمِ علمیہ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "عربی، فارسی، اردو اور قرآنی مستند لغات و ڈکشنری",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "واپس"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Elegant Banner for Lughat
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF831843), Color(0xFF9D174D), Color(0xFFBE185D))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "قاموس و لغات کتب خانہ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFDF2F8)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "درسِ نظامی، عربی و فارسی ادب، اور حلِ لغات القرآن کے لیے معتبر ترین معاجم کا ذخیرہ۔",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.92f),
                            lineHeight = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Spellcheck,
                                contentDescription = null,
                                tint = Color(0xFFFDF2F8),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                placeholder = { Text("لغت تلاش کریں (مثلاً: مصباح، غیاث، فیروز، قاموس...)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "تلاش"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "صاف کریں"
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            // Lughat Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    LughatCardItem(
                        item = item,
                        pdfManager = pdfManager,
                        onOpen = {
                            onNavigate(Screen.BookViewer.createRoute(item.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LughatCardItem(
    item: LughatItem,
    pdfManager: PdfManager,
    onOpen: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDownloaded by remember(item.id) { mutableStateOf(pdfManager.isBookDownloaded(item.id)) }
    var isDownloading by remember { mutableStateOf(false) }
    var progressFraction by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("lughat_item_${item.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Authentic Lughat Book Cover
            GenericBookCover(
                title = item.titleUrdu,
                tag = item.languageTypeUrdu,
                badgeText = item.languageTypeUrdu,
                author = item.compilerUrdu.ifBlank { "لغت" },
                themeColor = Color(0xFF831843), // Rich maroon / wine
                width = 62.dp,
                height = 86.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titleUrdu,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFCE7F3)
                    ) {
                        Text(
                            text = item.languageTypeUrdu,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9D174D),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = item.compilerUrdu,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.descriptionUrdu,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                if (isDownloading) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(5.dp),
                        color = Color(0xFF9D174D)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Buttons: Open & Download
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onOpen,
                    colors = if (isDownloaded) {
                        ButtonDefaults.filledTonalButtonColors()
                    } else {
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF9D174D))
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isDownloaded) Icons.Default.Book else Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isDownloaded) "مطالعہ" else "پڑھیں", fontSize = 12.sp)
                }

                if (!isDownloaded) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 2.5.dp,
                            color = Color(0xFF9D174D)
                        )
                    } else {
                        FilledTonalIconButton(
                            onClick = {
                                isDownloading = true
                                scope.launch {
                                    val result = pdfManager.downloadOrGetPdf(
                                        bookId = item.id,
                                        pdfUrl = item.pdfUrl,
                                        onProgress = { _, _, fraction ->
                                            progressFraction = fraction
                                        }
                                    )
                                    isDownloading = false
                                    if (result.isSuccess) {
                                        isDownloaded = true
                                        pdfManager.copyPdfToDeviceDownloads(item.id, item.titleUrdu)
                                        Toast.makeText(
                                            context,
                                            "${item.titleUrdu} کامیابی سے ڈاؤن لوڈ ہو گئی",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val errorMsg = result.exceptionOrNull()?.message ?: "نامعلوم خرابی"
                                        Toast.makeText(context, "ڈاؤن لوڈ میں خرابی: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "ڈاؤن لوڈ کریں",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
