package com.example.ui.screens.lms

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StudentDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val lmsRepo = viewModel.lmsRepository

    val userProfile by firebaseRepo.userProfile.collectAsStateWithLifecycle()
    val studentProgress by lmsRepo.studentProgress.collectAsStateWithLifecycle()
    val recentReadings by viewModel.recentReadings.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Welcome Header & Profile Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = userProfile.photo,
                            contentDescription = "Student Profile Picture",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Assalamu Alaikum,",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                ) {
                                    Text(
                                        text = "${studentProgress.learningStreak}d Streak 🔥",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = userProfile.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Darja: ${userProfile.country.ifBlank { "Darja-e-Rabia" }} • Aalim Student",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    IconButton(
                        onClick = { onNavigate(Screen.LmsProfileEdit.route) },
                        modifier = Modifier.testTag("edit_student_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Reading Highlight Card
        if (recentReadings.isNotEmpty()) {
            val lastBook = recentReadings.first()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigate(Screen.BookViewer.createRoute(lastBook.bookId)) }
                    .testTag("continue_reading_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = "Continue Reading",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Continue Reading",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = "Last Opened Today",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = lastBook.bookTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "Author: ${lastBook.author} • Page ${lastBook.pageNumber} of ${lastBook.totalPages}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val progressRatio = (lastBook.pageNumber.toFloat() / lastBook.totalPages.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.White.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onNavigate(Screen.BookViewer.createRoute(lastBook.bookId)) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Resume")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resume Reading", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // LMS Core Navigation Grid
        SectionHeader("Digital Madrasa Portal")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Live Classes",
                subtitle = "Google Meet & Zoom",
                icon = Icons.Default.School,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LiveClasses.route)
            }

            LmsGridCard(
                title = "Video Courses",
                subtitle = "Lectures & Lessons",
                icon = Icons.Default.PlayArrow,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.VideoCourses.route)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Audio Daroos",
                subtitle = "Streaming & Offline",
                icon = Icons.Default.AutoStories,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.AudioLectures.route)
            }

            LmsGridCard(
                title = "Exam Results",
                subtitle = "Grades & Report Cards",
                icon = Icons.Default.Stars,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.ExamResults.route)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Q&A Forum",
                subtitle = "Questions & Discussions",
                icon = Icons.Default.FormatQuote,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.DiscussionForum.route)
            }

            LmsGridCard(
                title = "Messages",
                subtitle = "Teacher & Admin Chat",
                icon = Icons.Default.Edit,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.DirectMessaging.route)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Book Page OCR",
                subtitle = "Extract Text & Notes",
                icon = Icons.Default.Note,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.OcrAssistant.route)
            }

            LmsGridCard(
                title = "Parent Panel",
                subtitle = "Guardian Progress View",
                icon = Icons.Default.WorkspacePremium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.ParentDashboard.route)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("LMS Study Center")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Study Planner",
                subtitle = "Schedules & Reminders",
                icon = Icons.Default.CalendarMonth,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsStudyPlanner.route)
            }

            LmsGridCard(
                title = "Assignments",
                subtitle = "Tasks & Submissions",
                icon = Icons.Default.Assignment,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsAssignments.route)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Saved Notes",
                subtitle = "Highlights & Notes",
                icon = Icons.Default.Note,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsNotes.route)
            }

            LmsGridCard(
                title = "Certificates",
                subtitle = "Diplomas & Diplomas",
                icon = Icons.Default.WorkspacePremium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsCertificates.route)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LmsGridCard(
                title = "Achievements",
                subtitle = "Badges & Milestones",
                icon = Icons.Default.EmojiEvents,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsAchievements.route)
            }

            LmsGridCard(
                title = "Leaderboard",
                subtitle = "Ranks & Top Readers",
                icon = Icons.Default.Leaderboard,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.LmsLeaderboard.route)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Learning Progress & Goals Dashboard
        SectionHeader("Learning Progress & Milestones")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Metric Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressStatBox("Books Read", "${studentProgress.booksRead}", MaterialTheme.colorScheme.primary)
                    ProgressStatBox("Pages Read", "${studentProgress.pagesRead}", MaterialTheme.colorScheme.secondary)
                    ProgressStatBox("Study Hours", "${studentProgress.readingTimeHours}h", MaterialTheme.colorScheme.tertiary)
                    ProgressStatBox("Avg Score", "${studentProgress.averageScore}%", MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Goals Tracker
                Text("Study Goals Tracker", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                GoalProgressRow(
                    label = "Daily Page Goal",
                    currentText = "${studentProgress.currentDailyPages} / ${studentProgress.dailyGoalPages} pages",
                    progress = (studentProgress.currentDailyPages.toFloat() / studentProgress.dailyGoalPages.toFloat()).coerceIn(0f, 1f),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                GoalProgressRow(
                    label = "Weekly Hours Goal",
                    currentText = "${studentProgress.currentWeeklyHours} / ${studentProgress.weeklyGoalHours} hours",
                    progress = (studentProgress.currentWeeklyHours / studentProgress.weeklyGoalHours).coerceIn(0f, 1f),
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                GoalProgressRow(
                    label = "Monthly Goal Progress",
                    currentText = "${studentProgress.currentMonthlyHours} / ${studentProgress.monthlyGoalHours} hours",
                    progress = (studentProgress.currentMonthlyHours / studentProgress.monthlyGoalHours).coerceIn(0f, 1f),
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weekly Chart Visualizer
                Text("Weekly Study Hours Breakdown", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val maxHour = (studentProgress.weeklyStudyHours.maxOrNull() ?: 3.0f).coerceAtLeast(1f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    daysOfWeek.forEachIndexed { idx, day ->
                        val hours = studentProgress.weeklyStudyHours.getOrElse(idx) { 0f }
                        val heightFraction = (hours / maxHour).coerceIn(0.1f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${hours}h", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height((60 * heightFraction).dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(if (idx == 5) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(day, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Access Lists (Downloaded, Favorites, Bookmarks)
        SectionHeader("Quick Access Library")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickLibraryChip("Downloaded Kutub", Icons.Default.Download, MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                onNavigate(Screen.Downloads.route)
            }
            QuickLibraryChip("Favorite Kutub", Icons.Default.Favorite, MaterialTheme.colorScheme.secondary, Modifier.weight(1f)) {
                onNavigate(Screen.Favorites.route)
            }
            QuickLibraryChip("Bookmarked Pages", Icons.Default.Bookmark, MaterialTheme.colorScheme.tertiary, Modifier.weight(1f)) {
                onNavigate(Screen.Bookmarks.route)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Learning Hub (Quiz, Hadith, Ayah, Prayer)
        SectionHeader("Today's Learning Hub")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onNavigate(Screen.Quiz.route) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = "Today's Quiz",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Today's Dars-e-Nizami Quiz", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Attempt Fiqh & Nahw Quiz to boost your leaderboard score!", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                }
                Button(
                    onClick = { onNavigate(Screen.Quiz.route) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Start", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun LmsGridCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(95.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
                }
            }

            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ProgressStatBox(
    title: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun GoalProgressRow(
    label: String,
    currentText: String,
    progress: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(currentText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}

@Composable
private fun QuickLibraryChip(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
