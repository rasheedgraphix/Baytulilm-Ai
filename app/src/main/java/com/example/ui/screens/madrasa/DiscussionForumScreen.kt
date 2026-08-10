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
fun DiscussionForumScreen(
    lmsRepository: LmsRepository,
    onNavigateBack: () -> Unit
) {
    val forumPosts by lmsRepository.forumPosts.collectAsStateWithLifecycle()
    var showNewQuestionDialog by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var questionInput by remember { mutableStateOf("") }
    var subjectInput by remember { mutableStateOf("Fiqh & Usul") }

    var activeReplyPostId by remember { mutableStateOf<String?>(null) }
    var replyInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Q&A Discussion Forum", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewQuestionDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.AddComment, contentDescription = "Ask Question")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(forumPosts) { post ->
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(post.subject, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                }
                                if (post.isPinned) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                                }
                            }

                            Text("By ${post.authorName} (${post.authorRole})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(post.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(post.question, fontSize = 13.sp, lineHeight = 18.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${post.likesCount} Likes", fontSize = 12.sp)
                            }

                            TextButton(onClick = { activeReplyPostId = if (activeReplyPostId == post.id) null else post.id }) {
                                Icon(Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reply (${post.comments.size})", fontSize = 12.sp)
                            }
                        }

                        if (post.comments.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))

                            post.comments.forEach { comment ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            if (comment.isTeacherReply) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(comment.authorName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (comment.isTeacherReply) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                                        Text(comment.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(comment.text, fontSize = 12.sp)
                                }
                            }
                        }

                        if (activeReplyPostId == post.id) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = replyInput,
                                    onValueChange = { replyInput = it },
                                    placeholder = { Text("Write your reply...") },
                                    modifier = Modifier.weight(1f),
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = {
                                        if (replyInput.isNotBlank()) {
                                            lmsRepository.addForumReply(post.id, replyInput)
                                            replyInput = ""
                                            activeReplyPostId = null
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
        }
    }

    if (showNewQuestionDialog) {
        AlertDialog(
            onDismissRequest = { showNewQuestionDialog = false },
            title = { Text("Ask Fiqh / Dars Question") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Question Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = { questionInput = it },
                        label = { Text("Detailed Explanation / Daleel query") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = subjectInput,
                        onValueChange = { subjectInput = it },
                        label = { Text("Subject (e.g. Fiqh, Nahw, Hadith)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank() && questionInput.isNotBlank()) {
                            lmsRepository.addForumPost(titleInput, questionInput, subjectInput)
                            titleInput = ""
                            questionInput = ""
                            showNewQuestionDialog = false
                        }
                    }
                ) {
                    Text("Post Question")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewQuestionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
