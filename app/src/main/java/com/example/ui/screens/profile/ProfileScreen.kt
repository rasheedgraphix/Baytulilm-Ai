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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.input.VisualTransformation
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
import com.example.util.lStr

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

    val currentLangState by LanguageManager.currentLanguage.collectAsState()
    val currentTheme by LanguageManager.currentTheme.collectAsState()
    val appLanguage = LocalAppLanguage.current
    val langCode = appLanguage.code
    val isRtl = appLanguage.isRtl

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
                        painter = painterResource(id = R.drawable.img_baytul_ilm_icon_1784999011685),
                        contentDescription = if (langCode == "ps") "د کارونکي انځور" else if (isRtl) "صارف کی تصویر" else "User Avatar",
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
                    val roleDisplay = when (langCode) {
                        "ps" -> when (userProfile.role) {
                            UserRole.STUDENT -> "زده کوونکی"
                            UserRole.TEACHER -> "استاد / مدرس"
                            UserRole.ADMIN -> "اډمین"
                            UserRole.SUPER_ADMIN -> "سپر اډمین"
                            else -> userProfile.role
                        }
                        "ur" -> when (userProfile.role) {
                            UserRole.STUDENT -> "طالب علم"
                            UserRole.TEACHER -> "مدرس / استاد"
                            UserRole.ADMIN -> "ایڈمن"
                            UserRole.SUPER_ADMIN -> "سپر ایڈمن"
                            else -> userProfile.role
                        }
                        else -> userProfile.role
                    }

                    val roleLabelText = when (langCode) {
                        "ps" -> "دنده: $roleDisplay"
                        "ur" -> "حیثیت: $roleDisplay"
                        else -> "Role: ${userProfile.role}"
                    }

                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = roleLabelText,
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
                        val verifiedText = when (langCode) {
                            "ps" -> "تصدیق شوی"
                            "ur" -> "تصدیق شدہ"
                            else -> "Verified"
                        }
                        SuggestionChip(
                            onClick = { },
                            label = { Text(verifiedText, fontSize = 11.sp, color = Color(0xFF2E7D32)) },
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFE8F5E9))
                        )
                    } else {
                        val verifyText = when (langCode) {
                            "ps" -> "ایمیل تصدیق کړئ"
                            "ur" -> "ای میل تصدیق کریں"
                            else -> "Verify Email"
                        }
                        SuggestionChip(
                            onClick = { authViewModel.sendEmailVerification() },
                            label = { Text(verifyText, fontSize = 11.sp, color = Color(0xFFC62828)) },
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
            ProfileStatCard(number = "${favorites.size}", title = lStr("favorites"), modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${bookmarks.size}", title = lStr("bookmarks"), modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${downloads.size}", title = lStr("downloads"), modifier = Modifier.weight(1f))
            ProfileStatCard(number = "${recentReadings.size}", title = lStr("recent_reading"), modifier = Modifier.weight(1f))
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
                    lStr("edit_profile"),
                    Icons.Default.Edit,
                    isRtl = isRtl
                ) {
                    showEditDialog = true
                }
                ProfileOptionRow(
                    "${lStr("app_language")} (${currentLangState.flagEmoji} ${currentLangState.nativeName})",
                    Icons.Default.Language,
                    isRtl = isRtl
                ) {
                    showLanguagePicker = true
                }

                val themeText = when (currentTheme) {
                    "Light" -> lStr("light_theme")
                    "Dark" -> lStr("dark_theme")
                    else -> lStr("system_theme")
                }

                ProfileOptionRow(
                    "${lStr("theme_settings")} ($themeText)",
                    Icons.Default.ColorLens,
                    isRtl = isRtl
                ) {
                    showThemePicker = true
                }
                ProfileOptionRow(
                    when (langCode) {
                        "ps" -> "پاسورډ بدل کړئ"
                        "ur" -> "پاس ورڈ تبدیل کریں"
                        else -> "Change Password"
                    },
                    Icons.Default.LockReset,
                    isRtl = isRtl
                ) {
                    showPasswordDialog = true
                }
                ProfileOptionRow(
                    lStr("my_bookmarks"),
                    Icons.Default.Bookmark,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.Bookmarks.route)
                }
                ProfileOptionRow(
                    lStr("my_favorites"),
                    Icons.Default.Favorite,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.Favorites.route)
                }
                ProfileOptionRow(
                    lStr("recent_history"),
                    Icons.Default.History,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.Recent.route)
                }
                ProfileOptionRow(
                    lStr("downloaded_books"),
                    Icons.Default.Download,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.Downloads.route)
                }
                ProfileOptionRow(
                    lStr("settings"),
                    Icons.Default.Settings,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.Settings.route)
                }
                ProfileOptionRow(
                    lStr("about"),
                    Icons.Default.Info,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.About.route)
                }
                ProfileOptionRow(
                    if (langCode == "ur") "رازداری کی پالیسی (Privacy Policy)" else "Privacy Policy",
                    Icons.Default.Lock,
                    isRtl = isRtl
                ) {
                    onNavigate(Screen.PrivacyPolicy.route)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val isGuest = userProfile.uid.isEmpty() || userProfile.uid == "guest_learner" || runCatching { com.google.firebase.auth.FirebaseAuth.getInstance().currentUser }.getOrNull() == null

        // Auth Action Button (Login for Guest / Logout for User)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (isGuest) {
                Button(
                    onClick = { onNavigate(Screen.Login.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.Login,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (langCode) {
                            "ps" -> "ننوتل / نوی اکاونټ جوړول"
                            "ur" -> "لاگ ان کریں / نیا اکاؤنٹ بنائیں"
                            else -> "Sign In / Create Account"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
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
                            lStr("sign_out"),
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
                        val deleteText = when (langCode) {
                            "ps" -> "اکاونټ او معلومات حذف کړئ"
                            "ur" -> "اکاؤنٹ اور ڈیٹا حذف کریں"
                            else -> "Delete Account & Data"
                        }
                        Text(
                            deleteText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
            title = { Text(lStr("edit_profile")) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val labelName = lStr("full_name")
                    val labelPhone = when (langCode) {
                        "ps" -> "تلیفون شمیره"
                        "ur" -> "موبائل نمبر"
                        else -> "Mobile Number"
                    }
                    val labelCountry = when (langCode) {
                        "ps" -> "هیواد"
                        "ur" -> "ملک"
                        else -> "Country"
                    }
                    val labelProvince = when (langCode) {
                        "ps" -> "ولایت / صوبہ"
                        "ur" -> "صوبہ"
                        else -> "Province"
                    }
                    val labelCity = when (langCode) {
                        "ps" -> "ښار"
                        "ur" -> "شہر"
                        else -> "City"
                    }

                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text(labelName) })
                    OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text(labelPhone) })
                    OutlinedTextField(value = editCountry, onValueChange = { editCountry = it }, label = { Text(labelCountry) })
                    OutlinedTextField(value = editProvince, onValueChange = { editProvince = it }, label = { Text(labelProvince) })
                    OutlinedTextField(value = editCity, onValueChange = { editCity = it }, label = { Text(labelCity) })
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
                    Text(lStr("save_changes"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text(lStr("cancel")) }
            }
        )
    }

    // Change Password Dialog
    if (showPasswordDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf<String?>(null) }
        var isOldPasswordVisible by remember { mutableStateOf(false) }
        var isNewPasswordVisible by remember { mutableStateOf(false) }

        val titleChangePass = when (langCode) {
            "ps" -> "پاسورډ بدل کړئ"
            "ur" -> "پاس ورڈ تبدیل کریں"
            else -> "Change Password"
        }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text(titleChangePass) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val labelOld = when (langCode) {
                        "ps" -> "اوسنی پاسورډ"
                        "ur" -> "موجودہ پاس ورڈ"
                        else -> "Current Password"
                    }
                    val labelNew = when (langCode) {
                        "ps" -> "نوی پاسورډ (لږ تر لږه ۸ توري)"
                        "ur" -> "نیا پاس ورڈ (کم از کم 8 حروف)"
                        else -> "New Password (min 8 chars)"
                    }
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text(labelOld) },
                        trailingIcon = {
                            IconButton(onClick = { isOldPasswordVisible = !isOldPasswordVisible }) {
                                Icon(
                                    imageVector = if (isOldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isOldPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isOldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = {
                            newPass = it
                            passwordError = null
                        },
                        label = { Text(labelNew) },
                        trailingIcon = {
                            IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                                Icon(
                                    imageVector = if (isNewPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isNewPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError != null
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val validationError = com.example.util.PasswordValidator.validatePassword(newPass)
                    if (validationError != null) {
                        passwordError = validationError
                        return@Button
                    }
                    authViewModel.changePassword(oldPass, newPass)
                    showPasswordDialog = false
                }) {
                    val updateText = when (langCode) {
                        "ps" -> "پاسورډ اپ ډیټ کړئ"
                        "ur" -> "پاس ورڈ اپ ڈیٹ کریں"
                        else -> "Update Password"
                    }
                    Text(updateText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text(lStr("cancel")) }
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteConfirm) {
        val titleDelete = when (langCode) {
            "ps" -> "اکاونټ حذف کوئ؟"
            "ur" -> "اکاؤنٹ حذف کریں؟"
            else -> "Delete Account?"
        }
        val bodyDelete = when (langCode) {
            "ps" -> "ایا تاسو ډاډه یاست چې خپل اکاونټ حذف کول غواړئ؟ دا عمل بېرته نشي کیدی او ستاسو محفوظ شوي ټول معلومات، بک مارکونه او تاريخ به حذف شي."
            "ur" -> "کیا آپ واقعی اپنا اکاؤنٹ حذف کرنا چاہتے ہیں؟ یہ عمل واپس نہیں ہو سکتا اور آپ کے محفوظ کردہ نوٹس، بک مارکس اور تاریخ ختم ہو جائے گی۔"
            else -> "Are you sure you want to delete your account? This action cannot be undone and will erase your saved notes, bookmarks, and sync history."
        }
        val confirmDeleteText = when (langCode) {
            "ps" -> "هو، زما اکاونټ حذف کړئ"
            "ur" -> "جی ہاں، میرا اکاؤنٹ حذف کریں"
            else -> "Yes, Delete My Account"
        }

        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(titleDelete) },
            text = { Text(bodyDelete) },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.deleteAccount()
                        showDeleteConfirm = false
                        onNavigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(confirmDeleteText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text(lStr("cancel")) }
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
private fun ProfileOptionRow(title: String, icon: ImageVector, isRtl: Boolean = false, onClick: () -> Unit) {
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

        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = if (isRtl) "خلاص کړئ" else "Open", tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

