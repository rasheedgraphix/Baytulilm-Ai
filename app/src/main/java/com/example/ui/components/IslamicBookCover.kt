package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookEntity
import com.example.util.PdfThumbnailHelper
import kotlin.math.absoluteValue

@Composable
fun IslamicBookCover(
    bookTitle: String = "",
    yearNumber: Int = 1,
    pdfUrl: String = "",
    bookId: String = "",
    width: Dp = 110.dp,
    height: Dp = 150.dp,
    cornerRadius: Dp = 12.dp,
    book: BookEntity? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val finalTitle = when {
        bookTitle.isNotBlank() -> bookTitle
        book != null -> (if (book.titleUrdu.isNotBlank()) book.titleUrdu else book.title)
        else -> ""
    }
    val finalPdfUrl = when {
        pdfUrl.isNotBlank() -> pdfUrl
        book != null -> book.pdfUrl
        else -> ""
    }
    val finalBookId = when {
        bookId.isNotBlank() -> bookId
        book != null -> book.id
        else -> ""
    }

    var bitmap by remember(finalPdfUrl) { mutableStateOf<Bitmap?>(null) }

    // Har book ke liye uska pdfUrl se first page load hoga
    LaunchedEffect(finalPdfUrl) {
        if (finalPdfUrl.isNotBlank()) {
            bitmap = PdfThumbnailHelper.getFirstPage(context, finalPdfUrl, finalBookId)
        }
    }

    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = finalTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Jab tak first page load ho raha hai, unique color ka fallback
            val colors = listOf(
                listOf(Color(0xFF0D5C33), Color(0xFF1B8A4C)),
                listOf(Color(0xFF8B4513), Color(0xFFCD853F)),
                listOf(Color(0xFF1A237E), Color(0xFF3949AB)),
                listOf(Color(0xFF4A148C), Color(0xFF7B1FA2))
            )
            val idx = finalBookId.hashCode().absoluteValue % colors.size
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors[idx])),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = finalTitle.ifBlank { "کتاب" },
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
