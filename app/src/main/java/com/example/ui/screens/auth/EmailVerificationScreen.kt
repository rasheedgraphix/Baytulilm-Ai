package com.example.ui.screens.auth

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.AuthResultState
import com.example.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun openEmailClient(context: Context, onNotFound: () -> Unit) {
    try {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_EMAIL)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val genericIntent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse("mailto:")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(genericIntent)
        } catch (ex: Exception) {
            onNotFound()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val userState by authViewModel.userState.collectAsStateWithLifecycle()
    val authResult by authViewModel.authResult.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isChecking by remember { mutableStateOf(false) }
    var isVerifiedSuccess by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Automatic check on Lifecycle ON_RESUME and handle Firebase Action Code if present
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val activity = context as? androidx.activity.ComponentActivity
                val intentData = activity?.intent?.data
                if (intentData != null) {
                    val mode = intentData.getQueryParameter("mode")
                    val oobCode = intentData.getQueryParameter("oobCode")
                    if (mode == "verifyEmail" && !oobCode.isNullOrEmpty()) {
                        FirebaseAuth.getInstance().applyActionCode(oobCode)
                            .addOnCompleteListener {
                                authViewModel.checkEmailVerification()
                            }
                    } else {
                        authViewModel.checkEmailVerification()
                    }
                } else {
                    authViewModel.checkEmailVerification()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Resend countdown timer logic
    LaunchedEffect(resendCountdown) {
        if (resendCountdown > 0) {
            delay(1000L)
            resendCountdown -= 1
        }
    }

    // Handle verification success detection
    LaunchedEffect(userState.emailVerified) {
        if (userState.emailVerified && !isVerifiedSuccess) {
            isVerifiedSuccess = true
            isChecking = false
            errorMessage = null
            scope.launch {
                snackbarHostState.showSnackbar("You can now sign in.")
            }
            delay(2000L)
            authViewModel.logout()
            onNavigateToLogin()
        }
    }

    // Monitor auth result state for errors or messages
    LaunchedEffect(authResult) {
        if (authResult is AuthResultState.Error && !userState.emailVerified) {
            val errState = authResult as AuthResultState.Error
            if (isChecking) {
                isChecking = false
                errorMessage = "Your email is not verified yet.\nPlease verify your email first."
            }
            android.util.Log.d("EmailVerification", "Auth result error: ${errState.errorMessage}")
        } else if (authResult is AuthResultState.Success && userState.emailVerified && !isVerifiedSuccess) {
            isVerifiedSuccess = true
            isChecking = false
            errorMessage = null
            scope.launch {
                snackbarHostState.showSnackbar("You can now sign in.")
            }
            delay(2000L)
            authViewModel.logout()
            onNavigateToLogin()
        }
    }

    // Auto-polling every 5 seconds while screen is active
    LaunchedEffect(Unit) {
        while (!isVerifiedSuccess) {
            delay(5000L)
            if (!isVerifiedSuccess) {
                authViewModel.checkEmailVerification()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section with Baytul Ilm AI Islamic Branding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 36.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVerifiedSuccess) Icons.Default.CheckCircle else Icons.Default.MarkEmailUnread,
                            contentDescription = "Email Verification",
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isVerifiedSuccess) "Email Verified!" else "Verify Your Email",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (userState.email.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userState.email,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Main Content Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success State Card with Animation
                AnimatedVisibility(
                    visible = isVerifiedSuccess,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val checkScale by animateFloatAsState(
                                targetValue = if (isVerifiedSuccess) 1.2f else 0.8f,
                                animationSpec = tween(durationMillis = 600),
                                label = "checkScale"
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier
                                    .size(64.dp)
                                    .scale(checkScale)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Email Verified Successfully!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You can now sign in to Baytul Ilm AI.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E7D32),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Redirecting to sign in screen...",
                                fontSize = 13.sp,
                                color = Color(0xFF388E3C),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Error Message Banner if verification check failed
                AnimatedVisibility(
                    visible = errorMessage != null && !isVerifiedSuccess,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Instructions Card with Clear User Guidance
                if (!isVerifiedSuccess) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "📩 A verification email has been sent.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Please check your Inbox.\n\nIf you do not receive it within a few minutes, please check your:\n• Spam folder\n• Junk folder\n• Promotions folder\n\nStill not received?\nTap \"Resend Verification Email\".",
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 1. 📧 Open Email App Button
                    Button(
                        onClick = {
                            openEmailClient(context) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("No email application found.")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Open Email App",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📧 Open Email App",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Primary Button: "✅ I've Verified My Email"
                    OutlinedButton(
                        onClick = {
                            isChecking = true
                            errorMessage = null
                            authViewModel.checkEmailVerification()
                            scope.launch {
                                delay(1500L)
                                if (isChecking && !userState.emailVerified) {
                                    isChecking = false
                                    errorMessage = "Your email is not verified yet.\nPlease verify your email first."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isChecking,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isChecking) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Checking Status...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text(
                                text = "✅ I've Verified My Email",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 5. 🔄 Resend Verification Email with 60s Countdown
                    OutlinedButton(
                        onClick = {
                            authViewModel.sendEmailVerification()
                            resendCountdown = 60
                            scope.launch {
                                snackbarHostState.showSnackbar("Verification email resent. Please check your Inbox and Spam folders.")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = resendCountdown == 0 && !isChecking,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Resend Email",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (resendCountdown > 0) {
                                "Resend available in ${resendCountdown} seconds"
                            } else {
                                "🔄 Resend Verification Email"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. ✏️ Change Email Address Button
                    TextButton(
                        onClick = {
                            authViewModel.logout()
                            onNavigateToRegister()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = !isChecking
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Change Email",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "← Use a Different Email Address",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Logout & Back to Login
                    TextButton(
                        onClick = {
                            authViewModel.logout()
                            onNavigateToLogin()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        enabled = !isChecking
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign Out",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sign Out & Back to Login",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

