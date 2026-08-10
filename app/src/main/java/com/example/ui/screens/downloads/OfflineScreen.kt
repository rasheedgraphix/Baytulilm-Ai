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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BookEntity
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
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
                            text = if (isUrdu) "آف لائن لائبریری" else "Offline Library",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isUrdu) "انٹرنیٹ کے بغیر ڈاؤن لوڈ کردہ کتب، پی ڈی ایف، نوٹس اور کوئز پیک پڑھیں" else "Read downloaded Kutub, PDFs, notes & quiz packs without internet",
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
                        text = "Storage Used: ${String.format("%.1f", downloadedBooks.size * 28.5f)} MB",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "Device Free: 34.8 GB",
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
                Tab(
                    selected = selected,
                    onClick = { selectedCategory = category }
                ) {
                    Text(
                        text = category,
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
                    Text(
                        text = "${selectedItemIds.size} items selected",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Row {
                        TextButton(onClick = {
                            selectedItemIds.forEach { viewModel.deleteDownload(it) }
                            snackbarMessage = "Deleted ${selectedItemIds.size} downloaded items"
                            selectedItemIds.clear()
                            isMultiSelectMode = false
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete Selected", color = Color.Red, fontSize = 12.sp)
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

                    Text(
                        text = "No Offline Content Yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Download Kutub, PDFs, Notes and Quiz Packs from the Library to access them anytime without internet connection.",
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
                        Text("Go to Library", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Downloaded Items (${downloadedBooks.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(onClick = {
                            downloadedBooks.forEach { viewModel.deleteDownload(it.id) }
                            snackbarMessage = "Cleared all offline downloads"
                        }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete All", fontSize = 11.sp, color = Color.Red)
                        }
                    }
                }

                items(downloadedBooks, key = { it.id }) { book ->
                    val isSelected = selectedItemIds.contains(book.id)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
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
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isMultiSelectMode) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            if (it) selectedItemIds.add(book.id)
                                            else selectedItemIds.remove(book.id)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = book.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFF2E7D32).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color(0xFF2E7D32),
                                                    modifier = Modifier.size(10.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "Downloaded",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF2E7D32)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "${book.darja} • ${book.subject} • ${book.pageCount} Pages",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Details Metadata Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Size: 28.5 MB • Date: 2026-07-25",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Location: Internal App Storage / Offline / ${book.id}.pdf",
                                        fontSize = 9.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ChromeReaderMode,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Read Offline", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteDownload(book.id)
                                            snackbarMessage = "Removed ${book.title} from offline storage"
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red.copy(alpha = 0.8f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
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
