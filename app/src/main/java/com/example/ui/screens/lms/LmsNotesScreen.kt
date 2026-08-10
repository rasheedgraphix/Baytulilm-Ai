package com.example.ui.screens.lms

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LmsNotesScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val lmsRepo = viewModel.lmsRepository
    val studentNotes by lmsRepo.studentNotes.collectAsStateWithLifecycle()

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteBookTitle by remember { mutableStateOf("Al-Hidayah (Sharh Bidayat al-Mubtadi)") }
    var notePage by remember { mutableStateOf("45") }
    var highlightedText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Yellow") }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Saved Notes & Highlighting",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Create, edit, highlight kutub text & export notes to PDF",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Exporting all ${studentNotes.size} notes to PDF...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("export_notes_pdf_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export PDF", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export PDF", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            Text("Create New Study Note", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { showAddNoteDialog = !showAddNoteDialog }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Note", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        if (showAddNoteDialog) {
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = noteBookTitle,
                                onValueChange = { noteBookTitle = it },
                                label = { Text("Book Title / Kitab") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = notePage,
                                onValueChange = { notePage = it },
                                label = { Text("Page Number") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = highlightedText,
                                onValueChange = { highlightedText = it },
                                label = { Text("Highlighted Arabic / Matn Text") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = noteText,
                                onValueChange = { noteText = it },
                                label = { Text("Personal Explanation / Note") },
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (noteText.isNotBlank()) {
                                        lmsRepo.addNote(
                                            bookId = "book_custom",
                                            bookTitle = noteBookTitle,
                                            page = notePage.toIntOrNull() ?: 1,
                                            text = highlightedText,
                                            note = noteText,
                                            category = selectedCategory,
                                            tags = listOf("Personal Note", "Dars-e-Nizami")
                                        )
                                        Toast.makeText(context, "Saved Note for $noteBookTitle!", Toast.LENGTH_SHORT).show()
                                        highlightedText = ""
                                        noteText = ""
                                        showAddNoteDialog = false
                                    } else {
                                        Toast.makeText(context, "Please write a note description", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("save_note_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Save Note")
                            }
                        }
                    }
                }
            }

            item {
                Text("Saved Kutub Notes & Highlights (${studentNotes.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(studentNotes) { note ->
                val highlightBg = when (note.colorCategory) {
                    "Yellow" -> Color(0xFFFFF9C4)
                    "Green" -> Color(0xFFC8E6C9)
                    "Blue" -> Color(0xFFBBDEFB)
                    else -> Color(0xFFF8BBD0)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                Text(note.bookTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text("Page ${note.pageNumber}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            IconButton(onClick = { lmsRepo.deleteNote(note.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Note", tint = MaterialTheme.colorScheme.error)
                            }
                        }

                        if (note.highlightedText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(highlightBg, shape = RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.FormatQuote, contentDescription = "Highlight", tint = Color.DarkGray, modifier = Modifier.height(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = note.highlightedText,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = note.noteText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            note.tags.forEach { tag ->
                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
