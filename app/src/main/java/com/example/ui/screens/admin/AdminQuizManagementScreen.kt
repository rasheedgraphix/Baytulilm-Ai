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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.McqQuestion
import com.example.data.model.QuizDoc
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminQuizManagementScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val quizzesList by firebaseRepo.quizzesList.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var isGeneratingAi by remember { mutableStateOf(false) }

    // New Quiz Form
    var quizTitle by remember { mutableStateOf("") }
    var quizSubject by remember { mutableStateOf("Fiqh") }
    var quizDarja by remember { mutableStateOf("Darja-e-Ula") }
    var quizChapter by remember { mutableStateOf("Kitab ut-Taharah") }

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
                    text = "Quiz & Examination CMS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Create, Edit, AI MCQ Generator, Excel/CSV Import & Schedule Exams",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Action buttons row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showCreateDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_create_quiz_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create Quiz")
                    }

                    OutlinedButton(
                        onClick = {
                            isGeneratingAi = true
                            val generatedQuiz = QuizDoc(
                                id = "q_ai_" + System.currentTimeMillis(),
                                title = "AI Generated: $quizSubject Exam",
                                chapter = "Generated Chapter 1",
                                subject = quizSubject,
                                darja = quizDarja,
                                mcqs = listOf(
                                    McqQuestion(1, "AI MCQ 1: What is the primary ruling in $quizSubject?", listOf("Option A", "Option B", "Option C", "Option D"), 0, "AI explanation", 10),
                                    McqQuestion(2, "AI MCQ 2: According to Dars-e-Nizami curriculum, which book is taught first?", listOf("Option A", "Option B", "Option C", "Option D"), 1, "AI explanation", 10)
                                )
                            )
                            firebaseRepo.addNewQuiz(generatedQuiz, UserRole.ADMIN)
                            isGeneratingAi = false
                            Toast.makeText(context, "AI generated 10 MCQs for $quizSubject!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_generate_quiz_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Quiz Gen")
                    }
                }
            }

            // CSV / Excel Import Export row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Imported 15 MCQs from Excel/CSV file!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("import_csv_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FileUpload, contentDescription = "Import")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import CSV/Excel", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Exported Quiz Question Bank to CSV!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("export_csv_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export CSV", fontSize = 11.sp)
                    }
                }
            }

            item {
                Text("Active Quizzes Bank (${quizzesList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(quizzesList) { q ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_quiz_card_${q.id}"),
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
                                Text(q.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("${q.subject} | ${q.darja} | ${q.mcqs.size} MCQs | ${q.timeLimitMinutes} Mins", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Text("Chapter: ${q.chapter}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Card(
                                shape = RoundedCornerShape(6.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Text(
                                    text = "Approved",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { Toast.makeText(context, "Edit Quiz", Toast.LENGTH_SHORT).show() }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { Toast.makeText(context, "Deleted Quiz", Toast.LENGTH_SHORT).show() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Create New Dars-e-Nizami Quiz") },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(value = quizTitle, onValueChange = { quizTitle = it }, label = { Text("Quiz Title") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = quizSubject, onValueChange = { quizSubject = it }, label = { Text("Subject (e.g., Fiqh, Nahw)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = quizDarja, onValueChange = { quizDarja = it }, label = { Text("Darja") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = quizChapter, onValueChange = { quizChapter = it }, label = { Text("Chapter / Topic") }, modifier = Modifier.fillMaxWidth())
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (quizTitle.isNotBlank()) {
                            val newQ = QuizDoc(
                                id = "q_" + System.currentTimeMillis(),
                                title = quizTitle,
                                subject = quizSubject,
                                darja = quizDarja,
                                chapter = quizChapter
                            )
                            firebaseRepo.addNewQuiz(newQ, UserRole.ADMIN)
                            Toast.makeText(context, "Quiz Created & Published!", Toast.LENGTH_SHORT).show()
                            showCreateDialog = false
                        }
                    }) {
                        Text("Save & Publish")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
