package com.example.ui.screens.lms

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LmsStudyPlannerScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val lmsRepo = viewModel.lmsRepository
    val studyPlans by lmsRepo.studyPlans.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf("Daily") } // Daily, Weekly, Monthly
    var showAddDialog by remember { mutableStateOf(false) }

    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newSubject by remember { mutableStateOf("Nahw") }
    var newTimeSlot by remember { mutableStateOf("08:00 AM - 09:30 AM") }

    val filterPlans = studyPlans.filter { it.scheduleType == selectedTab }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Study Planner & Time Schedule",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Organize daily kutub study, weekly revision & study reminders",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Schedule Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Daily", "Weekly", "Monthly").forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text("$tab Schedule", fontSize = 12.sp) },
                            modifier = Modifier.testTag("planner_tab_$tab")
                        )
                    }
                }
            }

            // Create New Study Plan Toggle
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Add Study Task", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { showAddDialog = !showAddDialog }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        if (showAddDialog) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = newTitle,
                                onValueChange = { newTitle = it },
                                label = { Text("Task Title") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("planner_task_title_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newDesc,
                                onValueChange = { newDesc = it },
                                label = { Text("Description / Specific Pages") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("planner_task_desc_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newSubject,
                                    onValueChange = { newSubject = it },
                                    label = { Text("Subject (e.g. Fiqh)") },
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = newTimeSlot,
                                    onValueChange = { newTimeSlot = it },
                                    label = { Text("Time Slot") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (newTitle.isNotBlank()) {
                                        lmsRepo.addStudyPlan(
                                            title = newTitle,
                                            desc = newDesc,
                                            subject = newSubject,
                                            scheduleType = selectedTab,
                                            timeSlot = newTimeSlot
                                        )
                                        Toast.makeText(context, "Added $newTitle to $selectedTab Schedule!", Toast.LENGTH_SHORT).show()
                                        newTitle = ""
                                        newDesc = ""
                                        showAddDialog = false
                                    } else {
                                        Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("save_planner_task_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Save to $selectedTab Schedule")
                            }
                        }
                    }
                }
            }

            item {
                Text("$selectedTab Tasks & Reminders (${filterPlans.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            items(filterPlans) { plan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (plan.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { lmsRepo.toggleStudyPlan(plan.id) }) {
                            Icon(
                                imageVector = if (plan.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Toggle Complete",
                                tint = if (plan.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = plan.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (plan.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                            )
                            Text(plan.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Alarm, contentDescription = "Time", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.height(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${plan.timeSlot} | Subject: ${plan.subjectName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        IconButton(onClick = { lmsRepo.deleteStudyPlan(plan.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Plan", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
