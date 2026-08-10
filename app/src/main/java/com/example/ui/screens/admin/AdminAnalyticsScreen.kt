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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

data class TopBookStat(val title: String, val downloads: Int, val progress: Float)
data class StudentLeaderboardStat(val name: String, val darja: String, val score: Int, val quizzesCompleted: Int)

@Composable
fun AdminAnalyticsScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val adminRepo = viewModel.adminRepository
    val metrics by adminRepo.metrics.collectAsStateWithLifecycle()

    val topBooks = listOf(
        TopBookStat("Al-Hidayah Vol 1", 3420, 0.95f),
        TopBookStat("Sharh Ibn 'Aqil", 2890, 0.82f),
        TopBookStat("Kafiyah Ibn al-Hajib", 2150, 0.65f),
        TopBookStat("Al-Kuduri", 1890, 0.55f),
        TopBookStat("Mishkat al-Masabih", 1420, 0.42f)
    )

    val topStudents = listOf(
        StudentLeaderboardStat("Abdullah Student", "Darja-e-Rabia", 980, 24),
        StudentLeaderboardStat("Tariq Jameel", "Darja-e-Sania", 940, 21),
        StudentLeaderboardStat("Usman Ghani", "Darja-e-Khamsa", 890, 19),
        StudentLeaderboardStat("Ahmad Raza", "Darja-e-Ula", 850, 18)
    )

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
                    text = "System Analytics & Performance Reports",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "DAU/MAU trends, top downloaded books, quiz completion rates & exports",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("User Activity Trends", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Exporting Analytics PDF/CSV...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("export_analytics_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export PDF Report")
                    }
                }
            }

            // Summary Metrics Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Daily Active (DAU)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${metrics.dailyActiveUsers}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Column {
                                Text("Weekly Active (WAU)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${metrics.weeklyActiveUsers}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            }
                            Column {
                                Text("Monthly Active (MAU)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${metrics.monthlyActiveUsers}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                }
            }

            item {
                Text("Most Downloaded Kutub", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(topBooks) { bookStat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(bookStat.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("${bookStat.downloads} downloads", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { bookStat.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Top Performing Students Leaderboard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(topStudents) { student ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(student.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("${student.darja} | ${student.quizzesCompleted} Quizzes Attempted", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${student.score} Pts", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
