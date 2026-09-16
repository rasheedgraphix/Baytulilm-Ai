package com.example.ui.screens.book

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.ui.components.PdfPageItem
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookEntity
import com.example.data.repository.InitialDataSeed
import com.example.data.repository.TafseerRepository
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppConfig
import com.example.util.PdfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var currentPage by rememberSaveable { mutableIntStateOf(1) }
    var totalPages by rememberSaveable { mutableIntStateOf(1) }

    // PDF loading state
    var isLoadingPdf by remember { mutableStateOf(true) }
    var downloadProgressFraction by remember { mutableFloatStateOf(0f) }
    var downloadStatusText by remember { mutableStateOf("دستاویز تیار کی جا رہی ہے...") }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var pdfError by remember { mutableStateOf<String?>(null) }
    var isDownloadedLocally by remember { mutableStateOf(false) }

    // UI Controls
    var isNightMode by rememberSaveable { mutableStateOf(false) }
    var zoomScale by rememberSaveable { mutableFloatStateOf(1.0f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var showJumpDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteInput by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var jumpPageInput by remember { mutableStateOf("") }

    // Pinch-to-zoom & Pan gesture state
    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        zoomScale = (zoomScale * zoomChange).coerceIn(0.7f, 3.5f)
        if (zoomScale > 1.05f) {
            panOffset += offsetChange
        } else {
            panOffset = Offset.Zero
        }
    }

    // Vertical scrolling list state
    val listState = rememberLazyListState()

    val currentVisiblePage by remember {
        derivedStateOf {
            if (totalPages > 0) {
                (listState.firstVisibleItemIndex + 1).coerceIn(1, totalPages)
            } else 1
        }
    }

    LaunchedEffect(currentVisiblePage) {
        if (!isLoadingPdf && totalPages > 0) {
            currentPage = currentVisiblePage
        }
    }

    var hasScrolledToInitialPage by rememberSaveable(bookId) { mutableStateOf(false) }
    LaunchedEffect(pdfFile, totalPages) {
        if (pdfFile != null && totalPages > 0 && !hasScrolledToInitialPage) {
            val targetIdx = (currentPage - 1).coerceIn(0, totalPages - 1)
            if (targetIdx > 0) {
                listState.scrollToItem(targetIdx)
            }
            hasScrolledToInitialPage = true
        }
    }

    // 1. Resolve Book metadata with fallback
    LaunchedEffect(bookId) {
        com.example.util.KalimaShahadatPlayer.stop()
        val loadedBook = viewModel.repository.getBookById(bookId)
            ?: viewModel.allBooks.value.find { it.id == bookId }
            ?: InitialDataSeed.sampleBooks.find { it.id == bookId }
            ?: TafseerRepository.getTafaseer().flatMap { it.volumes }.find { it.id == bookId }?.let { volume ->
                val tafseer = TafseerRepository.getTafaseer().find { it.id == volume.tafseerId }
                val fullTitleUrdu = if (tafseer != null) "${tafseer.titleUrdu} (${volume.titleUrdu})" else volume.titleUrdu
                val fullTitle = if (tafseer != null) "${tafseer.title} - ${volume.title}" else volume.title
                BookEntity(
                    id = volume.id,
                    title = fullTitle,
                    author = tafseer?.authorUrdu ?: tafseer?.author ?: "Unknown",
                    titleUrdu = fullTitleUrdu,
                    pdfUrl = volume.pdfUrl,
                    pageCount = 0,
                    subject = "Tafseer",
                    darja = "General",
                    language = tafseer?.language ?: "Urdu"
                )
            }
            ?: com.example.data.repository.FatawaRepository.getFatawaList().flatMap { it.volumes }.find { it.id == bookId }?.let { volume ->
                val fatwa = com.example.data.repository.FatawaRepository.getFatawaList().find { it.id == volume.fatawaId }
                val fullTitleUrdu = if (fatwa != null) "${fatwa.titleUrdu} (${volume.titleUrdu})" else volume.titleUrdu
                val fullTitle = if (fatwa != null) "${fatwa.title} - ${volume.title}" else volume.title
                BookEntity(
                    id = volume.id,
                    title = fullTitle,
                    author = fatwa?.authorUrdu ?: fatwa?.author ?: "Unknown",
                    titleUrdu = fullTitleUrdu,
                    pdfUrl = volume.pdfUrl,
                    pageCount = 0,
                    subject = "Fatawa",
                    darja = "General",
                    language = fatwa?.language ?: "Urdu"
                )
            }
            ?: com.example.data.repository.QuranRepository.getEditionById(bookId)?.let { quran ->
                BookEntity(
                    id = quran.id,
                    title = quran.title,
                    author = quran.publisherUrdu,
                    titleUrdu = quran.titleUrdu,
                    pdfUrl = quran.pdfUrl,
                    pageCount = quran.totalPages,
                    subject = "القرآن الکریم",
                    darja = "General",
                    language = "عربی"
                )
            }
            ?: com.example.data.repository.LughatRepository.getLughatById(bookId)?.let { lughat ->
                BookEntity(
                    id = lughat.id,
                    title = lughat.title,
                    author = lughat.compilerUrdu,
                    titleUrdu = lughat.titleUrdu,
                    pdfUrl = lughat.pdfUrl,
                    pageCount = lughat.totalPages,
                    subject = "لغات و معاجم",
                    darja = "General",
                    language = lughat.languageTypeUrdu
                )
            }

        if (loadedBook != null) {
            book = loadedBook
            val initPage = if (loadedBook.lastReadPage in 1..loadedBook.pageCount) loadedBook.lastReadPage else 1
            currentPage = initPage
            totalPages = loadedBook.pageCount.coerceAtLeast(1)
        } else {
            isLoadingPdf = false
            pdfError = "کتاب کی معلومات دستیاب نہیں ہیں۔ براہ کرم واپس جائیں۔"
        }
    }

    val currentBook = book
    val downloadJob = remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    // Function to load and render PDF
    fun loadPdf() {
        val targetBook = currentBook ?: return
        isLoadingPdf = true
        pdfError = null
        downloadProgressFraction = 0f
        downloadStatusText = "صفحات لوڈ ہو رہے ہیں..."

        downloadJob.value?.cancel()
        downloadJob.value = coroutineScope.launch(Dispatchers.IO) {
            val isLocal = pdfManager.isBookDownloaded(targetBook.id)
            withContext(Dispatchers.Main) {
                isDownloadedLocally = isLocal
            }

            val result = pdfManager.downloadOrGetPdf(
                bookId = targetBook.id,
                pdfUrl = targetBook.pdfUrl,
                onProgress = { bytesRead, totalBytes, fraction ->
                    val status = if (totalBytes > 0) {
                        val mbRead = bytesRead / (1024.0 * 1024.0)
                        val mbTotal = totalBytes / (1024.0 * 1024.0)
                        val pct = ((bytesRead.toDouble() / totalBytes.toDouble()) * 100).toInt().coerceIn(0, 100)
                        String.format("لوڈ ہو رہا ہے: %d%% (%.1f / %.1f MB)", pct, mbRead, mbTotal)
                    } else {
                        val mbRead = bytesRead / (1024.0 * 1024.0)
                        String.format("لوڈ ہو رہا ہے: %.1f MB", mbRead)
                    }
                    coroutineScope.launch(Dispatchers.Main) {
                        downloadProgressFraction = if (fraction >= 0f) fraction else 0f
                        downloadStatusText = status
                    }
                }
            )

            result.onSuccess { file ->
                pdfManager.invalidateCoverCache(targetBook.id)
                pdfManager.extractAndSaveFirstPageCover(targetBook.id, file)
                viewModel.repository.setDownloadStatus(targetBook.id, true, 1.0f)
                val actualPages = pdfManager.getPageCount(file).coerceAtLeast(1)

                withContext(Dispatchers.Main) {
                    pdfFile = file
                    totalPages = actualPages
                    isDownloadedLocally = true
                    isLoadingPdf = false
                    pdfError = null

                    if (actualPages != targetBook.pageCount) {
                        viewModel.repository.insertBook(targetBook.copy(pageCount = actualPages))
                    }
                }
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    isLoadingPdf = false
                    pdfError = exception.localizedMessage ?: "PDF لوڈ نہیں ہو سکی۔ براہ کرم انٹرنیٹ کنکشن چیک کریں یا براؤزر میں کھولیں۔"
                }
            }
        }
    }

    // Trigger load when currentBook is available
    LaunchedEffect(currentBook?.id) {
        if (currentBook != null) {
            loadPdf()
        }
    }

    // Auto-update reading progress when page changes
    LaunchedEffect(currentPage, pdfFile) {
        if (currentBook != null && pdfFile != null) {
            viewModel.updateReadingProgress(currentBook, currentPage)
        }
    }

    if (currentBook == null && isLoadingPdf) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("کتاب کھولی جا رہی ہے...", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        }
        return
    }

    val activeBook = currentBook ?: BookEntity(
        id = bookId,
        title = "کتاب",
        author = "",
        subject = "Islamic",
        darja = "Dars-e-Nizami",
        language = "Arabic",
        type = "Main Book",
        description = "",
        pdfUrl = "",
        pageCount = 1
    )

    val readerBg = if (isNightMode) Color(0xFF141816) else Color(0xFFF7F8F6)
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = activeBook.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isLoadingPdf) "لوڈ ہو رہا ہے..." else "صفحہ $currentPage از $totalPages",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search PDF")
                    }
                    IconButton(onClick = { isNightMode = !isNightMode }) {
                        Icon(
                            imageVector = if (isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Night Mode",
                            tint = if (isNightMode) Color(0xFFFFD54F) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        viewModel.saveBookmarkPage(
                            bookId = activeBook.id,
                            bookTitle = activeBook.title,
                            page = currentPage,
                            note = "صفحہ $currentPage پر نشان لگایا گیا"
                        )
                        Toast.makeText(context, "صفحہ $currentPage بک مارک کر لیا گیا", Toast.LENGTH_SHORT).show()
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
                        AppConfig.shareAppWithWebsite(context)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share App"
                        )
                    }
                    IconButton(onClick = {
                        if (activeBook.pdfUrl.isNotBlank()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeBook.pdfUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "لنک کھولنے میں مسئلہ رہا", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open in Browser"
                        )
                    }
                    IconButton(onClick = {
                        if (isDownloadedLocally) {
                            Toast.makeText(context, "یہ کتاب پہلے ہی محفوظ ہے", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.downloadBook(
                                activeBook,
                                onProgress = { downloadProgressFraction = it },
                                onResult = { success ->
                                    if (success) {
                                        isDownloadedLocally = true
                                        Toast.makeText(context, "کتاب کامیابی سے محفوظ ہو گئی", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "ڈاؤنلوڈ مکمل نہیں ہو سکا", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }) {
                        Icon(
                            imageVector = if (isDownloadedLocally) Icons.Default.FileDownloadDone else Icons.Outlined.DownloadForOffline,
                            contentDescription = "Download Offline",
                            tint = if (isDownloadedLocally) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (!isLoadingPdf && pdfError == null && pdfFile != null) {
                Surface(
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    if (currentPage > 1) {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(currentPage - 2)
                                        }
                                    }
                                },
                                enabled = currentPage > 1,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Page", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("پچھلا", fontSize = 12.sp)
                            }

                            TextButton(onClick = { showJumpDialog = true }) {
                                Text("صفحہ $currentPage از $totalPages", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { if (zoomScale > 0.7f) zoomScale = (zoomScale - 0.2f).coerceAtLeast(0.7f) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
                                }
                                Text(
                                    "${(zoomScale * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                IconButton(
                                    onClick = { if (zoomScale < 3.0f) zoomScale = (zoomScale + 0.2f).coerceAtMost(3.0f) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
                                }
                                if (zoomScale != 1.0f) {
                                    IconButton(
                                        onClick = {
                                            zoomScale = 1.0f
                                            panOffset = Offset.Zero
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset Zoom", modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            FilledTonalButton(
                                onClick = {
                                    if (currentPage < totalPages) {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(currentPage)
                                        }
                                    }
                                },
                                enabled = currentPage < totalPages,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("اگلا", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Page", modifier = Modifier.size(16.dp))
                            }
                        }

                        if (totalPages > 1) {
                            Slider(
                                value = currentPage.toFloat(),
                                onValueChange = { targetVal ->
                                    val targetPage = targetVal.toInt().coerceIn(1, totalPages)
                                    currentPage = targetPage
                                    coroutineScope.launch {
                                        listState.scrollToItem(targetPage - 1)
                                    }
                                },
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
                .background(readerBg)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.key) {
                            Key.VolumeUp -> {
                                if (currentPage > 1) {
                                    coroutineScope.launch { listState.animateScrollToItem(currentPage - 2) }
                                }
                                true
                            }
                            Key.VolumeDown -> {
                                if (currentPage < totalPages) {
                                    coroutineScope.launch { listState.animateScrollToItem(currentPage) }
                                }
                                true
                            }
                            else -> false
                        }
                    } else false
                }
                .focusRequester(focusRequester)
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
            when {
                isLoadingPdf -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(54.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "کتاب کھولی جا رہی ہے...",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeBook.title,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { downloadProgressFraction.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val pct = (downloadProgressFraction * 100).toInt().coerceIn(0, 100)
                        Text(
                            text = if (pct > 0) "لوڈ ہو رہا ہے... $pct%" else "لوڈ ہو رہا ہے...",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = { 
                                downloadJob.value?.cancel()
                                downloadJob.value = null
                                isLoadingPdf = false 
                                pdfError = "لوڈنگ منسوخ کر دی گئی"
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("cancel_download_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "منسوخ کریں",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("منسوخ کریں", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = downloadStatusText,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Quick Fallback Button during download
                        OutlinedButton(
                            onClick = {
                                if (activeBook.pdfUrl.isNotBlank()) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeBook.pdfUrl))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "لنک کھولنے میں مسئلہ رہا", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("براؤزر میں کھولیں", fontSize = 12.sp)
                        }
                    }
                }

                pdfError != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
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
                                text = "کتاب کھولنے میں مسئلہ پیش آیا",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = pdfError ?: "فائل حاصل کرتے وقت انٹرنیٹ کنکشن کا مسئلہ رہا۔",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { loadPdf() },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("دوبارہ کوشش کریں")
                                }
                                OutlinedButton(
                                    onClick = {
                                        if (activeBook.pdfUrl.isNotBlank()) {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeBook.pdfUrl))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "لنک کھولنے میں مسئلہ رہا", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Browser", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("براؤزر میں پڑھیں")
                                }
                            }
                        }
                    }
                }

                pdfFile != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .transformable(state = transformableState)
                            .graphicsLayer(
                                scaleX = zoomScale,
                                scaleY = zoomScale,
                                translationX = panOffset.x,
                                translationY = panOffset.y
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("pdf_vertical_pages_list"),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                        ) {
                            items(count = totalPages, key = { index -> index }) { pageIndex ->
                                PdfPageItem(
                                    file = pdfFile!!,
                                    pageIndex = pageIndex,
                                    totalPages = totalPages,
                                    pdfManager = pdfManager,
                                    isNightMode = isNightMode
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showJumpDialog) {
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = { Text("صفحہ نمبر منتخب کریں (1-$totalPages)") },
            text = {
                OutlinedTextField(
                    value = jumpPageInput,
                    onValueChange = { jumpPageInput = it },
                    placeholder = { Text("صفحہ نمبر درج کریں...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val page = jumpPageInput.toIntOrNull()
                    if (page != null && page in 1..totalPages) {
                        currentPage = page
                        coroutineScope.launch {
                            listState.scrollToItem(page - 1)
                        }
                    }
                    showJumpDialog = false
                    jumpPageInput = ""
                }) {
                    Text("جائیں")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text("منسوخ")
                }
            }
        )
    }

    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = { Text("${activeBook.title} میں تلاش") },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        placeholder = { Text("صفحہ نمبر یا نوٹ تلاش کریں...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1 سے $totalPages کے درمیان کوئی بھی صفحہ نمبر درج کریں۔",
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
                        coroutineScope.launch {
                            listState.scrollToItem(targetPage - 1)
                        }
                    }
                    showSearchDialog = false
                    searchInput = ""
                }) {
                    Text("جائیں")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("بند کریں")
                }
            }
        )
    }

    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("مطالعہ نوٹ درج کریں (صفحہ $currentPage)") },
            text = {
                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    placeholder = { Text("اس صفحہ کے اہم نکات یا وضاحت لکھیں...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (noteInput.isNotBlank()) {
                        viewModel.saveBookmarkPage(
                            bookId = activeBook.id,
                            bookTitle = activeBook.title,
                            page = currentPage,
                            note = noteInput
                        )
                        Toast.makeText(context, "نوٹ محفوظ ہو گیا", Toast.LENGTH_SHORT).show()
                    }
                    showNoteDialog = false
                    noteInput = ""
                }) {
                    Text("محفوظ کریں")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("منسوخ")
                }
            }
        )
    }
}
