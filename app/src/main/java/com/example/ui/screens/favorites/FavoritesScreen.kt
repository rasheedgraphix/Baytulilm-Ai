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

@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val isUrdu = LocalAppLanguage.current.code == "ur"
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
                    text = if (isUrdu) "پسندیدہ کتب و درجات" else "Favorite Books & Kutub",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isUrdu) "تمام درجات کی پسندیدہ کتب" else "Your starred books across all Darjat",
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
                Text(
                    text = if (isUrdu) "ابھی تک کوئی پسندیدہ کتاب شامل نہیں کی گئی۔ کسی بھی کتاب پر دل کے آئیکن پر ٹیپ کریں" else "No favorite books added yet. Tap the heart icon on any book to add it to your favorites.",
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
                        onReadClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                        onDetailClick = { onNavigate(Screen.BookDetail.createRoute(book.id)) },
                        onFavoriteToggle = { viewModel.toggleFavorite(book) },
                        onBookmarkToggle = { viewModel.toggleBookmark(book) },
                        onDownloadClick = { viewModel.downloadBook(book) },
                        onShareClick = { /* Share */ }
                    )
                }
            }
        }
    }
}
