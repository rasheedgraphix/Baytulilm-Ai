package com.example.ui.screens.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage
import com.example.util.lStr

@Composable
fun BookmarksScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val appLanguage = LocalAppLanguage.current
    val langCode = appLanguage.code
    val isRtl = appLanguage.isRtl
    val bookmarkedBooks by viewModel.bookmarkedBooks.collectAsStateWithLifecycle()
    val pageBookmarks by viewModel.allBookmarks.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = lStr("bookmarks_and_notes"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                val subtitle = when (langCode) {
                    "ps" -> "نښه شوو پاڼو او زدوکړې یادښتونو ته چټک لاسرسی"
                    "ur" -> "محفوظ شدہ صفحات اور تعلیمی نوٹس تک فوری رسائی"
                    else -> "Quick access to your saved pages and study notes"
                }
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        if (bookmarkedBooks.isEmpty() && pageBookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                val emptyText = when (langCode) {
                    "ps" -> "تر اوسه هیڅ بک مارک نه دی خوندي شوی. د کتاب لوستلو پر مهال د بک مارک په آئیکن کلیک وکړئ."
                    "ur" -> "ابھی تک کوئی بک مارک محفوظ نہیں ہوا۔ کتاب پڑھتے وقت بک مارک آئیکن پر ٹیپ کریں"
                    else -> "No bookmarks saved yet. Tap the bookmark icon while reading a book to save it here."
                }
                Text(
                    text = emptyText,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (pageBookmarks.isNotEmpty()) {
                    item {
                        val headerText = when (langCode) {
                            "ps" -> "د پاڼې بک مارکونه او درسي یادښتونه"
                            "ur" -> "صفحہ کے بک مارکس اور تعلیمی نوٹس"
                            else -> "Page Bookmarks & Study Notes"
                        }
                        Text(
                            text = headerText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(pageBookmarks) { bm ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigate(Screen.BookViewer.createRoute(bm.bookId)) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = lStr("bookmarks"),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = bm.bookTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    val noteText = when (langCode) {
                                        "ps" -> "پاڼه ${bm.pageNumber} • یادښت: ${bm.note}"
                                        "ur" -> "صفحہ ${bm.pageNumber} • نوٹ: ${bm.note}"
                                        else -> "Page ${bm.pageNumber} • Note: ${bm.note}"
                                    }
                                    Text(
                                        text = noteText,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
