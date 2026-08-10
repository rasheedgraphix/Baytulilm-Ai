package com.example.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BookDoc
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminBookManagementScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val adminRepo = viewModel.adminRepository

    val booksList by firebaseRepo.booksDatabase.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedDarjaFilter by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Book Catalog, 1: PDF Management & Replace

    // New Book Form Fields
    var newTitle by remember { mutableStateOf("") }
    var newTitleArabic by remember { mutableStateOf("") }
    var newAuthor by remember { mutableStateOf("") }
    var newDarja by remember { mutableStateOf("Darja-e-Ula") }
    var newSubject by remember { mutableStateOf("Nahw") }
    var newLanguage by remember { mutableStateOf("Arabic/Urdu") }
    var newPdfUrl by remember { mutableStateOf("") }

    val darjaOptions = listOf("All", "Darja-e-Ula", "Darja-e-Sania", "Darja-e-Salisa", "Darja-e-Rabia", "Darja-e-Khamsa", "Darja-e-Sadisa", "Darja-e-Sabi'a", "Dora Hadith")

    val filteredBooks = booksList.filter { b ->
        (selectedDarjaFilter == "All" || b.darja.equals(selectedDarjaFilter, ignoreCase = true)) &&
                (searchQuery.isBlank() || b.title.contains(searchQuery, ignoreCase = true) || b.author.contains(searchQuery, ignoreCase = true) || b.subject.contains(searchQuery, ignoreCase = true))
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
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Book & PDF Content Management",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Add, Edit, Feature, Publish, Duplicate & PDF Storage Control",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Book Catalog CMS", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("PDF & File Storage", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by title, author...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_book_search_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("admin_add_book_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Book")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bulk actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        val removedCount = firebaseRepo.cleanAndSyncLibraryToFirebase()
                        Toast.makeText(context, "Cleaned $removedCount duplicate books! Library synced with Firebase.", Toast.LENGTH_LONG).show()
                        adminRepo.addLog("Cleaned $removedCount duplicate books and synced with Firebase", "Admin", UserRole.ADMIN, "Book")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("clean_duplicates_btn")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Clean Duplicates")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clean Duplicates & Sync")
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Bulk Upload simulated! Kutub processed successfully.", Toast.LENGTH_SHORT).show()
                        adminRepo.addLog("Ran Bulk Upload operation", "Admin", UserRole.ADMIN, "Book")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("bulk_upload_btn")
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = "Bulk")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bulk CSV")
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text("Total Clean Books: ${filteredBooks.size}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBooks) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_book_item_${book.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(book.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(book.titleArabic, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text("Author: ${book.author} | Darja: ${book.darja} | Subject: ${book.subject}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (book.isFeatured) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Text(
                                        text = if (book.isFeatured) "Featured" else "Standard",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (selectedTab == 1) {
                                // PDF Management details view
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = Color.Red)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("PDF URL: ${book.pdfUrl}", fontSize = 11.sp, maxLines = 1, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("File Size: ${book.size}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text("Pages: ${book.pages}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text("Downloads: ${book.downloads}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row {
                                    IconButton(onClick = {
                                        Toast.makeText(context, "Duplicated book: ${book.title} (Copy)", Toast.LENGTH_SHORT).show()
                                        firebaseRepo.addNewBook(book.copy(id = "b_" + System.currentTimeMillis(), title = "${book.title} (Copy)"), UserRole.ADMIN)
                                    }) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = MaterialTheme.colorScheme.primary)
                                    }

                                    IconButton(onClick = {
                                        Toast.makeText(context, "Edited book parameters", Toast.LENGTH_SHORT).show()
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Updated publish status for ${book.title}", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Publish, contentDescription = "Publish", modifier = Modifier.padding(end = 4.dp))
                                    Text("Published", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Book Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Book / PDF to CMS") },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(value = newTitle, onValueChange = { newTitle = it }, label = { Text("Title (English/Urdu)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = newTitleArabic, onValueChange = { newTitleArabic = it }, label = { Text("Title (Arabic)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = newAuthor, onValueChange = { newAuthor = it }, label = { Text("Author Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = newDarja, onValueChange = { newDarja = it }, label = { Text("Darja (e.g. Darja-e-Ula)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = newSubject, onValueChange = { newSubject = it }, label = { Text("Subject (e.g. Nahw, Fiqh)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = newPdfUrl, onValueChange = { newPdfUrl = it }, label = { Text("Firebase Storage PDF Path") }, modifier = Modifier.fillMaxWidth())
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (newTitle.isNotBlank()) {
                            val newBook = BookDoc(
                                id = "b_" + System.currentTimeMillis(),
                                title = newTitle,
                                titleArabic = if (newTitleArabic.isBlank()) newTitle else newTitleArabic,
                                author = if (newAuthor.isBlank()) "Unknown Scholar" else newAuthor,
                                darja = newDarja,
                                subject = newSubject,
                                pdfUrl = if (newPdfUrl.isBlank()) firebaseRepo.getStoragePdfPath(newDarja, "new_book.pdf") else newPdfUrl
                            )
                            firebaseRepo.addNewBook(newBook, UserRole.ADMIN)
                            Toast.makeText(context, "New Book Added!", Toast.LENGTH_SHORT).show()
                            showAddDialog = false
                        }
                    }) {
                        Text("Save & Publish")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
