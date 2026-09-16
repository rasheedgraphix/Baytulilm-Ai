package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.PdfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PdfPageItem(
    file: File,
    pageIndex: Int,
    totalPages: Int,
    pdfManager: PdfManager,
    isNightMode: Boolean,
    modifier: Modifier = Modifier
) {
    var bitmap by remember(file, pageIndex) { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember(file, pageIndex) { mutableStateOf(true) }
    var hasError by remember(file, pageIndex) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun loadBitmap() {
        isLoading = true
        hasError = false
        coroutineScope.launch(Dispatchers.IO) {
            val bmp = pdfManager.renderPageBitmap(file, pageIndex, 1080)
            withContext(Dispatchers.Main) {
                if (bmp != null) {
                    bitmap = bmp
                    hasError = false
                } else {
                    hasError = true
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(file, pageIndex) {
        loadBitmap()
    }

    val pageNumber = pageIndex + 1
    val cardBg = if (isNightMode) Color(0xFF1E2220) else Color.White

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pdf_page_$pageNumber")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (bitmap == null) Modifier.aspectRatio(0.707f) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    val colorFilter = if (isNightMode) {
                        val nightMatrix = ColorMatrix(
                            floatArrayOf(
                                -1f, 0f, 0f, 0f, 255f,
                                0f, -1f, 0f, 0f, 255f,
                                0f, 0f, -1f, 0f, 255f,
                                0f, 0f, 0f, 1f, 0f
                            )
                        )
                        ColorFilter.colorMatrix(nightMatrix)
                    } else null

                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "صفحہ $pageNumber از $totalPages",
                        colorFilter = colorFilter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "صفحہ $pageNumber لوڈ ہو رہا ہے...",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isNightMode) Color.LightGray else Color.Gray
                        )
                    }
                } else if (hasError) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "صفحہ $pageNumber لوڈ نہیں ہو سکا",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { loadBitmap() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("دوبارہ کوشش", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Page Number indicator badge between pages
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isNightMode) Color(0xFF262E29) else Color(0xFFE6ECE8),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                text = "— صفحہ $pageNumber از $totalPages —",
                style = MaterialTheme.typography.labelSmall,
                color = if (isNightMode) Color(0xFFA8B4AC) else Color(0xFF4A554E),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
            )
        }
    }
}
