package com.example.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthResultState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * SignupScreen - اسلامی اور کلین ڈیزائن کے ساتھ سائن اپ اسکرین
 * 4 فیلڈز: Name, Phone, Email, Password
 * Firebase Authentication اور Firestore 'users' کلیکشن انٹیگریشن
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel? = null,
    onNavigate: (String) -> Unit,
    onSignupSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // فارم کی تمام فیلڈز
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // الرٹس (Alert Dialogs) کی اسٹیٹس
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isGoogleLoading by remember { mutableStateOf(false) }

    val onGoogleSignInClick: () -> Unit = {
        coroutineScope.launch {
            try {
                isGoogleLoading = true
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(AuthRepository.GOOGLE_WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    isGoogleLoading = false
                    authViewModel?.loginWithGoogleIdToken(idToken)
                    onSignupSuccess()
                } else {
                    isGoogleLoading = false
                    errorMessage = "گوگل کی اسناد درست نہیں ملیں۔"
                }
            } catch (e: GetCredentialCancellationException) {
                isGoogleLoading = false
            } catch (e: NoCredentialException) {
                isGoogleLoading = false
                errorMessage = "ڈیوائس پر کوئی گوگل اکاؤنٹ دستیاب نہیں ملا۔"
            } catch (e: GetCredentialException) {
                isGoogleLoading = false
                errorMessage = "Google Sign-In میں دشواری: ${e.localizedMessage ?: "دوبارہ کوشش کریں"}"
            } catch (e: Throwable) {
                isGoogleLoading = false
                errorMessage = e.localizedMessage ?: "Google Sign-In failed."
            }
        }
    }

    // رنگوں کا خوبصورت اسلامی انتخاب
    val primaryGreen = Color(0xFF0F5132)
    val lightGreen = Color(0xFFE8F5E9)
    val goldAccent = Color(0xFFD4AF37)

    // سائن اپ کا مین ہینڈلر فنکشن
    fun handleSignup() {
        val trimmedName = name.trim()
        val trimmedPhone = phone.trim()
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedConfirmPassword = confirmPassword.trim()

        // 1. تمام فیلڈز کی تصدیق (Validation)
        if (trimmedName.isBlank() || trimmedPhone.isBlank() || trimmedEmail.isBlank() || trimmedPassword.isBlank() || trimmedConfirmPassword.isBlank()) {
            errorMessage = "براہِ کرم تمام فیلڈز (نام، فون، ای میل، پاس ورڈ اور کنفرم پاس ورڈ) پُر کریں۔"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            errorMessage = "براہِ کرم درست ای میل ایڈریس درج کریں۔"
            return
        }

        if (trimmedPassword.length < 6) {
            errorMessage = "پاس ورڈ کم از کم 6 ہندسوں یا حروف پر مشتمل ہونا چاہیے۔"
            return
        }

        if (trimmedPassword != trimmedConfirmPassword) {
            errorMessage = "پاس ورڈ اور کنفرم پاس ورڈ مماثل نہیں ہیں۔ براہِ کرم تصدیق کریں۔"
            return
        }

        isLoading = true
        errorMessage = null

        coroutineScope.launch {
            try {
                // 2. Firebase Authentication سے یوزر بنانا
                val auth = FirebaseAuth.getInstance()
                val authResult = auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword).await()
                val user = authResult.user

                if (user != null) {
                    val uid = user.uid

                    // یوزر کے نام کو Firebase Auth پروفائل میں سیٹ کرنا
                    runCatching {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(trimmedName)
                            .build()
                        user.updateProfile(profileUpdates).await()
                    }

                    // 3. یوزر کا ڈیٹا Firestore Database میں "users" کلیکشن میں محفوظ کرنا (Document ID = uid)
                    val firestore = FirebaseFirestore.getInstance()
                    val userData = hashMapOf<String, Any>(
                        "uid" to uid,
                        "name" to trimmedName,
                        "phone" to trimmedPhone,
                        "email" to (user.email ?: trimmedEmail),
                        "createdAt" to FieldValue.serverTimestamp(),
                        "role" to "STUDENT"
                    )

                    firestore.collection("users")
                        .document(uid)
                        .set(userData, SetOptions.merge())
                        .await()

                    // ای میل تصدیقی لنک بھیجیں تاکہ یوزر اپنے اکاؤنٹ کی تصدیق کر سکے
                    runCatching {
                        user.sendEmailVerification().await()
                        android.util.Log.d("SignupScreen", "Verification email sent to ${user.email}")
                    }.onFailure { ex ->
                        android.util.Log.w("SignupScreen", "Failed to send verification email: ${ex.localizedMessage}")
                    }

                    // اگر AuthViewModel دستیاب ہو تو ایپ کی لوکل اسٹیٹ اپڈیٹ کریں
                    authViewModel?.let { vm ->
                        val userProfile = UserProfile(
                            uid = uid,
                            name = trimmedName,
                            email = trimmedEmail,
                            phone = trimmedPhone,
                            role = UserRole.STUDENT,
                            emailVerified = user.isEmailVerified,
                            createdAt = System.currentTimeMillis(),
                            lastLogin = System.currentTimeMillis()
                        )
                        vm.setUserProfile(userProfile)
                    }

                    // سائن اپ کامیاب الرٹ دکھائیں
                    isLoading = false
                    showSuccessDialog = true
                } else {
                    isLoading = false
                    errorMessage = "اکاؤنٹ بنانے میں دشواری پیش آئی۔ دوبارہ کوشش کریں۔"
                }
            } catch (e: Throwable) {
                isLoading = false
                val msg = e.localizedMessage ?: e.message ?: "سائن اپ کے دوران خرابی پیش آ گئی۔"
                errorMessage = msg
                android.util.Log.e("SignupScreen", "Signup error: $msg", e)
            }
        }
    }

    // کامیابی کا الرٹ ڈائیلاگ
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSignupSuccess()
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = primaryGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "مبارک ہو!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = primaryGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "آپ کا اکاؤنٹ کامیابی سے بن گیا ہے!\n\nہم نے آپ کی ای میل پر تصدیقی لنک (Verification Link) بھیج دیا ہے۔\n\nبراہِ کرم اپنا Inbox چیک کریں۔ اگر ای میل نہ ملے تو Spam / Junk فولڈر ضرور دیکھیں۔",
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSignupSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "ٹھیک ہے (Continue)", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // خرابی کا الرٹ ڈائیلاگ
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "خرابی (Error)",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = errorMessage ?: "",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "ٹھیک ہے (OK)", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // اسلامی ہیرو بینر
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(primaryGreen, Color(0xFF1E4620))
                    )
                )
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // اسلامی آئیکن
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Islamic Logo",
                        tint = goldAccent,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = goldAccent,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "بیت العلم - نیا اکاؤنٹ بنائیں",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "علمِ دین کی روشن راہوں میں خوش آمدید",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // سائن اپ کارڈ
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "سائن اپ فارم",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
                Text(
                    text = "براہِ کرم نیچے دی گئی تمام فیلڈز مکمل کریں",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 1. نام (Name Field)
                Text(
                    text = "مکمل نام (Name) *",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("اپنا پورا نام درج کریں") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Name", tint = primaryGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. فون نمبر (Phone Number Field)
                Text(
                    text = "فون نمبر (Phone Number) *",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("03XXXXXXXXX") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = "Phone", tint = primaryGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. ای میل (Email Field)
                Text(
                    text = "ای میل (Email) *",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("example@domain.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = primaryGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. پاس ورڈ (Password Field)
                Text(
                    text = "پاس ورڈ (Password) *",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("کم از کم 6 ہندسے") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Password", tint = primaryGreen)
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password",
                                tint = primaryGreen
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5. پاس ورڈ کی تصدیق (Confirm Password Field)
                val passwordsMatch = confirmPassword.isNotEmpty() && password == confirmPassword
                val passwordsMismatch = confirmPassword.isNotEmpty() && password != confirmPassword

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پاس ورڈ کی تصدیق (Confirm Password) *",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    if (passwordsMatch) {
                        Text(
                            text = "✓ مماثل ہے",
                            color = primaryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (passwordsMismatch) {
                        Text(
                            text = "⚠ مماثل نہیں",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("پاس ورڈ دوبارہ درج کریں") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LockReset,
                            contentDescription = "Confirm Password",
                            tint = if (passwordsMismatch) MaterialTheme.colorScheme.error else primaryGreen
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Confirm Password",
                                tint = primaryGreen
                            )
                        }
                    },
                    isError = passwordsMismatch,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        handleSignup()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (passwordsMismatch) MaterialTheme.colorScheme.error else primaryGreen,
                        focusedLabelColor = if (passwordsMismatch) MaterialTheme.colorScheme.error else primaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // سائن اپ بٹن (Sign Up Button)
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        handleSignup()
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "اکاؤنٹ بن رہا ہے...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Sign Up",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign Up کریں",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(14.dp))

                // گوگل سائن ان / سائن اپ بٹن
                OutlinedButton(
                    onClick = { onGoogleSignInClick() },
                    enabled = !isGoogleLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isGoogleLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = primaryGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "جاری ہے...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Google Sign Up",
                                modifier = Modifier.size(20.dp),
                                tint = primaryGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Google کے ساتھ سائن اپ کریں",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // لاگ ان اسکرین پر واپس جانے کا لنک
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پہلے سے اکاؤنٹ موجود ہے؟ ",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "لاگ ان کریں",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier
                            .clickable { onNavigate(Screen.Login.route) }
                            .padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
