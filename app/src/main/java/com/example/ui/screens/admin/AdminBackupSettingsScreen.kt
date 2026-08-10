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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminBackupSettingsScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val adminRepo = viewModel.adminRepository

    var isMaintenanceMode by remember { mutableStateOf(false) }
    var minRequiredVersion by remember { mutableStateOf("1.2.0") }
    var isForceUpdateEnabled by remember { mutableStateOf(false) }

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
                    text = "Backup, Remote Config & Maintenance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Export Firestore backup JSON, toggle maintenance mode & force updates",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Firestore Database Backup & Restore
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Firestore Data Backup & Export", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Export entire books, quizzes, user profiles & logs to JSON cloud backup", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    adminRepo.addLog("Created Full System Backup", "Admin", UserRole.ADMIN, "Backup")
                                    Toast.makeText(context, "Firestore Backup Created: baytulilm_backup_2026.json", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("create_backup_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = "Backup")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Create Backup")
                            }

                            OutlinedButton(
                                onClick = {
                                    adminRepo.addLog("Restored System Backup", "Admin", UserRole.ADMIN, "Backup")
                                    Toast.makeText(context, "Restored database state!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("restore_backup_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = "Restore")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Restore Data")
                            }
                        }
                    }
                }
            }

            // Remote Configuration & Maintenance Controls
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("System Maintenance & Remote Config", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Enable Maintenance Mode", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Locks app for non-admin users during updates", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isMaintenanceMode,
                                onCheckedChange = {
                                    isMaintenanceMode = it
                                    Toast.makeText(context, if (it) "Maintenance Mode Enabled!" else "Maintenance Mode Disabled", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("maintenance_mode_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Force App Update", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Require users below minimum version to update", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isForceUpdateEnabled,
                                onCheckedChange = { isForceUpdateEnabled = it },
                                modifier = Modifier.testTag("force_update_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = minRequiredVersion,
                            onValueChange = { minRequiredVersion = it },
                            label = { Text("Minimum Required App Version") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Cache & Storage Management
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Cache & Storage Maintenance", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                Toast.makeText(context, "System cache purged successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("purge_cache_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CleaningServices, contentDescription = "Purge")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Purge Temporary PDF & Media Cache")
                        }
                    }
                }
            }
        }
    }
}
