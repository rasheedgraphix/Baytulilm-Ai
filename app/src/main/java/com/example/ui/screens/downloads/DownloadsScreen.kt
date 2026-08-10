package com.example.ui.screens.downloads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BookEntity
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel

data class ActiveDownloadItem(
    val id: String,
    val title: String,
    val totalSizeMb: Float,
    var downloadedMb: Float,
    var status: String, // "Downloading", "Paused", "Queued", "Failed"
    var speedKbps: Int,
    val type: String = "Full PDF"
)

@Composable
fun DownloadsScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val downloadedBooks by viewModel.downloadedBooks.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) } // 0: Active, 1: Offline Library, 2: Storage Manager, 3: Settings, 4: History

    // State for simulated active downloads & settings
    var wifiOnly by remember { mutableStateOf(true) }
    var allowMobileData by remember { mutableStateOf(false) }
    var downloadChargingOnly by remember { mutableStateOf(false) }
    var autoResumeReconnect by remember { mutableStateOf(true) }

    var cacheClearedToast by remember { mutableStateOf(false) }

    // Mock active downloads state
    val activeDownloads = remember {
        mutableStateListOf(
            ActiveDownloadItem("d1", "Sahih al-Bukhari (Vol 1)", 45.2f, 28.4f, "Downloading", 1250, "Full PDF"),
            ActiveDownloadItem("d2", "Al-Hidayah Sharh Bidayat al-Mubtadi", 62.0f, 12.1f, "Paused", 0, "Chapters 1-5"),
            ActiveDownloadItem("d3", "Sharh Ibn 'Aqil (Nahw)", 18.5f, 0f, "Queued", 0, "Quiz Pack & Notes")
        )
    }

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
            Column {
                Text(
                    text = "Professional Download Manager",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Manage offline Kutub, PDFs, notes, storage & background downloads",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 12.dp
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                    Icon(Icons.Default.Downloading, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Active (${activeDownloads.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                    Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Offline Library (${downloadedBooks.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                    Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Storage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rules", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("History", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        when (selectedTab) {
            0 -> ActiveDownloadsView(
                downloads = activeDownloads,
                onPause = { item -> item.status = "Paused"; item.speedKbps = 0 },
                onResume = { item -> item.status = "Downloading"; item.speedKbps = 1450 },
                onCancel = { item -> activeDownloads.remove(item) }
            )
            1 -> OfflineLibraryView(downloadedBooks = downloadedBooks, onNavigate = onNavigate, onDelete = { viewModel.deleteDownload(it) })
            2 -> StorageManagerView(
                downloadedCount = downloadedBooks.size,
                onClearCache = { cacheClearedToast = true },
                onDeleteAll = { downloadedBooks.forEach { viewModel.deleteDownload(it.id) } }
            )
            3 -> DownloadSettingsView(
                wifiOnly = wifiOnly,
                onWifiChange = { wifiOnly = it },
                allowMobileData = allowMobileData,
                onMobileChange = { allowMobileData = it },
                downloadChargingOnly = downloadChargingOnly,
                onChargingChange = { downloadChargingOnly = it },
                autoResume = autoResumeReconnect,
                onAutoResumeChange = { autoResumeReconnect = it }
            )
            4 -> DownloadHistoryView()
        }
    }
}

@Composable
private fun ActiveDownloadsView(
    downloads: List<ActiveDownloadItem>,
    onPause: (ActiveDownloadItem) -> Unit,
    onResume: (ActiveDownloadItem) -> Unit,
    onCancel: (ActiveDownloadItem) -> Unit
) {
    if (downloads.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("No active or queued downloads", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("All selected books and packs are fully downloaded", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(downloads, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Type: ${item.type} • Status: ${item.status}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Row {
                                if (item.status == "Downloading") {
                                    IconButton(onClick = { onPause(item) }) {
                                        Icon(Icons.Default.Pause, contentDescription = "Pause", tint = MaterialTheme.colorScheme.primary)
                                    }
                                } else if (item.status == "Paused") {
                                    IconButton(onClick = { onResume(item) }) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                IconButton(onClick = { onCancel(item) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.Red)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val progress = (item.downloadedMb / item.totalSizeMb).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("${String.format("%.1f", item.downloadedMb)} MB / ${String.format("%.1f", item.totalSizeMb)} MB (${(progress * 100).toInt()}%)", fontSize = 11.sp)
                            if (item.status == "Downloading") {
                                Text("${item.speedKbps} KB/s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            } else {
                                Text(item.status, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfflineLibraryView(
    downloadedBooks: List<BookEntity>,
    onNavigate: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    if (downloadedBooks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.DownloadDone, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Offline Library Empty", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Download books from the Library to read offline without internet.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(downloadedBooks, key = { it.id }) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(book.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${book.darja} • ${book.subject} • ${book.pageCount} Pages", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Ready Offline (PDF & Notes Cached)", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Open", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = { onDelete(book.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StorageManagerView(
    downloadedCount: Int,
    onClearCache: () -> Unit,
    onDeleteAll: () -> Unit
) {
    val totalSizeMb = downloadedCount * 28.5f
    val cacheSizeMb = 14.2f

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💾 Device Storage Overview", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("App Download Storage", fontSize = 12.sp)
                        Text("${String.format("%.1f", totalSizeMb)} MB", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(progress = { (totalSizeMb / 1024f).coerceIn(0.05f, 1f) }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)))

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Temporary PDF & Image Cache", fontSize = 12.sp)
                        Text("${cacheSizeMb} MB", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(progress = { (cacheSizeMb / 100f).coerceIn(0.1f, 1f) }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚡ Quick Storage Actions", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onClearCache,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear Temporary Cache (14.2 MB)")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onDeleteAll,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Delete All Offline Downloads")
                    }
                }
            }
        }
    }
}

@Composable
private fun DownloadSettingsView(
    wifiOnly: Boolean,
    onWifiChange: (Boolean) -> Unit,
    allowMobileData: Boolean,
    onMobileChange: (Boolean) -> Unit,
    downloadChargingOnly: Boolean,
    onChargingChange: (Boolean) -> Unit,
    autoResume: Boolean,
    onAutoResumeChange: (Boolean) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🌐 Network & Auto-Download Rules", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Download Only on Wi-Fi", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Prevents downloading large PDFs on mobile cellular data", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = wifiOnly, onCheckedChange = onWifiChange)
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Allow Mobile Data for Small Files", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Allows downloading notes, quizzes and small attachments", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = allowMobileData, onCheckedChange = onMobileChange)
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Download Only While Charging", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Saves battery life during heavy multi-book downloads", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = downloadChargingOnly, onCheckedChange = onChargingChange)
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto Resume Downloads", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Automatically retry interrupted downloads upon connection", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = autoResume, onCheckedChange = onAutoResumeChange)
                    }
                }
            }
        }
    }
}

@Composable
private fun DownloadHistoryView() {
    val historyItems = remember {
        listOf(
            Triple("Sahih Muslim (Vol 1)", "Completed • 38.4 MB", "2026-07-24"),
            Triple("Sunan Abi Dawud", "Completed • 42.1 MB", "2026-07-22"),
            Triple("Al-Fiqh al-Akbar", "Completed • 5.2 MB", "2026-07-20"),
            Triple("Sharh Mulla Jami", "Failed (Interrupted)", "2026-07-18")
        )
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(historyItems) { (title, subtitle, date) ->
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (subtitle.contains("Failed")) Icons.Default.Error else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (subtitle.contains("Failed")) Color.Red else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(date, fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

