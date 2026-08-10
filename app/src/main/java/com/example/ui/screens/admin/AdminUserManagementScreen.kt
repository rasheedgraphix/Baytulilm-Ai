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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun AdminUserManagementScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val adminRepo = viewModel.adminRepository
    val usersList by adminRepo.usersList.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = usersList.filter { u ->
        searchQuery.isBlank() || u.name.contains(searchQuery, ignoreCase = true) || u.email.contains(searchQuery, ignoreCase = true)
    }

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
                    text = "User Directory & Account Control",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Block/Unblock, Password Resets, Role Updates & Reading Histories",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by student name, email...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_user_search_input"),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            items(filteredUsers) { user ->
                val isBlocked = user.country == "BLOCKED"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_user_card_${user.uid}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isBlocked) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(user.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Phone: ${user.phone} | Region: ${user.country}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }

                            Card(
                                shape = RoundedCornerShape(6.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isBlocked) Color.Red else MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = if (isBlocked) "BLOCKED" else user.role,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Password reset email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.LockReset, contentDescription = "Reset")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Pass", fontSize = 11.sp)
                            }

                            Row {
                                IconButton(onClick = {
                                    adminRepo.toggleBlockUser(user.uid)
                                    Toast.makeText(context, if (isBlocked) "Unblocked user" else "Blocked user", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = if (isBlocked) Icons.Default.Check else Icons.Default.Block,
                                        contentDescription = "Block Toggle",
                                        tint = if (isBlocked) Color.Green else Color.Red
                                    )
                                }

                                IconButton(onClick = {
                                    adminRepo.deleteUser(user.uid)
                                    Toast.makeText(context, "Deleted user account", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
