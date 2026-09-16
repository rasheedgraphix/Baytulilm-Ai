package com.example.ui.screens.islamic

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FatawaVolume
import com.example.data.repository.FatawaRepository
import com.example.ui.components.GenericBookCover
import com.example.util.PdfManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FatawaDetailScreen(
    fatawaId: String,
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val fatwa = remember(fatawaId) { FatawaRepository.getFatawaList().find { it.id == fatawaId } }
    val context = LocalContext.current
    val pdfManager = remember { PdfManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = fatwa?.titleUrdu ?: "فتاویٰ و فقہ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (fatwa != null) {
                            Text(
                                text = "مصنف / مفتی: ${fatwa.authorUrdu}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("fatawa_detail_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "واپس"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { paddingValues ->
        if (fatwa != null) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Gavel,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "${fatwa.titleUrdu} (${fatwa.language})",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                if (fatwa.institutionUrdu.isNotEmpty()) {
                                    Text(
                                        text = fatwa.institutionUrdu,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Text(
                                    text = "کل ${fatwa.volumes.size} ${if (fatwa.volumes.size == 1) "جلد دستیاب ہے" else "جلدیں دستیاب ہیں"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                items(fatwa.volumes, key = { it.id }) { volume ->
                    FatawaVolumeCard(
                        volume = volume,
                        pdfManager = pdfManager,
                        onOpenVolume = {
                            onNavigate(com.example.ui.navigation.Screen.BookViewer.createRoute(volume.id))
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("فتاویٰ کا ریکارڈ دستیاب نہیں ہے۔")
            }
        }
    }
}

@Composable
fun FatawaVolumeCard(
    volume: FatawaVolume,
    pdfManager: PdfManager,
    onOpenVolume: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDownloaded by remember(volume.id) { mutableStateOf(pdfManager.isBookDownloaded(volume.id)) }
    var isDownloading by remember { mutableStateOf(false) }
    var progressFraction by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenVolume() }
            .testTag("fatawa_volume_${volume.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Volume Book Cover
            GenericBookCover(
                title = volume.titleUrdu,
                tag = "جلد",
                badgeText = "فتویٰ",
                author = "",
                themeColor = Color(0xFF6B1A25),
                width = 52.dp,
                height = 72.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Volume info on the right (RTL friendly / start)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = volume.titleUrdu,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isDownloaded) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ڈاؤن لوڈ شدہ (آف لائن دستیاب)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else if (isDownloading) {
                        Text(
                            text = "ڈاؤن لوڈ ہو رہا ہے: ${(progressFraction * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        Text(
                            text = "آن لائن مطالعہ / پی ڈی ایف",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isDownloading) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Buttons: Open & Download
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "پڑھیں" / "مطالعہ کریں" Button
                Button(
                    onClick = onOpenVolume,
                    colors = if (isDownloaded) {
                        ButtonDefaults.filledTonalButtonColors()
                    } else {
                        ButtonDefaults.buttonColors()
                    },
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("open_fatawa_btn_${volume.id}")
                ) {
                    Icon(
                        imageVector = if (isDownloaded) Icons.Default.Book else Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDownloaded) "مطالعہ کریں" else "پڑھیں",
                        fontSize = 13.sp
                    )
                }

                // Download Button (if not already downloaded)
                if (!isDownloaded) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        FilledTonalIconButton(
                            onClick = {
                                isDownloading = true
                                scope.launch {
                                    val result = pdfManager.downloadOrGetPdf(
                                        bookId = volume.id,
                                        pdfUrl = volume.pdfUrl,
                                        onProgress = { _, _, fraction ->
                                            progressFraction = fraction
                                        }
                                    )
                                    isDownloading = false
                                    if (result.isSuccess) {
                                        isDownloaded = true
                                        // Also save to device downloads folder
                                        pdfManager.copyPdfToDeviceDownloads(volume.id, volume.titleUrdu)
                                        Toast.makeText(context, "${volume.titleUrdu} کامیابی سے ڈاؤن لوڈ ہو گئی", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val errorMsg = result.exceptionOrNull()?.message ?: "نامعلوم خرابی"
                                        Toast.makeText(context, "ڈاؤن لوڈ میں خرابی: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("download_fatawa_btn_${volume.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "ڈاؤن لوڈ کریں",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
