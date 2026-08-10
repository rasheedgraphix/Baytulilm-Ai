package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.repository.AuthResultState
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.data.repository.AuthRepository
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.AuthViewModel
import com.example.util.lStr
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    var showUnverifiedDialog by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(0) }
    var resendMessage by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authResult.collectAsStateWithLifecycle()

    val onGoogleSignInClick: () -> Unit = {
        coroutineScope.launch {
            authViewModel.setAuthLoading()
            try {
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
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    authViewModel.loginWithGoogleIdToken(idToken)
                } else {
                    authViewModel.setAuthError("Google Sign-In returned an unsupported credential type.")
                }
            } catch (e: GetCredentialCancellationException) {
                android.util.Log.d("LoginScreen", "Google Sign-In cancelled by user")
                authViewModel.resetState()
            } catch (e: NoCredentialException) {
                android.util.Log.w("LoginScreen", "No Google credentials available on device/preview", e)
                authViewModel.setAuthError("No Google account found on this device/preview. Please sign in to Google on your Android device to continue.")
            } catch (e: GetCredentialException) {
                android.util.Log.e("LoginScreen", "Credential Manager error: ${e.message}", e)
                val msg = if (e.message?.contains("NoCredentialAvailable", ignoreCase = true) == true) {
                    "No Google account found on this device/preview. Please sign in to Google on your Android device to continue."
                } else {
                    "Google Sign-In failed: ${e.localizedMessage}"
                }
                authViewModel.setAuthError(msg)
            } catch (e: Exception) {
                android.util.Log.e("LoginScreen", "Google Sign-In error: ${e.message}", e)
                authViewModel.setAuthError(e.localizedMessage ?: "Google Sign-In failed.")
            }
        }
    }

    LaunchedEffect(resendCountdown) {
        if (resendCountdown > 0) {
            delay(1000L)
            resendCountdown -= 1
        }
    }

    LaunchedEffect(authState) {
        android.util.Log.d("LoginScreen", "Current AuthState: $authState")
        if (authState is AuthResultState.Success) {
            val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
            if (currentUser != null) {
                currentUser.reload().addOnCompleteListener {
                    val reloadedUser = FirebaseAuth.getInstance().currentUser
                    if (reloadedUser != null && !reloadedUser.isEmailVerified) {
                        showUnverifiedDialog = true
                    } else {
                        onLoginSuccess()
                        authViewModel.resetState()
                    }
                }
            } else {
                onLoginSuccess()
                authViewModel.resetState()
            }
        } else if (authState is AuthResultState.Error) {
            android.util.Log.d("LoginScreen", "Firebase failure: ${(authState as AuthResultState.Error).errorMessage}")
        }
    }

    if (showUnverifiedDialog) {
        AlertDialog(
            onDismissRequest = {
                showUnverifiedDialog = false
                authViewModel.logout()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MarkEmailUnread,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = lStr("email_verification_title"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your email address has not been verified yet.\n\nPlease verify your email before signing in.\n\nCheck your Inbox.\n\nIf you cannot find the email, please also check your Spam/Junk or Promotions folder.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (resendMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = resendMessage ?: "",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 📧 Open Email App Button
                    Button(
                        onClick = {
                            openEmailClient(context) {
                                resendMessage = "No email application found."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("📧 Open Email App", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // 🔄 Resend Verification Email Button
                    OutlinedButton(
                        onClick = {
                            authViewModel.sendEmailVerification()
                            resendCountdown = 60
                            resendMessage = "Verification email resent. Please check your inbox and spam folders."
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        enabled = resendCountdown == 0,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (resendCountdown > 0) {
                                "Resend available in ${resendCountdown} seconds"
                            } else {
                                "🔄 Resend Verification Email"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Cancel Button
                    TextButton(
                        onClick = {
                            showUnverifiedDialog = false
                            authViewModel.logout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text("Cancel", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            },
            dismissButton = null,
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section with Islamic Branding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_baytul_ilm_icon_1784999011685),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = lStr("app_name"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Your Complete AI-Powered Islamic Library & Learning Platform",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Login Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = lStr("sign_in_account"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(lStr("email_address")) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(lStr("password")) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Remember Me & Forgot Password Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { rememberMe = !rememberMe }
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text(
                            text = lStr("remember_me"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = lStr("forgot_password_q"),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onNavigate(Screen.ForgotPassword.route) }
                            .padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Status / Error Message
                when (val state = authState) {
                    is AuthResultState.Error -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    android.util.Log.d("LoginScreen", "User manually dismissed error")
                                    authViewModel.resetState()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    is AuthResultState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    else -> {}
                }

                // Email Login Button
                Button(
                    onClick = {
                        android.util.Log.d("LoginScreen", "Button clicked: Sign In with email=$email")
                        authViewModel.loginWithEmail(email, password, rememberMe)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = lStr("login"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(14.dp))

                // Social Sign-In Buttons
                OutlinedButton(
                    onClick = { onGoogleSignInClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = lStr("continue_google"),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lStr("continue_google"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Register Navigation Link
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = lStr("dont_have_account_q"),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = lStr("sign_up"),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigate(Screen.Register.route) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

