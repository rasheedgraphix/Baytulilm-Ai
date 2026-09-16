package com.example.ui.screens.islamic

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.ui.components.PdfPageItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AppConfig
import com.example.util.PdfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericPdfScreen(
    title: String,
    url: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val pdfManager = remember { PdfManager(context) }
    val coroutineScope = rememberCoroutineScope()

    var pdfFile by remember { mutableStateOf<File?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    var totalPages by rememberSaveable { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDownloadedLocally by remember { mutableStateOf(false) }

    // Zoom & Pan state
    var scale by rememberSaveable { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val listState = rememberLazyListState()

    val currentVisiblePage by remember {
        derivedStateOf {
            if (totalPages > 0) {
                (listState.firstVisibleItemIndex + 1).coerceIn(1, totalPages)
            } else 1
        }
    }

    LaunchedEffect(currentVisiblePage) {
        if (!isLoading && totalPages > 0) {
            currentPage = currentVisiblePage - 1
        }
    }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 4f)
        if (scale > 1f) {
            offset += offsetChange
        } else {
            offset = Offset.Zero
        }
    }

    val bookKey = remember(title) {
        title.lowercase().replace("[^a-z0-9]".toRegex(), "_")
    }

    fun loadPdf() {
        isLoading = true
        errorMessage = null
        downloadProgress = 0f
        coroutineScope.launch(Dispatchers.IO) {
            // First check if already downloaded
            val isAlreadyLocal = pdfManager.isBookDownloaded(bookKey)
            withContext(Dispatchers.Main) {
                isDownloadedLocally = isAlreadyLocal
            }

            val result = pdfManager.downloadOrGetPdf(bookKey, url) { _, _, prog ->
                downloadProgress = if (prog >= 0f) prog else 0f
            }

            withContext(Dispatchers.Main) {
                result.onSuccess { file ->
                    pdfFile = file
                    isDownloadedLocally = true
                    coroutineScope.launch(Dispatchers.IO) {
                        val count = pdfManager.getPageCount(file)
                        withContext(Dispatchers.Main) {
                            totalPages = count
                            isLoading = false
                        }
                    }
                }.onFailure { err ->
                    isLoading = false
                    errorMessage = err.localizedMessage ?: "PDF لوڈ نہیں ہو سکی۔ براہ کرم دوبارہ کوشش کریں یا براؤزر میں کھولیں۔"
                }
            }
        }
    }

    LaunchedEffect(url) {
        loadPdf()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        if (totalPages > 0) {
                            Text(
                                text = "صفحہ ${currentPage + 1} از $totalPages",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Optional Save/Download indicator
                    if (isDownloadedLocally) {
                        IconButton(onClick = {
                            Toast.makeText(context, "یہ کتاب آف لائن محفوظ ہے", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.FileDownloadDone,
                                contentDescription = "Saved Offline",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (!isLoading) {
                        IconButton(onClick = { loadPdf() }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download"
                            )
                        }
                    }

                    // Share button
                    IconButton(onClick = {
                        AppConfig.shareAppWithWebsite(context)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Website"
                        )
                    }

                    // Open in Browser fallback
                    IconButton(onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "لنک کھولنے میں مسئلہ", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = "Open in Browser")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (!isLoading && pdfFile != null && totalPages > 0) {
                Surface(
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = {
                                if (currentPage > 0) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(currentPage - 1)
                                    }
                                }
                            },
                            enabled = currentPage > 0
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                            Spacer(Modifier.width(4.dp))
                            Text("پچھلا")
                        }

                        Text(
                            text = "${currentPage + 1} / $totalPages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        FilledTonalButton(
                            onClick = {
                                if (currentPage < totalPages - 1) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(currentPage + 1)
                                    }
                                }
                            },
                            enabled = currentPage < totalPages - 1
                        ) {
                            Text("اگلا")
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Card(
                        modifier = Modifier.padding(24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                progress = { if (downloadProgress > 0f) downloadProgress else 0.5f },
                                modifier = Modifier.size(54.dp),
                                strokeWidth = 5.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (downloadProgress > 0f) "PDF لوڈ ہو رہی ہے: ${(downloadProgress * 100).toInt()}%" else "PDF کھولی جا رہی ہے...",
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "براہ کرم چند لمحے انتظار کریں",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                errorMessage != null -> {
                    Card(
                        modifier = Modifier.padding(24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "PDF لوڈ نہیں ہو سکی",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage ?: "Connection error",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { loadPdf() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("دوبارہ کوشش")
                                }
                                Button(onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // ignore
                                    }
                                }) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("براؤزر میں کھولیں")
                                }
                            }
                        }
                    }
                }

                pdfFile != null && totalPages > 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .transformable(state = transformableState)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                        ) {
                            items(count = totalPages, key = { index -> index }) { pageIndex ->
                                PdfPageItem(
                                    file = pdfFile!!,
                                    pageIndex = pageIndex,
                                    totalPages = totalPages,
                                    pdfManager = pdfManager,
                                    isNightMode = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
