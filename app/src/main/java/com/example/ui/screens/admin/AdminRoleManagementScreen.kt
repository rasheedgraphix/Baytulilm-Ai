package com.example.ui.screens.admin

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

data class RolePermissionInfo(
    val role: String,
    val permissions: List<String>,
    val badgeColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRoleManagementScreen(
    viewModel: MainViewModel
) {
    val adminRepo = viewModel.adminRepository
    val usersList by adminRepo.usersList.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf("All") }

    val rolePermissions = listOf(
        RolePermissionInfo("Super Admin", listOf("Full Access to System", "Manage Roles & Admins", "Database Backup & Restore", "System Security Rules"), Color(0xFFD32F2F)),
        RolePermissionInfo("Admin", listOf("Manage Content & Books", "Manage Users & Status", "Send Push Notifications", "Manage Announcements"), Color(0xFF1976D2)),
        RolePermissionInfo("Teacher", listOf("Upload PDF Notes", "Upload & Manage Quizzes", "Upload Assignments", "View Student Results"), Color(0xFF388E3C)),
        RolePermissionInfo("Student", listOf("Read & View Kutub", "Download PDFs", "Attempt Quizzes", "View Certificates"), Color(0xFFF57C00))
    )

    val filteredUsers = usersList.filter { u ->
        (selectedRoleFilter == "All" || u.role.equals(selectedRoleFilter, ignoreCase = true)) &&
                (searchQuery.isBlank() || u.name.contains(searchQuery, ignoreCase = true) || u.email.contains(searchQuery, ignoreCase = true))
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
                    text = "Role & Access Control Management",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Configure roles (Super Admin, Admin, Teacher, Student) & permissions",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Role Permission Hierarchy Section
            item {
                Text("Role Permission Matrix", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(rolePermissions) { info ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                shape = RoundedCornerShape(6.dp),
                                colors = CardDefaults.cardColors(containerColor = info.badgeColor)
                            ) {
                                Text(
                                    text = info.role,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        info.permissions.forEach { perm ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Check",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(perm, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // User Role Assignment Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("User Role Assignments", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search User by Name or Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_user_role_input"),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            items(filteredUsers) { user ->
                var expandedRoleMenu by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_role_card_${user.uid}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(user.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Box {
                            Button(
                                onClick = { expandedRoleMenu = true },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("change_role_btn_${user.uid}")
                            ) {
                                Text(user.role, fontSize = 12.sp)
                                Icon(Icons.Default.Edit, contentDescription = "Edit Role", modifier = Modifier.padding(start = 4.dp))
                            }

                            DropdownMenu(
                                expanded = expandedRoleMenu,
                                onDismissRequest = { expandedRoleMenu = false }
                            ) {
                                listOf(UserRole.ADMIN, UserRole.TEACHER, UserRole.STUDENT).forEach { roleOption ->
                                    DropdownMenuItem(
                                        text = { Text(roleOption) },
                                        onClick = {
                                            adminRepo.updateUserRole(user.uid, roleOption)
                                            expandedRoleMenu = false
                                        }
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
