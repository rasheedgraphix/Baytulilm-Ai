package com.example.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.BookCard
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage
import com.example.util.lStr

@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val appLanguage = LocalAppLanguage.current
    val langCode = appLanguage.code
    val isRtl = appLanguage.isRtl
    val favoriteBooks by viewModel.favoriteBooks.collectAsStateWithLifecycle()

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
                    text = lStr("my_favorites"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                val subtitle = when (langCode) {
                    "ps" -> "د ټولو درجاتو ستاسو خوښ شوي کتابونه"
                    "ur" -> "تمام درجات کی پسندیدہ کتب"
                    else -> "Your starred books across all Darjat"
                }
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        if (favoriteBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                val emptyText = when (langCode) {
                    "ps" -> "تر اوسه هیڅ خوښ شوی کتاب نه دی اضافه شوی. د کتاب اضافه کولو لپاره د زړه ایښودل شوي آئیکن باندې کلیک وکړئ."
                    "ur" -> "ابھی تک کوئی پسندیدہ کتاب شامل نہیں کی گئی۔ کسی بھی کتاب پر دل کے آئیکن پر ٹیپ کریں"
                    else -> "No favorite books added yet. Tap the heart icon on any book to add it to your favorites."
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
                items(favoriteBooks, key = { it.id }) { book ->
                    BookCard(
                        book = book,
                        onLoadThumbnail = { viewModel.getThumbnail(it) },
                        onReadClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                        onDetailClick = { onNavigate(Screen.BookDetail.createRoute(book.id)) },
                        onFavoriteToggle = { viewModel.toggleFavorite(book) },
                        onBookmarkToggle = { viewModel.toggleBookmark(book) },
                        onDownloadClick = { viewModel.downloadBook(book) },
                        onNavigate = onNavigate
                    )
                }
            }
        }
    }
}
