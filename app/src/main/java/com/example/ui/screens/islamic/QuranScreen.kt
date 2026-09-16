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
import com.example.data.model.QuranEdition
import com.example.data.repository.QuranRepository
import com.example.ui.navigation.Screen
import com.example.ui.components.GenericBookCover
import com.example.util.PdfManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val quranList = remember { QuranRepository.getAllEditions() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }

    val filters = remember {
        listOf(
            "all" to "تمام نسخہ جات",
            "tajweedi" to "تجویدی نسخے",
            "16" to "۱۶ سطری",
            "15" to "۱۵ سطری (حفاظی)",
            "13" to "۱۳ سطری",
            "10" to "۱۰ سطری (جلی)"
        )
    }

    val filteredList = remember(searchQuery, selectedFilter, quranList) {
        quranList.filter { quran ->
            val matchesFilter = when (selectedFilter) {
                "all" -> true
                "tajweedi" -> quran.isTajweedi
                "16" -> quran.lines == 16
                "15" -> quran.lines == 15
                "13" -> quran.lines == 13
                "10" -> quran.lines == 10
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                quran.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                quran.title.contains(searchQuery, ignoreCase = true) ||
                quran.scriptTypeUrdu.contains(searchQuery, ignoreCase = true) ||
                quran.publisherUrdu.contains(searchQuery, ignoreCase = true) ||
                quran.descriptionUrdu.contains(searchQuery, ignoreCase = true)

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
                            text = "قرآن مجید کے متبرک نسخہ جات",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تجویدی، ۱۵ سطری، ۱۶ سطری، اور تمام معیاری طباعتیں",
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
            // Elegant Quran Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF0F766E))
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
                            text = "القرآن الکریم",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFEF3C7)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "حفظ، تلاوت اور تجوید کے لیے معتبر و تصدیق شدہ نسخے پی ڈی ایف اور آف لائن ڈاؤن لوڈ سہولت کے ساتھ۔",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.92f),
                            lineHeight = 18.sp
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
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color(0xFFFEF3C7),
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
                placeholder = { Text("نسخہ تلاش کریں (مثلاً: ۱۶ سطری، تجویدی، اقراء...)") },
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

            // Editions List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { quran ->
                    QuranEditionCard(
                        quran = quran,
                        pdfManager = pdfManager,
                        onOpen = {
                            onNavigate(Screen.BookViewer.createRoute(quran.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuranEditionCard(
    quran: QuranEdition,
    pdfManager: PdfManager,
    onOpen: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDownloaded by remember(quran.id) { mutableStateOf(pdfManager.isBookDownloaded(quran.id)) }
    var isDownloading by remember { mutableStateOf(false) }
    var progressFraction by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("quran_edition_${quran.id}"),
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
            // Authentic Quran Book Cover
            GenericBookCover(
                title = quran.titleUrdu,
                tag = if (quran.isTajweedi) "تجویدی" else "${quran.lines} سطر",
                badgeText = "${quran.lines} سطری",
                author = "القرآن الکریم",
                themeColor = if (quran.isTajweedi) Color(0xFF047857) else Color(0xFF0F472C),
                width = 62.dp,
                height = 86.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = quran.titleUrdu,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (quran.isTajweedi) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFECFDF5)
                        ) {
                            Text(
                                text = "تجویدی رنگین",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = quran.publisherUrdu,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = quran.descriptionUrdu,
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
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action buttons: Open & Download
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onOpen,
                    colors = if (isDownloaded) {
                        ButtonDefaults.filledTonalButtonColors()
                    } else {
                        ButtonDefaults.buttonColors()
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isDownloaded) Icons.Default.Book else Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isDownloaded) "تلاوت" else "پڑھیں", fontSize = 12.sp)
                }

                // Download icon button
                if (!isDownloaded) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 2.5.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        FilledTonalIconButton(
                            onClick = {
                                isDownloading = true
                                scope.launch {
                                    val result = pdfManager.downloadOrGetPdf(
                                        bookId = quran.id,
                                        pdfUrl = quran.pdfUrl,
                                        onProgress = { _, _, fraction ->
                                            progressFraction = fraction
                                        }
                                    )
                                    isDownloading = false
                                    if (result.isSuccess) {
                                        isDownloaded = true
                                        pdfManager.copyPdfToDeviceDownloads(quran.id, quran.titleUrdu)
                                        Toast.makeText(
                                            context,
                                            "${quran.titleUrdu} کامیابی سے ڈاؤن لوڈ ہو گیا",
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
