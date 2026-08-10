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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.repository.AudioLectureDoc
import com.example.data.repository.NoteDoc
import com.example.data.repository.VideoLectureDoc
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminContentManagementScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val adminRepo = viewModel.adminRepository

    val notesList by adminRepo.notesList.collectAsStateWithLifecycle()
    val videoList by adminRepo.videoList.collectAsStateWithLifecycle()
    val audioList by adminRepo.audioList.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0: Notes, 1: Videos, 2: Audios

    // Form inputs for Notes
    var noteTitle by remember { mutableStateOf("") }
    var noteSubject by remember { mutableStateOf("Nahw") }
    var noteDarja by remember { mutableStateOf("Darja-e-Ula") }

    // Form inputs for Video
    var videoTitle by remember { mutableStateOf("") }
    var videoCategory by remember { mutableStateOf("YouTube Link") }
    var videoTeacher by remember { mutableStateOf("Mufti Usmani") }

    // Form inputs for Audio
    var audioTitle by remember { mutableStateOf("") }
    var audioType by remember { mutableStateOf("Lecture Audio") }

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
                    text = "Notes, Video & Audio Content CMS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Upload & manage PDF notes, YouTube/Firebase video lectures & MP3 audios",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Notes (PDF/Img)", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Videos", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Audios (MP3)", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // Notes CMS
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Upload New Note (PDF / Image)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = noteTitle,
                                    onValueChange = { noteTitle = it },
                                    label = { Text("Note Title") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("note_title_input")
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = noteSubject,
                                        onValueChange = { noteSubject = it },
                                        label = { Text("Subject") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = noteDarja,
                                        onValueChange = { noteDarja = it },
                                        label = { Text("Darja") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (noteTitle.isNotBlank()) {
                                            adminRepo.addNote(
                                                NoteDoc(
                                                    id = "n_" + System.currentTimeMillis(),
                                                    title = noteTitle,
                                                    subject = noteSubject,
                                                    darja = noteDarja,
                                                    author = "Admin Faculty"
                                                )
                                            )
                                            Toast.makeText(context, "Note uploaded successfully!", Toast.LENGTH_SHORT).show()
                                            noteTitle = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("upload_note_btn"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Upload, contentDescription = "Upload")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload PDF/Image Note")
                                }
                            }
                        }
                    }

                    item {
                        Text("Uploaded Notes Library (${notesList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    items(notesList) { note ->
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
                                    Text(note.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("${note.subject} | ${note.darja} | Type: ${note.fileType}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                }

                                IconButton(onClick = { adminRepo.deleteNote(note.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Video CMS
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Add Video Lecture (YouTube / Firebase Video)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = videoTitle,
                                    onValueChange = { videoTitle = it },
                                    label = { Text("Lecture Title") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("video_title_input")
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = videoCategory,
                                        onValueChange = { videoCategory = it },
                                        label = { Text("Source (YouTube/Firebase)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = videoTeacher,
                                        onValueChange = { videoTeacher = it },
                                        label = { Text("Teacher Name") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (videoTitle.isNotBlank()) {
                                            adminRepo.addVideo(
                                                VideoLectureDoc(
                                                    id = "v_" + System.currentTimeMillis(),
                                                    title = videoTitle,
                                                    category = videoCategory,
                                                    teacherName = videoTeacher
                                                )
                                            )
                                            Toast.makeText(context, "Video lecture published!", Toast.LENGTH_SHORT).show()
                                            videoTitle = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("publish_video_btn"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.VideoLibrary, contentDescription = "Add Video")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Add Video Lecture")
                                }
                            }
                        }
                    }

                    item {
                        Text("Published Video Lectures (${videoList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    items(videoList) { vid ->
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
                                    Text(vid.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("Source: ${vid.category} | Teacher: ${vid.teacherName} | Duration: ${vid.durationMinutes}m", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                }

                                IconButton(onClick = { adminRepo.deleteVideo(vid.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Audio CMS
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Upload MP3 Audio (Quran, Hadith, Lecture)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = audioTitle,
                                    onValueChange = { audioTitle = it },
                                    label = { Text("Audio Title") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("audio_title_input")
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = audioType,
                                    onValueChange = { audioType = it },
                                    label = { Text("Category (Quran / Hadith / Lecture)") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (audioTitle.isNotBlank()) {
                                            adminRepo.addAudio(
                                                AudioLectureDoc(
                                                    id = "a_" + System.currentTimeMillis(),
                                                    title = audioTitle,
                                                    type = audioType
                                                )
                                            )
                                            Toast.makeText(context, "Audio track uploaded!", Toast.LENGTH_SHORT).show()
                                            audioTitle = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("upload_audio_btn"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Audiotrack, contentDescription = "Upload Audio")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload MP3 Track")
                                }
                            }
                        }
                    }

                    item {
                        Text("Audio Library (${audioList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    items(audioList) { aud ->
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
                                    Text(aud.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("Type: ${aud.type} | Duration: ${aud.duration} | Size: ${aud.sizeMb}MB", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                }

                                IconButton(onClick = { adminRepo.deleteAudio(aud.id) }) {
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
