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
import com.example.data.model.AudioLectureDoc
import com.example.data.repository.LmsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioLecturesScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val audioLectures by lmsRepository.audioLectures.collectAsStateWithLifecycle()
    var isPlaying by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableStateOf("1.0x") }
    var sleepTimerText by remember { mutableStateOf("Off") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Daroos & Lectures", fontWeight = FontWeight.Bold) },
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Now Playing Dars", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = false,
                                    onClick = {
                                        currentSpeed = when (currentSpeed) {
                                            "1.0x" -> "1.25x"
                                            "1.25x" -> "1.5x"
                                            else -> "1.0x"
                                        }
                                    },
                                    label = { Text("Speed $currentSpeed", fontSize = 11.sp) }
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = {
                                        sleepTimerText = if (sleepTimerText == "Off") "15 min" else "Off"
                                    },
                                    label = { Text("Timer: $sleepTimerText", fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Dars 42: Kitab al-Buyu' - Valid Contracts in Al-Quduri", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Shaykh Mufti Taqi Usmani", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(12.dp))

                        Slider(
                            value = 0.35f,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("15:40", fontSize = 11.sp)

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Replay10, contentDescription = "Rewind 10s")
                                }
                                FloatingActionButton(
                                    onClick = { isPlaying = !isPlaying },
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play Pause"
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Forward10, contentDescription = "Forward 10s")
                                }
                            }

                            Text("45:20", fontSize = 11.sp)
                        }
                    }
                }
            }

            item {
                Text("Audio Daroos Playlist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(audioLectures) { audio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isPlaying = true }) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Play", tint = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(audio.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${audio.speaker} • ${audio.durationText}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row {
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark", modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = if (audio.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
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
