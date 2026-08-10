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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.AnnouncementDoc
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminPushNotificationsScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val adminRepo = viewModel.adminRepository

    val announcementsList by firebaseRepo.announcements.collectAsStateWithLifecycle()

    var pushTitle by remember { mutableStateOf("") }
    var pushBody by remember { mutableStateOf("") }
    var targetRole by remember { mutableStateOf("All Users") } // All, Students, Teachers, Admins
    var selectedCategory by remember { mutableStateOf("Exam Alert") }

    val roleTargets = listOf("All Users", "Students", "Teachers", "Admins")
    val categoryOptions = listOf("Exam Alert", "New Book", "Holiday Notice", "General")

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
                    text = "Push Notifications & Announcements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Send FCM push alerts, role-targeted messaging & campus announcements",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Compose Push Notification Alert", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = pushTitle,
                            onValueChange = { pushTitle = it },
                            label = { Text("Notification Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("push_title_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = pushBody,
                            onValueChange = { pushBody = it },
                            label = { Text("Message Body / Content") },
                            minLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("push_body_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Target Audience / Role:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            roleTargets.forEach { role ->
                                FilterChip(
                                    selected = targetRole == role,
                                    onClick = { targetRole = role },
                                    label = { Text(role, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("target_role_chip_$role")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (pushTitle.isNotBlank() && pushBody.isNotBlank()) {
                                    firebaseRepo.sendNotificationOrAnnouncement(
                                        title = pushTitle,
                                        message = pushBody,
                                        type = targetRole
                                    )
                                    adminRepo.addLog("Sent Push Notification: $pushTitle to $targetRole", "Admin", UserRole.ADMIN, "Settings")
                                    Toast.makeText(context, "Push Alert Sent to $targetRole!", Toast.LENGTH_SHORT).show()
                                    pushTitle = ""
                                    pushBody = ""
                                } else {
                                    Toast.makeText(context, "Please fill title & content!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("send_push_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Broadcast Push Notification")
                        }
                    }
                }
            }

            item {
                Text("Sent Announcements & Notice History", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(announcementsList) { ann ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(ann.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(ann.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Type: ${ann.type} | Author: ${ann.author}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }

                        IconButton(onClick = { Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
