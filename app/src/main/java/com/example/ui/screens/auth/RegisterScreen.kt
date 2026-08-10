package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.data.repository.AuthResultState
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.AuthViewModel
import com.example.util.lStr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("Pakistan") }
    var province by remember { mutableStateOf("Punjab") }
    var city by remember { mutableStateOf("Lahore") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf("") }

    val authState by authViewModel.authResult.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        android.util.Log.d("RegisterScreen", "Current AuthState: $authState")
        if (authState is AuthResultState.Success) {
            android.util.Log.d("RegisterScreen", "Firebase success: Navigating onRegisterSuccess")
            onRegisterSuccess()
            authViewModel.resetState()
        } else if (authState is AuthResultState.Error) {
            android.util.Log.d("RegisterScreen", "Firebase failure: ${(authState as AuthResultState.Error).errorMessage}")
        }
    }

    // Password strength indicator calculation
    val passwordStrength = remember(password) {
        when {
            password.isEmpty() -> ""
            password.length < 6 -> "Weak (min 6 characters)"
            password.length in 6..8 -> "Fair"
            password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() } -> "Strong"
            else -> "Good"
        }
    }

    val strengthColor = remember(passwordStrength) {
        when {
            passwordStrength.startsWith("Weak") -> Color.Red
            passwordStrength == "Fair" -> Color(0xFFFF9800)
            passwordStrength == "Good" -> Color(0xFF2196F3)
            passwordStrength == "Strong" -> Color(0xFF4CAF50)
            else -> Color.Gray
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 36.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Create Student / Teacher Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Join Baytul Ilm AI Digital Learning Platform",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                    text = "Account Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("${lStr("full_name")} *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("${lStr("email_address")} *") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mobile Number (Optional)
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Country, Province & City Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = province,
                        onValueChange = { province = it },
                        label = { Text("Province") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                if (password.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Password Strength: $passwordStrength",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = strengthColor,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selector
                Text(
                    text = "Select Account Role",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterChip(
                        selected = selectedRole == UserRole.STUDENT,
                        onClick = { selectedRole = UserRole.STUDENT },
                        label = { Text("Student", fontSize = 12.sp) }
                    )

                    FilterChip(
                        selected = selectedRole == UserRole.TEACHER,
                        onClick = { selectedRole = UserRole.TEACHER },
                        label = { Text("Teacher", fontSize = 12.sp) }
                    )

                    FilterChip(
                        selected = selectedRole == UserRole.ADMIN,
                        onClick = { selectedRole = UserRole.ADMIN },
                        label = { Text("Admin", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Privacy Policy & Terms Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { acceptedTerms = !acceptedTerms },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "I accept the Privacy Policy and Terms of Service *",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (validationError.isNotEmpty()) {
                    Text(
                        text = validationError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

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
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    android.util.Log.d("RegisterScreen", "User manually dismissed error")
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    else -> {}
                }

                Button(
                    onClick = {
                        android.util.Log.d("RegisterScreen", "Button clicked: Create Account with email=$email")
                        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                            validationError = "Please fill in all required fields marked with *."
                            android.util.Log.d("RegisterScreen", "Validation error: $validationError")
                            return@Button
                        }
                        if (password != confirmPassword) {
                            validationError = "Passwords do not match."
                            android.util.Log.d("RegisterScreen", "Validation error: $validationError")
                            return@Button
                        }
                        if (password.length < 6) {
                            validationError = "Password must be at least 6 characters long."
                            android.util.Log.d("RegisterScreen", "Validation error: $validationError")
                            return@Button
                        }
                        if (!acceptedTerms) {
                            validationError = "You must accept the Privacy Policy & Terms."
                            android.util.Log.d("RegisterScreen", "Validation error: $validationError")
                            return@Button
                        }
                        validationError = ""
                        android.util.Log.d("RegisterScreen", "Firebase request started for email=$email")
                        authViewModel.registerWithEmail(
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            country = country,
                            province = province,
                            city = city,
                            pass = password,
                            role = selectedRole
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = lStr("register"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = lStr("already_have_account_q"),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = lStr("login"),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigate(Screen.Login.route) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

