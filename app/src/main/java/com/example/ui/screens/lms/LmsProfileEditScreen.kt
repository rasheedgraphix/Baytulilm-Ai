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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LmsProfileEditScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val firebaseRepo = viewModel.firebaseRepository
    val userProfile by firebaseRepo.userProfile.collectAsStateWithLifecycle()

    var name by remember(userProfile) { mutableStateOf(userProfile.name) }
    var photoUrl by remember(userProfile) { mutableStateOf(userProfile.photo) }
    var email by remember(userProfile) { mutableStateOf(userProfile.email) }
    var phone by remember(userProfile) { mutableStateOf(userProfile.phone) }
    var language by remember(userProfile) { mutableStateOf(userProfile.language) }
    var currentDarja by remember(userProfile) { mutableStateOf(userProfile.country.ifBlank { "Darja-e-Rabia" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
                    text = "Edit Student Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Update profile details, avatar URL & Darja class level",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Preview
            AsyncImage(
                model = photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_name_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        label = { Text("Avatar Photo Image URL") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_photo_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = currentDarja,
                        onValueChange = { currentDarja = it },
                        label = { Text("Current Darja / Class Level") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_darja_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_email_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_phone_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = language,
                        onValueChange = { language = it },
                        label = { Text("Preferred Language") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val updated = userProfile.copy(
                                name = name,
                                photo = photoUrl,
                                email = email,
                                phone = phone,
                                language = language,
                                country = currentDarja
                            )
                            firebaseRepo.updateUserProfile(updated)
                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_profile_changes_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Profile Changes")
                    }
                }
            }
        }
    }
}
