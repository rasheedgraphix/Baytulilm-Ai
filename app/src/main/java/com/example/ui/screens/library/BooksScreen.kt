package com.example.ui.screens.library

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BookEntity
import com.example.ui.components.BookCard
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage
import com.example.util.lStr

enum class SortOption(val key: String) {
    TITLE_ASC("title_asc"),
    TITLE_DESC("title_desc"),
    POPULAR("most_popular"),
    PAGES("page_count");

    @Composable
    fun getDisplayName(): String {
        return when (this) {
            TITLE_ASC -> "${lStr("books")}: A-Z"
            TITLE_DESC -> "${lStr("books")}: Z-A"
            POPULAR -> lStr("featured_books")
            PAGES -> lStr("curriculum_progress")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    darjaId: String,
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val darjatList by viewModel.firebaseRepository.darjatListStream.collectAsStateWithLifecycle()
    val lang = LocalAppLanguage.current
    val isUrdu = lang.code == "ur" || lang.isRtl

    // Find Darja meta
    val darjaItem = darjatList.find { 
        it.id.equals(darjaId, ignoreCase = true) || 
        it.name.equals(darjaId, ignoreCase = true)
    }
    val darjaTitle = if (darjaItem != null) darjaItem.getDisplayName(isUrdu) else darjaId.ifBlank { lStr("books") }

    var searchQuery by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("All") }
    var isGridView by remember { mutableStateOf(false) }
    var favoriteOnly by remember { mutableStateOf(false) }
    var downloadedOnly by remember { mutableStateOf(false) }
    var currentSortOption by remember { mutableStateOf(SortOption.TITLE_ASC) }
    var showSortMenu by remember { mutableStateOf(false) }

    val subjectsList = listOf(
        "All", "Nahw", "Sarf", "Fiqh", "Usul-ul-Fiqh", "Hadith",
        "Tafseer", "Balagha", "Mantiq", "Aqeedah", "Arabic Literature", "Insha"
    )

    // Filter books matching current Darja, search, subject, favorites, and downloads
    val filteredBooks = allBooks.filter { book ->
        val matchesDarja = book.darja.contains(darjaTitle, ignoreCase = true) || 
                           darjaTitle.contains(book.darja, ignoreCase = true) ||
                           book.darja.contains(darjaId, ignoreCase = true) ||
                           ((darjaId.contains("6", ignoreCase = true) || darjaTitle.contains("6", ignoreCase = true) || darjaId.contains("sadisa", ignoreCase = true)) &&
                            (book.darja.contains("6", ignoreCase = true) || book.darja.contains("Sadisa", ignoreCase = true))) ||
                           (darjaItem != null && (
                               book.darja.contains(darjaItem.id, ignoreCase = true) ||
                               book.darja.contains(darjaItem.name, ignoreCase = true) ||
                               book.darja.contains(darjaItem.urduName, ignoreCase = true)
                           ))
        val matchesSearch = searchQuery.isBlank() ||
                book.title.contains(searchQuery, ignoreCase = true) ||
                book.author.contains(searchQuery, ignoreCase = true) ||
                book.subject.contains(searchQuery, ignoreCase = true)

        val matchesSubject = selectedSubject == "All" || book.subject.equals(selectedSubject, ignoreCase = true)
        val matchesFav = !favoriteOnly || book.isFavorite
        val matchesDownloaded = !downloadedOnly || book.isDownloaded

        matchesDarja && matchesSearch && matchesSubject && matchesFav && matchesDownloaded
    }.let { list ->
        when (currentSortOption) {
            SortOption.TITLE_ASC -> list.sortedBy { it.title }
            SortOption.TITLE_DESC -> list.sortedByDescending { it.title }
            SortOption.POPULAR -> list.sortedByDescending { it.rating }
            SortOption.PAGES -> list.sortedByDescending { it.pageCount }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = darjaTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (darjaItem != null && darjaItem.arabicName.isNotBlank()) {
                            Text(
                                text = "${darjaItem.getDisplayName(!isUrdu)} • ${darjaItem.arabicName}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = lStr("back"),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Bar & Grid/List Controls Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("${lStr("search")} $darjaTitle...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = lStr("search"),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = lStr("cancel"),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }

            // Subject Filters Horizontal Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjectsList) { subject ->
                    val displaySubject = if (subject == "All") lStr("all") else subject
                    FilterChip(
                        selected = selectedSubject == subject,
                        onClick = { selectedSubject = subject },
                        label = { Text(text = displaySubject, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Controls Toolbar (Grid/List Toggle, Sorting, Favorite Filter, Downloaded Filter)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Favorite Filter
                    FilterChip(
                        selected = favoriteOnly,
                        onClick = { favoriteOnly = !favoriteOnly },
                        label = { Text(lStr("favorites"), fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = if (favoriteOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.error
                        )
                    )

                    // Downloaded Filter
                    FilterChip(
                        selected = downloadedOnly,
                        onClick = { downloadedOnly = !downloadedOnly },
                        label = { Text(lStr("downloads"), fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DownloadDone,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Sort Menu Button
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sorting",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.getDisplayName(), fontSize = 13.sp) },
                                    onClick = {
                                        currentSortOption = option
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Grid / List View Toggle Button
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.List else Icons.Default.GridView,
                            contentDescription = "Toggle Grid/List View",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Books Content Container
            if (filteredBooks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = lStr("no_books_found"),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isUrdu) "$darjaTitle میں فی الحال کوئی کتابیں موجود نہیں ہیں۔" else "There are currently no books in $darjaTitle. Books, Shurooh, and PDF materials will appear here once added.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            } else {
                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredBooks, key = { it.id }) { book ->
                            BookCard(
                                book = book,
                                onReadClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                                onDetailClick = { onNavigate(Screen.BookDetail.createRoute(book.id)) },
                                onFavoriteToggle = { viewModel.toggleFavorite(book) },
                                onBookmarkToggle = { viewModel.toggleBookmark(book) },
                                onDownloadClick = { viewModel.downloadBook(book) },
                                onShareClick = { },
                                onAiChatClick = { onNavigate(Screen.AiAssistant.route) },
                                onAiQuizClick = { onNavigate(Screen.Quiz.route) },
                                onAiNotesClick = { onNavigate(Screen.LmsNotes.route) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredBooks, key = { it.id }) { book ->
                            BookCard(
                                book = book,
                                onReadClick = { onNavigate(Screen.BookViewer.createRoute(book.id)) },
                                onDetailClick = { onNavigate(Screen.BookDetail.createRoute(book.id)) },
                                onFavoriteToggle = { viewModel.toggleFavorite(book) },
                                onBookmarkToggle = { viewModel.toggleBookmark(book) },
                                onDownloadClick = { viewModel.downloadBook(book) },
                                onShareClick = { },
                                onAiChatClick = { onNavigate(Screen.AiAssistant.route) },
                                onAiQuizClick = { onNavigate(Screen.Quiz.route) },
                                onAiNotesClick = { onNavigate(Screen.LmsNotes.route) }
                            )
                        }
                    }
                }
            }
        }
    }
}
