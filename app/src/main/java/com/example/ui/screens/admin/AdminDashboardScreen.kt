package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel

data class MetricCardData(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val containerColor: Color
)

data class CmsActionData(
    val title: String,
    val subtitle: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val adminRepo = viewModel.adminRepository
    val firebaseRepo = viewModel.firebaseRepository

    val metrics by adminRepo.metrics.collectAsStateWithLifecycle()
    val activityLogs by adminRepo.activityLogs.collectAsStateWithLifecycle()
    val booksList by firebaseRepo.booksDatabase.collectAsStateWithLifecycle()

    val statCards = listOf(
        MetricCardData("Total Users", "${metrics.totalUsers}", Icons.Default.Group, MaterialTheme.colorScheme.primaryContainer),
        MetricCardData("Active Users", "${metrics.activeUsers}", Icons.Default.People, MaterialTheme.colorScheme.secondaryContainer),
        MetricCardData("Students / Teachers", "${metrics.studentsCount} / ${metrics.teachersCount}", Icons.Default.School, MaterialTheme.colorScheme.tertiaryContainer),
        MetricCardData("Total Books & PDFs", "${metrics.totalBooks} (${metrics.totalPdfs} PDFs)", Icons.Default.MenuBook, MaterialTheme.colorScheme.primaryContainer),
        MetricCardData("Total Quizzes", "${metrics.totalQuizzes}", Icons.Default.Quiz, MaterialTheme.colorScheme.secondaryContainer),
        MetricCardData("Total Downloads", "${metrics.totalDownloads}", Icons.Default.Download, MaterialTheme.colorScheme.tertiaryContainer),
        MetricCardData("DAU / WAU / MAU", "${metrics.dailyActiveUsers} / ${metrics.weeklyActiveUsers} / ${metrics.monthlyActiveUsers}", Icons.Default.Assessment, MaterialTheme.colorScheme.primaryContainer),
        MetricCardData("Storage Used", "${metrics.storageUsedGb} GB / ${metrics.storageTotalGb} GB", Icons.Default.Storage, MaterialTheme.colorScheme.secondaryContainer)
    )

    val cmsActions = listOf(
        CmsActionData("Role & Permissions", "Manage Super Admin, Admin, Teacher, Student", Screen.AdminRoleManagement.route, Icons.Default.AdminPanelSettings),
        CmsActionData("Book & PDF CMS", "Add, edit, feature, duplicate & bulk upload", Screen.AdminBookManagement.route, Icons.Default.Book),
        CmsActionData("Shurooh & Translations", "Link Sharh & Multilingual versions", Screen.AdminShuroohTranslations.route, Icons.Default.Translate),
        CmsActionData("Quiz CMS & AI", "Create, approve, import & AI generator", Screen.AdminQuizManagement.route, Icons.Default.Quiz),
        CmsActionData("Notes, Audio & Video", "Manage lectures, MP3s & PDF notes", Screen.AdminContentManagement.route, Icons.Default.VideoLibrary),
        CmsActionData("Notifications & Alerts", "Targeted push & announcements", Screen.AdminPushNotifications.route, Icons.Default.NotificationsActive),
        CmsActionData("User Management", "Block, reset passwords, view histories", Screen.AdminUserManagement.route, Icons.Default.Person),
        CmsActionData("Analytics & Reports", "Download charts, active student rankings", Screen.AdminAnalytics.route, Icons.Default.Analytics),
        CmsActionData("Certificates CMS", "Issue, approve & verify certificates", Screen.AdminCertificates.route, Icons.Default.WorkspacePremium),
        CmsActionData("Bulk Operations & Import", "Bulk quiz, PDF upload, notifications & backup", Screen.AdminBulkOps.route, Icons.Default.CloudDownload),
        CmsActionData("Backup & App Config", "Firestore backup, maintenance mode, force update", Screen.AdminBackupSettings.route, Icons.Default.Backup),
        CmsActionData("Security & App Check", "Firestore security rules & rate limiting", Screen.AdminSecurity.route, Icons.Default.Security)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Baytul Ilm AI Admin Panel & CMS",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Full system administration, content management, analytics & security control",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Storage: ${metrics.storageUsedGb} GB of ${metrics.storageTotalGb} GB Used",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${((metrics.storageUsedGb / metrics.storageTotalGb) * 100).toInt()}%",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { metrics.storageUsedGb / metrics.storageTotalGb },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // Metrics Section Header
        item {
            Text(
                text = "System Metrics Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Metrics Grid (2 Columns)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (i in statCards.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val card1 = statCards[i]
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_metric_${card1.title.lowercase().replace(' ', '_')}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = card1.containerColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = card1.icon,
                                    contentDescription = card1.title,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(card1.title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(card1.value, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (i + 1 < statCards.size) {
                            val card2 = statCards[i + 1]
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_metric_${card2.title.lowercase().replace(' ', '_')}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = card2.containerColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = card2.icon,
                                        contentDescription = card2.title,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(card2.title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(card2.value, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // CMS Management Actions Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CMS Operational Modules",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Action Modules List
        items(cmsActions) { action ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(action.route) }
                    .testTag("cms_module_${action.route}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(action.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(action.subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Recent Activity Logs Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recent Activity Logs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(activityLogs.take(5)) { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(log.action, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${log.user} (${log.role})", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(log.timestamp, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
