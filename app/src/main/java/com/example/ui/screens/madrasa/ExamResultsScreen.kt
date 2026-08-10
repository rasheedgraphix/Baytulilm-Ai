package com.example.ui.screens.madrasa

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.LmsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamResultsScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val examResults by lmsRepository.examResults.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examinations & Result Cards", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(examResults) { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(result.examTerm, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = MaterialTheme.colorScheme.primary)
                                Text("${result.studentName} • ${result.darja}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF4CAF50).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = result.grade,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total Marks", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${result.obtainedMarks} / ${result.totalMarks}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Divider(modifier = Modifier.height(30.dp).width(1.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Percentage", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${result.percentage}%", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Divider(modifier = Modifier.height(30.dp).width(1.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Position", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(result.position, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFFF9800))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Subject Breakdown:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        result.subjectMarks.forEach { subject ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(subject.subjectName, fontSize = 13.sp)
                                Text("${subject.obtainedMarks} / ${subject.totalMarks}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Remarks: ${result.remarks}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val reportText = """
                                    BAYTUL ILM AI ISLAMIC SEMINARY
                                    OFFICIAL RESULT CARD
                                    Student: ${result.studentName}
                                    Darja: ${result.darja}
                                    Exam: ${result.examTerm}
                                    Total: ${result.obtainedMarks} / ${result.totalMarks} (${result.percentage}%)
                                    Grade: ${result.grade} | Position: ${result.position}
                                    Remarks: ${result.remarks}
                                """.trimIndent()

                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_SUBJECT, "Result Card - ${result.studentName}")
                                    putExtra(Intent.EXTRA_TEXT, reportText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Result Card PDF"))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Print / Share PDF Report Card")
                        }
                    }
                }
            }
        }
    }
}
