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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Translate
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
import com.example.data.model.BookDoc
import com.example.data.model.UserRole
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminShuroohTranslationsScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val booksList by firebaseRepo.booksDatabase.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0: Shurooh Management, 1: Multilingual Translations

    // Form fields for linking
    var mainBookTitle by remember { mutableStateOf("Al-Hidayah (Volume 1)") }
    var newSharhTitle by remember { mutableStateOf("Sharh Hidayah (Al-Binayah)") }
    var translationLang by remember { mutableStateOf("Urdu") }
    var translationTitle by remember { mutableStateOf("Tashil al-Hidayah Urdu") }

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
                    text = "Shurooh & Translations CMS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Link commentaries (Shurooh) & Multilingual versions (Urdu/English/Arabic)",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Shurooh Linking", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Translations Linking", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTab == 0) {
                // Shurooh Management
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Link New Sharh with Main Matn", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = mainBookTitle,
                                onValueChange = { mainBookTitle = it },
                                label = { Text("Main Matn / Book Title") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("main_matn_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newSharhTitle,
                                onValueChange = { newSharhTitle = it },
                                label = { Text("Sharh Title / Author") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("sharh_title_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val newBook = BookDoc(
                                        id = "sharh_" + System.currentTimeMillis(),
                                        title = newSharhTitle,
                                        titleArabic = newSharhTitle,
                                        author = "Sharh Scholar",
                                        description = "Commentary on $mainBookTitle",
                                        isSharh = true,
                                        isMainBook = false
                                    )
                                    firebaseRepo.addNewBook(newBook, UserRole.ADMIN)
                                    Toast.makeText(context, "Sharh linked with $mainBookTitle!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("link_sharh_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Link, contentDescription = "Link")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Link & Upload Sharh")
                            }
                        }
                    }
                }

                item {
                    Text("Existing Linked Shurooh Database", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                items(booksList.filter { it.isSharh || it.id == "b_03" }) { sharh ->
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
                                Text(sharh.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Linked Sharh | Author: ${sharh.author}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { Toast.makeText(context, "Unlinked Sharh", Toast.LENGTH_SHORT).show() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            } else {
                // Translations Management
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Link Multilingual Translation (Urdu / English / Arabic)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = mainBookTitle,
                                onValueChange = { mainBookTitle = it },
                                label = { Text("Original Book Title") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = translationLang,
                                onValueChange = { translationLang = it },
                                label = { Text("Language (Urdu / English / Arabic)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = translationTitle,
                                onValueChange = { translationTitle = it },
                                label = { Text("Translation Title") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val newBook = BookDoc(
                                        id = "trans_" + System.currentTimeMillis(),
                                        title = translationTitle,
                                        language = translationLang,
                                        isTranslation = true
                                    )
                                    firebaseRepo.addNewBook(newBook, UserRole.ADMIN)
                                    Toast.makeText(context, "Translation ($translationLang) linked!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Translate, contentDescription = "Translate")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Link Version")
                            }
                        }
                    }
                }
            }
        }
    }
}
