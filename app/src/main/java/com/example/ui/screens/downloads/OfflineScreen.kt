package com.example.ui.screens.downloads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BookEntity
import com.example.ui.components.BookCoverThumbnailView
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage
import com.example.util.lStr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val appLanguage = LocalAppLanguage.current
    val langCode = appLanguage.code
    val isRtl = appLanguage.isRtl
    val downloadedBooks by viewModel.downloadedBooks.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("All") } // "All", "Books", "PDFs", "Notes", "Quiz Packs", "Flashcards"
    var isMultiSelectMode by remember { mutableStateOf(false) }
    val selectedItemIds = remember { mutableStateListOf<String>() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("All", "Books", "PDFs", "Notes", "Quiz Packs", "Flashcards")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DownloadDone,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lStr("offline"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    val headerSubtitle = when (langCode) {
                        "ps" -> "د انټرنیټ پرته ډاونلوډ شوي کتابونه، پی ډی ایفونه او نوټونه ولولئ"
                        "ur" -> "انٹرنیٹ کے بغیر ڈاؤن لوڈ کردہ کتب، پی ڈی ایف، نوٹس اور کوئز پیک پڑھیں"
                        else -> "Read downloaded Kutub, PDFs, notes & quiz packs without internet"
                    }
                    Text(
                        text = headerSubtitle,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                if (downloadedBooks.isNotEmpty()) {
                    IconButton(onClick = {
                        isMultiSelectMode = !isMultiSelectMode
                        if (!isMultiSelectMode) selectedItemIds.clear()
                    }) {
                        Icon(
                            imageVector = if (isMultiSelectMode) Icons.Default.Close else Icons.Default.SelectAll,
                            contentDescription = "Multi Select",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Storage Info Bar
        val storageUsedLabel = when (langCode) {
            "ps" -> "کارول شوې حافظه: ${String.format("%.1f", downloadedBooks.size * 28.5f)} MB"
            "ur" -> "استعمال شدہ میموری: ${String.format("%.1f", downloadedBooks.size * 28.5f)} MB"
            else -> "Storage Used: ${String.format("%.1f", downloadedBooks.size * 28.5f)} MB"
        }
        val deviceFreeLabel = when (langCode) {
            "ps" -> "خالي ځای: 34.8 GB"
            "ur" -> "مفت اسپیس: 34.8 GB"
            else -> "Device Free: 34.8 GB"
        }
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = storageUsedLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = deviceFreeLabel,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Filter Category Chips
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 12.dp,
            divider = {}
        ) {
            categories.forEach { category ->
                val selected = selectedCategory == category
                val categoryName = when (category) {
                    "All" -> when (langCode) { "ps" -> "ټول"; "ur" -> "تمام"; else -> "All" }
                    "Books" -> when (langCode) { "ps" -> "کتابونه"; "ur" -> "کتب"; else -> "Books" }
                    "PDFs" -> "PDFs"
                    "Notes" -> when (langCode) { "ps" -> "نوټونه"; "ur" -> "نوٹس"; else -> "Notes" }
                    "Quiz Packs" -> when (langCode) { "ps" -> "ازموینې کڅوړې"; "ur" -> "کوئز پیک"; else -> "Quiz Packs" }
                    "Flashcards" -> when (langCode) { "ps" -> "فلش کارډونه"; "ur" -> "فلیش کارڈز"; else -> "Flashcards" }
                    else -> category
                }
                Tab(
                    selected = selected,
                    onClick = { selectedCategory = category }
                ) {
                    Text(
                        text = categoryName,
                        fontSize = 12.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        Divider()

        // Multi-select Action Bar
        AnimatedVisibility(visible = isMultiSelectMode) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val itemsSelectedText = when (langCode) {
                        "ps" -> "${selectedItemIds.size} توکي وټاکل شول"
                        "ur" -> "${selectedItemIds.size} آئٹمز منتخب ہیں"
                        else -> "${selectedItemIds.size} items selected"
                    }
                    Text(
                        text = itemsSelectedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Row {
                        TextButton(onClick = {
                            selectedItemIds.forEach { viewModel.deleteDownload(it) }
                            snackbarMessage = when (langCode) {
                                "ps" -> "${selectedItemIds.size} توکي پاک شول"
                                "ur" -> "${selectedItemIds.size} ڈاؤن لوڈ شدہ آئٹمز حذف کر دیے گئے"
                                else -> "Deleted ${selectedItemIds.size} downloaded items"
                            }
                            selectedItemIds.clear()
                            isMultiSelectMode = false
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red)
                            Spacer(modifier = Modifier.width(4.dp))
                            val deleteSelectedText = when (langCode) {
                                "ps" -> "ټاکل شوي پاک کړئ"
                                "ur" -> "منتخب حذف کریں"
                                else -> "Delete Selected"
                            }
                            Text(deleteSelectedText, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Content
        if (downloadedBooks.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val emptyTitle = when (langCode) {
                        "ps" -> "تر اوسه هیڅ آفلاین توکي نشته"
                        "ur" -> "ابھی تک کوئی آف لائن مواد نہیں ہے"
                        else -> "No Offline Content Yet"
                    }
                    Text(
                        text = emptyTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val emptyDesc = when (langCode) {
                        "ps" -> "د کتابتون څخه کتابونه، پی ډی ایفونه او نوټونه ډاونلوډ کړئ ترڅو هر وخت د انټرنیټ پرته ورته لاسرسی ومومئ."
                        "ur" -> "لائبریری سے کتب، پی ڈی ایف اور نوٹس ڈاؤن لوڈ کریں تاکہ بغیر انٹرنیٹ کے کسی بھی وقت مطالعہ کر سکیں۔"
                        else -> "Download Kutub, PDFs, Notes and Quiz Packs from the Library to access them anytime without internet connection."
                    }
                    Text(
                        text = emptyDesc,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onNavigate(Screen.Library.route) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val libraryBtnText = when (langCode) {
                            "ps" -> "کتابتون ته لاړ شئ"
                            "ur" -> "لائبریری کھولیں"
                            else -> "Go to Library"
                        }
                        Text(libraryBtnText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(downloadedBooks, key = { it.id }) { book ->
                    val isSelected = selectedItemIds.contains(book.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable {
                                if (isMultiSelectMode) {
                                    if (isSelected) selectedItemIds.remove(book.id)
                                    else selectedItemIds.add(book.id)
                                } else {
                                    onNavigate(Screen.BookViewer.createRoute(book.id))
                                }
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var thumb by remember(book.id) { mutableStateOf<android.graphics.Bitmap?>(null) }
                                LaunchedEffect(book.id) {
                                    thumb = viewModel.getThumbnail(book)
                                }
                                BookCoverThumbnailView(
                                    book = book,
                                    thumbnail = thumb,
                                    width = 38.dp,
                                    height = 54.dp
                                )
                                if (isMultiSelectMode) {
                                    Checkbox(checked = isSelected, onCheckedChange = {
                                        if (it) selectedItemIds.add(book.id)
                                        else selectedItemIds.remove(book.id)
                                    })
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(book.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${book.darja} • ${book.subject}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { 
                                    viewModel.deleteDownload(book.id) 
                                    snackbarMessage = "Removed ${book.title}"
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        snackbarMessage?.let { msg ->
            LaunchedEffect(msg) {
                kotlinx.coroutines.delay(3000)
                snackbarMessage = null
            }
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { snackbarMessage = null }) {
                        Text("OK", color = MaterialTheme.colorScheme.inversePrimary)
                    }
                }
            ) {
                Text(msg)
            }
        }
    }
}
