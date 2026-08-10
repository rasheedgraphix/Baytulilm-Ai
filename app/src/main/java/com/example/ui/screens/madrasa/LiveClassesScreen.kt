package com.example.ui.screens.madrasa

import android.content.Intent
import android.net.Uri
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
import com.example.data.model.LiveClassDoc
import com.example.data.repository.LmsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveClassesScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val liveClasses by lmsRepository.liveClasses.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Live Classes & Halaqah", fontWeight = FontWeight.Bold)
                        Text("Google Meet • Zoom • YouTube • Jitsi", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Interactive Live Halaqahs", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Attend live lectures, track attendance, and access past class recordings.", fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Text("Scheduled & Live Sessions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(liveClasses) { liveClass ->
                LiveClassCard(
                    liveClass = liveClass,
                    onJoin = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(liveClass.meetingLink))
                        context.startActivity(intent)
                    },
                    onWatchRecording = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(liveClass.recordingUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun LiveClassCard(
    liveClass: LiveClassDoc,
    onJoin: () -> Unit,
    onWatchRecording: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (liveClass.isLiveNow) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (liveClass.isLiveNow) Color(0xFFE53935) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (liveClass.isLiveNow) "• LIVE NOW" else liveClass.platform,
                        color = if (liveClass.isLiveNow) Color.White else MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Text(liveClass.scheduledTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(liveClass.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Ustadh: ${liveClass.teacherName} • ${liveClass.darja}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${liveClass.totalAttendees} Students Joined", fontSize = 12.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (liveClass.recordingUrl.isNotBlank()) {
                        OutlinedButton(
                            onClick = onWatchRecording,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Recording", fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onJoin,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (liveClass.isLiveNow) "Join Live" else "Class Link", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
