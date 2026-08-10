package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.components.ThemePickerDialog
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LanguageManager
import com.example.util.LocalAppLanguage

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    val favorites by viewModel.favoriteBooks.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarkedBooks.collectAsStateWithLifecycle()
    val downloads by viewModel.downloadedBooks.collectAsStateWithLifecycle()
    val recentReadings by viewModel.recentReadings.collectAsStateWithLifecycle()
    val userProfile by authViewModel.userState.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    val currentLang by LanguageManager.currentLanguage.collectAsState()
    val currentTheme by LanguageManager.currentTheme.collectAsState()
    val isUrdu = LocalAppLanguage.current.code == "ur"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // User Profile Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_1784865359662),
                        contentDescription = if (isUrdu) "صارف کی تصویر" else "User Avatar",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userProfile.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = userProfile.email,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                if (userProfile.city.isNotEmpty()) {
                    Text(
                        text = "${userProfile.city}, ${userProfile.province} (${userProfile.country})",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Role Chip Badge & Verification Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val roleDisplay = if (isUrdu) {
                        when (userProfile.role) {
                            UserRole.STUDENT -> "طالب علم"
                            UserRole.TEACHER -> "مدرس / استاد"
                            UserRole.ADMIN -> "ایڈمن"
                            UserRole.SUPER_ADMIN -> "سپر ایڈمن"
                            else -> userProfile.role
                        }
                    } else userProfile.role

                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = if (isUrdu) "حیثیت: $roleDisplay" else "Role: ${userProfile.role}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    if (userProfile.emailVerified) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(if (isUrdu) "تصدیق شدہ" else "Verified", fontSize = 11.sp, color = Color(0xFF2E7D32)) },
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFE8F5E9))
                        )
                    } else {
                        SuggestionChip(
                            onClick = { authViewModel.sendEmailVerification() },
                            label = { Text(if (isUrdu) "ای میل تصدیق کریں" else "Verify Email", fontSize = 11.sp, color = Color(0xFFC62828)) },
                            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFC62828), modifier = Modifier.size(14.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFFFEBEE))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Stats Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileStatCard(number = "${favorites.size}", title = if (isUrdu) "پسندیدہ" else "Favorites", modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${bookmarks.size}", title = if (isUrdu) "بک مارکس" else "Bookmarks", modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${downloads.size}", title = if (isUrdu) "ڈاؤن لوڈ شدہ" else "Downloaded", modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${recentReadings.size}", title = if (isUrdu) "مطالعہ" else "Readings", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Options List
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ProfileOptionRow(
                    if (isUrdu) "پروفائل کی تفصیلات میں ترمیم" else "Edit Profile Details",
                    Icons.Default.Edit,
                    isUrdu = isUrdu
                ) {
                    showEditDialog = true
                }
                ProfileOptionRow(
                    if (isUrdu) "ایپ کی زبان (${currentLang.flagEmoji} ${currentLang.nativeName})" else "App Language (${currentLang.flagEmoji} ${currentLang.nativeName})",
                    Icons.Default.Language,
                    isUrdu = isUrdu
                ) {
                    showLanguagePicker = true
                }

                val themeText = if (isUrdu) {
                    when (currentTheme) {
                        "Light" -> "روشن"
                        "Dark" -> "تاریک"
                        else -> "سسٹم"
                    }
                } else currentTheme

                ProfileOptionRow(
                    if (isUrdu) "تھیم کی ترتیبات ($themeText)" else "Theme Settings ($currentTheme)",
                    Icons.Default.ColorLens,
                    isUrdu = isUrdu
                ) {
                    showThemePicker = true
                }
                ProfileOptionRow(
                    if (isUrdu) "پاس ورڈ تبدیل کریں" else "Change Password",
                    Icons.Default.LockReset,
                    isUrdu = isUrdu
                ) {
                    showPasswordDialog = true
                }
                ProfileOptionRow(
                    if (isUrdu) "میرے بک مارکس" else "My Bookmarks",
                    Icons.Default.Bookmark,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.Bookmarks.route)
                }
                ProfileOptionRow(
                    if (isUrdu) "پسندیدہ کتب" else "Favorite Kutub",
                    Icons.Default.Favorite,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.Favorites.route)
                }
                ProfileOptionRow(
                    if (isUrdu) "مطالعے کی تاریخ" else "Reading History",
                    Icons.Default.History,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.Recent.route)
                }
                ProfileOptionRow(
                    if (isUrdu) "آف لائن ڈاؤن لوڈز" else "Offline Downloads",
                    Icons.Default.Download,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.Downloads.route)
                }
                ProfileOptionRow(
                    if (isUrdu) "ایپ کی ترتیبات" else "App Settings",
                    Icons.Default.Settings,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.Settings.route)
                }
                ProfileOptionRow(
                    if (isUrdu) "تعارف و رابطہ" else "About & Contact",
                    Icons.Default.Info,
                    isUrdu = isUrdu
                ) {
                    onNavigate(Screen.About.route)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Auth Action Button (Logout / Delete)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        authViewModel.logout()
                        onNavigate(Screen.Login.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isUrdu) "اکاؤنٹ سے سائن آؤٹ کریں" else "Sign Out Account",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                OutlinedButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (isUrdu) "اکاؤنٹ اور ڈیٹا حذف کریں" else "Delete Account & Data",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        var editName by remember { mutableStateOf(userProfile.name) }
        var editPhone by remember { mutableStateOf(userProfile.phone) }
        var editCountry by remember { mutableStateOf(userProfile.country) }
        var editProvince by remember { mutableStateOf(userProfile.province) }
        var editCity by remember { mutableStateOf(userProfile.city) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(if (isUrdu) "پروفائل کی تفصیلات میں ترمیم" else "Edit Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text(if (isUrdu) "مکمل نام" else "Full Name") })
                    OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text(if (isUrdu) "موبائل نمبر" else "Mobile Number") })
                    OutlinedTextField(value = editCountry, onValueChange = { editCountry = it }, label = { Text(if (isUrdu) "ملک" else "Country") })
                    OutlinedTextField(value = editProvince, onValueChange = { editProvince = it }, label = { Text(if (isUrdu) "صوبہ" else "Province") })
                    OutlinedTextField(value = editCity, onValueChange = { editCity = it }, label = { Text(if (isUrdu) "شہر" else "City") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    authViewModel.updateProfile(
                        name = editName,
                        phone = editPhone,
                        country = editCountry,
                        province = editProvince,
                        city = editCity,
                        language = userProfile.language,
                        theme = userProfile.theme
                    )
                    showEditDialog = false
                }) {
                    Text(if (isUrdu) "تبدیلیاں محفوظ کریں" else "Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text(if (isUrdu) "منسوخ کریں" else "Cancel") }
            }
        )
    }

    // Change Password Dialog
    if (showPasswordDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text(if (isUrdu) "پاس ورڈ تبدیل کریں" else "Change Password") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text(if (isUrdu) "موجودہ پاس ورڈ" else "Current Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text(if (isUrdu) "نیا پاس ورڈ (کم از کم 6 حروف)" else "New Password (min 6 chars)") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    authViewModel.changePassword(oldPass, newPass)
                    showPasswordDialog = false
                }) {
                    Text(if (isUrdu) "پاس ورڈ اپ ڈیٹ کریں" else "Update Password")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text(if (isUrdu) "منسوخ کریں" else "Cancel") }
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(if (isUrdu) "اکاؤنٹ حذف کریں؟" else "Delete Account?") },
            text = {
                Text(
                    if (isUrdu) "کیا آپ واقعی اپنا اکاؤنٹ حذف کرنا چاہتے ہیں؟ یہ عمل واپس نہیں ہو سکتا اور آپ کے محفوظ کردہ نوٹس، بک مارکس اور تاریخ ختم ہو جائے گی۔"
                    else "Are you sure you want to delete your account? This action cannot be undone and will erase your saved notes, bookmarks, and sync history."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.deleteAccount()
                        showDeleteConfirm = false
                        onNavigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (isUrdu) "جی ہاں، میرا اکاؤنٹ حذف کریں" else "Yes, Delete My Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text(if (isUrdu) "منسوخ کریں" else "Cancel") }
            }
        )
    }

    if (showLanguagePicker) {
        LanguagePickerDialog(
            onDismissRequest = { showLanguagePicker = false },
            onLanguageSelected = { selectedLang ->
                authViewModel.updateProfile(
                    name = userProfile.name,
                    phone = userProfile.phone,
                    country = userProfile.country,
                    province = userProfile.province,
                    city = userProfile.city,
                    language = selectedLang.englishName,
                    theme = userProfile.theme
                )
            }
        )
    }

    if (showThemePicker) {
        ThemePickerDialog(
            onDismissRequest = { showThemePicker = false },
            onThemeSelected = { selectedTheme ->
                authViewModel.updateProfile(
                    name = userProfile.name,
                    phone = userProfile.phone,
                    country = userProfile.country,
                    province = userProfile.province,
                    city = userProfile.city,
                    language = userProfile.language,
                    theme = selectedTheme
                )
            }
        )
    }
}

@Composable
private fun ProfileStatCard(number: String, title: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ProfileOptionRow(title: String, icon: ImageVector, isUrdu: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = if (isUrdu) "کھولیں" else "Open", tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

