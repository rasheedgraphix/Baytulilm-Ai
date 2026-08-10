package com.example.ui.screens.madrasa

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.VideoCourseDoc
import com.example.data.repository.LmsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCoursesScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val courses by lmsRepository.videoCourses.collectAsStateWithLifecycle()
    var activeCourse by remember { mutableStateOf<VideoCourseDoc?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Courses & Lessons", fontWeight = FontWeight.Bold) },
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Continue Watching", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Mastery in Nahw Mir & Kafiyah • Lesson 2: Rules of Fa'il", fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.65f },
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            item {
                Text("Enrolled Video Subjects", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(courses) { course ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(course.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Teacher: ${course.teacherName} • ${course.darja}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(course.description, fontSize = 13.sp, lineHeight = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Lessons in Course (${course.lessons.size}):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        course.lessons.forEach { lesson ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = if (lesson.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        tint = if (lesson.isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(lesson.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("${lesson.durationMinutes} mins", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(onClick = { }, modifier = Modifier.size(28.dp)) {
                                        Icon(
                                            imageVector = if (lesson.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                            contentDescription = "Download",
                                            modifier = Modifier.size(18.dp)
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
}
