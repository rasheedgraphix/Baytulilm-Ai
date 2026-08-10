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
import com.example.data.repository.LmsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessagingScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val messages by lmsRepository.directMessages.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("Mufti Muhammad Taqi") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Teacher & Admin Messaging", fontWeight = FontWeight.Bold)
                        Text("Chatting with: $recipientName", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg ->
                    val isMe = msg.isMeSender
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp,
                                bottomStart = if (isMe) 14.dp else 2.dp,
                                bottomEnd = if (isMe) 2.dp else 14.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isMe) "You" else "${msg.senderName} (${msg.senderRole})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(msg.messageText, fontSize = 13.sp, lineHeight = 18.sp)

                                if (msg.mediaType != "None" && msg.mediaUrl.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (msg.mediaType == "PDF") Icons.Default.PictureAsPdf else Icons.Default.Mic,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Attachment: ${msg.mediaUrl}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    msg.timestamp,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { lmsRepository.sendMessage(recipientName, "Sent PDF Attachment", "PDF", "parsing_notes.pdf") }) {
                        Icon(Icons.Default.AttachFile, contentDescription = "Attach File")
                    }
                    IconButton(onClick = { lmsRepository.sendMessage(recipientName, "Voice Message (0:15)", "Voice", "voice_note_1.mp3") }) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Note")
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Write message to teacher...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                lmsRepository.sendMessage(recipientName, inputText)
                                inputText = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
