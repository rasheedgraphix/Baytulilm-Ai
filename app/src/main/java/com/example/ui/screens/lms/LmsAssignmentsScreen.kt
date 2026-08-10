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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Grading
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
fun LmsAssignmentsScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val lmsRepo = viewModel.lmsRepository
    val assignments by lmsRepo.assignments.collectAsStateWithLifecycle()
    val submissions by lmsRepo.submissions.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf("All") } // All, Homework, Reading Task, Assignment
    var selectedAssignmentForSubmit by remember { mutableStateOf<String?>(null) }

    var responseText by remember { mutableStateOf("") }
    var selectedFileType by remember { mutableStateOf("PDF") }
    var attachedFileName by remember { mutableStateOf("my_solution_irab.pdf") }

    val filterAssignments = assignments.filter {
        selectedFilter == "All" || it.assignmentType == selectedFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Assignments, Homework & Submissions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Teacher task assignments, PDF/Image submission uploads & grade tracking",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Task Type Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Homework", "Reading Task", "Assignment").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 11.sp) },
                            modifier = Modifier.testTag("assignment_filter_$filter")
                        )
                    }
                }
            }

            // Submissions & Status Tracking Header
            if (submissions.isNotEmpty()) {
                item {
                    Text("Your Submissions & Teacher Feedback", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                items(submissions) { sub ->
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
                                Text(sub.assignmentTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (sub.status == "Graded") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Text(
                                        text = sub.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Attached: ${sub.fileUrl} (${sub.fileType})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Response: ${sub.textResponse}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            if (sub.status == "Graded") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Teacher Feedback: ${sub.teacherFeedback}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Score Awarded: ${sub.grade}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("Assigned Tasks (${filterAssignments.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(filterAssignments) { assign ->
                val isSubmitOpen = selectedAssignmentForSubmit == assign.id

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
                            Text(assign.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Card(
                                shape = RoundedCornerShape(6.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                            ) {
                                Text(
                                    text = assign.assignmentType,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(assign.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Teacher: ${assign.teacherName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            Text("Due: ${assign.dueDate}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                selectedAssignmentForSubmit = if (isSubmitOpen) null else assign.id
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("submit_assignment_btn_${assign.id}"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = "Submit")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isSubmitOpen) "Close Submission Form" else "Submit Assignment / Solution")
                        }

                        if (isSubmitOpen) {
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = responseText,
                                onValueChange = { responseText = it },
                                label = { Text("Write Submission Notes / Answers") },
                                minLines = 2,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submission_notes_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    selected = selectedFileType == "PDF",
                                    onClick = {
                                        selectedFileType = "PDF"
                                        attachedFileName = "solution_file.pdf"
                                    },
                                    label = { Text("PDF File") }
                                )

                                FilterChip(
                                    selected = selectedFileType == "Image",
                                    onClick = {
                                        selectedFileType = "Image"
                                        attachedFileName = "handwritten_notes.jpg"
                                    },
                                    label = { Text("Image Upload") }
                                )

                                Text("Attached: $attachedFileName", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    lmsRepo.submitAssignment(
                                        assignmentId = assign.id,
                                        assignmentTitle = assign.title,
                                        textResponse = responseText.ifBlank { "Attached submission file." },
                                        fileType = selectedFileType,
                                        fileName = attachedFileName
                                    )
                                    Toast.makeText(context, "Assignment submitted successfully!", Toast.LENGTH_SHORT).show()
                                    responseText = ""
                                    selectedAssignmentForSubmit = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("final_submit_assignment_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Finalize & Submit Task")
                            }
                        }
                    }
                }
            }
        }
    }
}
