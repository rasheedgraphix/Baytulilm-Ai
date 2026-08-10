package com.example.ui.screens.book

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookEntity
import com.example.ui.viewmodel.MainViewModel
import com.example.util.PdfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookViewerScreen(
    bookId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pdfManager = remember { PdfManager(context) }

    var book by remember { mutableStateOf<BookEntity?>(null) }
    var currentPage by remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(1) }

    // PDF loading state
    var isLoadingPdf by remember { mutableStateOf(true) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var pdfError by remember { mutableStateOf<String?>(null) }

    // Page rendering state
    var renderedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isRenderingPage by remember { mutableStateOf(false) }

    // UI Controls
    var isNightMode by remember { mutableStateOf(false) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var showJumpDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteInput by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var jumpPageInput by remember { mutableStateOf("") }

    // Load Book metadata
    LaunchedEffect(bookId) {
        val loadedBook = viewModel.repository.getBookById(bookId)
        if (loadedBook != null) {
            book = loadedBook
            currentPage = if (loadedBook.lastReadPage in 1..loadedBook.pageCount) loadedBook.lastReadPage else 1
            totalPages = loadedBook.pageCount
        }
    }

    val currentBook = book
    val loadPdf = {
        if (currentBook != null) {
            isLoadingPdf = true
            pdfError = null
            downloadProgress = 0f

            coroutineScope.launch(Dispatchers.IO) {
                val result = pdfManager.downloadOrGetPdf(
                    bookId = currentBook.id,
                    pdfUrl = currentBook.pdfUrl,
                    onProgress = { progress ->
                        downloadProgress = progress
                    }
                )

                result.onSuccess { file ->
                    val actualPages = pdfManager.getPageCount(file)
                    if (actualPages > 0) {
                        pdfFile = file
                        totalPages = actualPages
                        isLoadingPdf = false
                        pdfError = null
                        if (actualPages != currentBook.pageCount) {
                            viewModel.repository.insertBook(currentBook.copy(pageCount = actualPages))
                        }
                    } else {
                        isLoadingPdf = false
                        pdfError = "The PDF file appears to be empty or unreadable."
                    }
                }.onFailure { exception ->
                    isLoadingPdf = false
                    pdfError = exception.message ?: "Failed to download PDF. Please check network connection."
                }
            }
        }
    }

    LaunchedEffect(currentBook?.id) {
        if (currentBook != null) {
            loadPdf()
        }
    }

    // Auto-update reading progress when page changes (Continue Reading feature)
    LaunchedEffect(currentPage, pdfFile) {
        if (currentBook != null && pdfFile != null) {
            viewModel.updateReadingProgress(currentBook, currentPage)
        }
    }

    // Render current page when page index or PDF file changes
    LaunchedEffect(pdfFile, currentPage) {
        val file = pdfFile
        if (file != null) {
            isRenderingPage = true
            val bitmap = pdfManager.renderPageBitmap(
                file = file,
                pageIndex = (currentPage - 1).coerceIn(0, (totalPages - 1).coerceAtLeast(0)),
                targetWidth = 1200
            )
            renderedBitmap = bitmap
            isRenderingPage = false
        }
    }

    if (currentBook == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val readerBg = if (isNightMode) Color(0xFF121413) else Color(0xFFF5F7F5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentBook.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (isLoadingPdf) "Loading PDF..." else "Page $currentPage / $totalPages",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search PDF")
                    }
                    IconButton(onClick = { isNightMode = !isNightMode }) {
                        Icon(
                            imageVector = if (isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Night Mode"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.saveBookmarkPage(
                            bookId = currentBook.id,
                            bookTitle = currentBook.title,
                            page = currentPage,
                            note = "Saved Bookmark Page $currentPage"
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmark Page",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { showNoteDialog = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Add Note")
                    }
                    IconButton(onClick = {
                        viewModel.downloadBook(currentBook)
                    }) {
                        Icon(
                            imageVector = if (currentBook.isDownloaded) Icons.Filled.Download else Icons.Outlined.DownloadForOffline,
                            contentDescription = "Download Offline",
                            tint = if (currentBook.isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (!isLoadingPdf && pdfError == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (currentPage > 1) currentPage-- },
                                enabled = currentPage > 1
                            ) {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Page")
                            }

                            TextButton(onClick = { showJumpDialog = true }) {
                                Text("Page $currentPage / $totalPages", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { if (zoomScale > 0.7f) zoomScale -= 0.15f }) {
                                    Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out")
                                }
                                Text("${(zoomScale * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                IconButton(onClick = { if (zoomScale < 2.5f) zoomScale += 0.15f }) {
                                    Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In")
                                }
                                if (zoomScale != 1.0f) {
                                    IconButton(onClick = { zoomScale = 1.0f }) {
                                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset Zoom", modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            IconButton(
                                onClick = { if (currentPage < totalPages) currentPage++ },
                                enabled = currentPage < totalPages
                            ) {
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Page")
                            }
                        }

                        if (totalPages > 1) {
                            Slider(
                                value = currentPage.toFloat(),
                                onValueChange = { currentPage = it.toInt().coerceIn(1, totalPages) },
                                valueRange = 1f..totalPages.toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(readerBg),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoadingPdf -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 5.dp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Loading PDF Document...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentBook.title,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (downloadProgress > 0f) {
                            LinearProgressIndicator(
                                progress = { downloadProgress },
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${(downloadProgress * 100).toInt()}% downloaded",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Preparing document pages...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                pdfError != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ErrorOutline,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Unable to Load PDF",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = pdfError ?: "An unexpected error occurred while reading the PDF file.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { loadPdf() },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Retry Loading")
                                }
                            }
                        }
                    }
                }

                pdfFile != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(12.dp)
                                .graphicsLayer(
                                    scaleX = zoomScale,
                                    scaleY = zoomScale
                                )
                        ) {
                            if (isRenderingPage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                val bitmap = renderedBitmap
                                if (bitmap != null) {
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("pdf_rendered_page")
                                    ) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = "PDF Page $currentPage of ${currentBook.title}",
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Failed to render page $currentPage",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "- Page $currentPage of $totalPages -",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isNightMode) Color.LightGray else Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

    if (showJumpDialog) {
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = { Text("Jump to Page (1-$totalPages)") },
            text = {
                OutlinedTextField(
                    value = jumpPageInput,
                    onValueChange = { jumpPageInput = it },
                    placeholder = { Text("Enter page number...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val page = jumpPageInput.toIntOrNull()
                    if (page != null && page in 1..totalPages) {
                        currentPage = page
                    }
                    showJumpDialog = false
                    jumpPageInput = ""
                }) {
                    Text("Go")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = { Text("Search Inside ${currentBook.title}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        placeholder = { Text("Type page number or search note...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Enter a page number between 1 and $totalPages to navigate directly.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val targetPage = searchInput.toIntOrNull()
                    if (targetPage != null && targetPage in 1..totalPages) {
                        currentPage = targetPage
                    }
                    showSearchDialog = false
                    searchInput = ""
                }) {
                    Text("Jump")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Add Study Note (Page $currentPage)") },
            text = {
                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    placeholder = { Text("Write your analysis or explanation note...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (noteInput.isNotBlank()) {
                        viewModel.saveBookmarkPage(
                            bookId = currentBook.id,
                            bookTitle = currentBook.title,
                            page = currentPage,
                            note = noteInput
                        )
                    }
                    showNoteDialog = false
                    noteInput = ""
                }) {
                    Text("Save Note")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
