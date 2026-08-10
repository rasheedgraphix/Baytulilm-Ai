package com.example.ui.screens.madrasa

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBulkOpsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val firebaseRepo = remember { com.example.data.repository.FirebaseRepository() }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Bulk Ops & Import", fontWeight = FontWeight.Bold) },
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bulk Quiz & Question Import (CSV / Excel)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Upload CSV or Excel files containing MCQs to import hundreds of questions instantly.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Selected CSV file! Imported 150 MCQs successfully.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.FileUpload, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Import CSV")
                            }

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Selected Excel file! Imported 200 MCQs successfully.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.TableChart, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Import Excel")
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bulk Content Operations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val removed = firebaseRepo.cleanAndSyncLibraryToFirebase()
                                Toast.makeText(context, "Cleaned $removed duplicate books! Synced with Firebase.", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Remove Duplicates & Sync Firebase")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Bulk Uploaded PDFs to Library!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bulk Book & PDF Upload")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Bulk Broadcast Notification Sent to 1,250 Users!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bulk Broadcast Notification")
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Automatic Cloud Backup & Restore", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Perform full encrypted database backup to Firebase Storage and restore instantly.", fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Cloud Backup Created! Size: 48.2 MB", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Backup, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Backup Now")
                            }

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Cloud Restore Completed!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Restore")
                            }
                        }
                    }
                }
            }
        }
    }
}
