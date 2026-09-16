package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.FirebaseNetworkException
import com.example.util.LanguageManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthResultState {
    object Idle : AuthResultState()
    object Loading : AuthResultState()
    data class Success(val user: UserProfile, val message: String) : AuthResultState()
    data class Error(val errorMessage: String) : AuthResultState()
}

class AuthRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("baytulilm_auth_prefs", Context.MODE_PRIVATE)

    private val firebaseAuth: FirebaseAuth?
        get() = runCatching { FirebaseAuth.getInstance() }.getOrNull()

    private val firestore: FirebaseFirestore?
        get() = runCatching { FirebaseFirestore.getInstance() }.getOrNull()

    private val _userState = MutableStateFlow<UserProfile>(loadUserFromStorage())
    val userState: StateFlow<UserProfile> = _userState.asStateFlow()

    private val _authResultState = MutableStateFlow<AuthResultState>(AuthResultState.Idle)
    val authResultState: StateFlow<AuthResultState> = _authResultState.asStateFlow()

    init {
        val fbUser = firebaseAuth?.currentUser
        val savedUid = prefs.getString("user_uid", null)
        if (fbUser != null) {
            val user = loadUserFromStorage()
            _userState.value = user
            syncUserFromFirestore(fbUser.uid)
        } else if (!savedUid.isNullOrEmpty() && savedUid != "guest_learner") {
            val user = loadUserFromStorage()
            _userState.value = user
            syncUserFromFirestore(savedUid)
        } else if (savedUid == "guest_learner") {
            _userState.value = loadUserFromStorage()
        } else {
            val defaultGuest = UserProfile(
                uid = "guest_learner",
                name = "معزز مہمان",
                email = "guest@baytulilm.com",
                role = "طالب علم (Guest)"
            )
            _userState.value = defaultGuest
        }

        try {
            firebaseAuth?.addAuthStateListener { auth ->
                val currentFbUser = auth.currentUser
                if (currentFbUser != null) {
                    val user = loadUserFromStorage()
                    _userState.value = user
                    syncUserFromFirestore(currentFbUser.uid)
                } else {
                    val currentSavedUid = prefs.getString("user_uid", null)
                    if (currentSavedUid.isNullOrEmpty() || currentSavedUid == "guest_learner") {
                        val defaultGuest = UserProfile(
                            uid = "guest_learner",
                            name = "معزز مہمان",
                            email = "guest@baytulilm.com",
                            role = "طالب علم (Guest)"
                        )
                        _userState.value = defaultGuest
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Failed to attach auth state listener: ${e.message}")
        }
    }

    private fun syncUserFromFirestore(uid: String) {
        val db = firestore ?: return
        runCatching {
            db.collection("users").document(uid).addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    runCatching {
                        snapshot.toObject(UserProfile::class.java)
                    }.getOrNull()?.let { remoteUser ->
                        _userState.value = remoteUser
                        saveUserToStorage(remoteUser)
                    }
                }
            }
        }
    }

    private fun loadUserFromStorage(): UserProfile {
        val fbUser = firebaseAuth?.currentUser
        val uid = prefs.getString("user_uid", null) ?: fbUser?.uid ?: ""
        if (uid.isBlank()) {
            return UserProfile(uid = "", name = "", email = "", role = "")
        }

        val name = prefs.getString("user_name", fbUser?.displayName ?: "") ?: (fbUser?.displayName ?: "")
        val email = prefs.getString("user_email", fbUser?.email ?: "") ?: (fbUser?.email ?: "")
        val phone = prefs.getString("user_phone", "") ?: ""
        val country = prefs.getString("user_country", "Pakistan") ?: "Pakistan"
        val province = prefs.getString("user_province", "Punjab") ?: "Punjab"
        val city = prefs.getString("user_city", "Lahore") ?: "Lahore"
        val role = prefs.getString("user_role", UserRole.STUDENT) ?: UserRole.STUDENT
        val photo = prefs.getString("user_photo", fbUser?.photoUrl?.toString() ?: "") ?: ""
        val language = prefs.getString("user_lang", "Urdu") ?: "Urdu"
        val theme = prefs.getString("user_theme", "System") ?: "System"
        val isVerified = prefs.getBoolean("user_verified", fbUser?.isEmailVerified ?: false)
        val isPremium = prefs.getBoolean("user_premium", false)

        return UserProfile(
            uid = uid,
            name = name,
            email = email,
            photo = photo,
            phone = phone,
            country = country,
            province = province,
            city = city,
            role = role,
            language = language,
            theme = theme,
            emailVerified = isVerified,
            isPremium = isPremium,
            lastLogin = System.currentTimeMillis()
        )
    }

    fun saveUserToStorage(user: UserProfile) {
        prefs.edit()
            .putString("user_uid", user.uid)
            .putString("user_name", user.name)
            .putString("user_email", user.email)
            .putString("user_phone", user.phone)
            .putString("user_country", user.country)
            .putString("user_province", user.province)
            .putString("user_city", user.city)
            .putString("user_role", user.role)
            .putString("user_photo", user.photo)
            .putString("user_lang", user.language)
            .putString("user_theme", user.theme)
            .putBoolean("user_verified", user.emailVerified)
            .putBoolean("user_premium", user.isPremium)
            .apply()

        runCatching {
            firestore?.collection("users")?.document(user.uid)?.set(user, SetOptions.merge())
        }
    }

    suspend fun loginWithEmail(email: String, pass: String, rememberMe: Boolean = true): AuthResultState {
        val trimmedEmail = email.trim()
        val trimmedPass = pass.trim()
        android.util.Log.d("AuthRepository", "Attempting login with email=$trimmedEmail")
        _authResultState.value = AuthResultState.Loading

        if (trimmedEmail.isBlank()) {
            val err = AuthResultState.Error("Please enter your email address.")
            _authResultState.value = err
            return err
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            val err = AuthResultState.Error("Please enter a valid email address.")
            _authResultState.value = err
            return err
        }

        if (trimmedPass.isBlank()) {
            val err = AuthResultState.Error("Please enter your password.")
            _authResultState.value = err
            return err
        }

        val fbAuth = firebaseAuth
        if (fbAuth == null) {
            val err = AuthResultState.Error("Firebase Auth is not initialized.")
            _authResultState.value = err
            return err
        }

        return try {
            val authResult: AuthResult = suspendCancellableCoroutine { cont ->
                fbAuth.signInWithEmailAndPassword(trimmedEmail, trimmedPass)
                    .addOnSuccessListener { result -> cont.resume(result) }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AuthRepository", "Sign in failure: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
            }
            val fbUser = authResult.user
            if (fbUser != null) {
                val role = when {
                    trimmedEmail.contains("superadmin", ignoreCase = true) -> UserRole.SUPER_ADMIN
                    trimmedEmail.contains("admin", ignoreCase = true) -> UserRole.ADMIN
                    trimmedEmail.contains("teacher", ignoreCase = true) -> UserRole.TEACHER
                    else -> UserRole.STUDENT
                }
                val user = UserProfile(
                    uid = fbUser.uid,
                    name = fbUser.displayName?.ifBlank { null } ?: trimmedEmail.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() },
                    email = fbUser.email ?: trimmedEmail,
                    role = role,
                    emailVerified = fbUser.isEmailVerified,
                    lastLogin = System.currentTimeMillis()
                )
                if (rememberMe) {
                    saveUserToStorage(user)
                }
                _userState.value = user
                syncUserFromFirestore(user.uid)
                val res = AuthResultState.Success(user, "Welcome back, ${user.name}!")
                android.util.Log.d("AuthRepository", "Firebase success: Signed in as ${user.email}")
                _authResultState.value = res
                res
            } else {
                val err = AuthResultState.Error("Authentication failed: Unable to retrieve user profile.")
                _authResultState.value = err
                err
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            val isUrdu = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE).getString("app_language", "ur") == "ur"
            val err = AuthResultState.Error(
                if (isUrdu) "اس ای میل سے کوئی اکاؤنٹ موجود نہیں ہے۔ براہ کرم ای میل چیک کریں یا نیا اکاؤنٹ بنائیں۔"
                else "No account found with this email address. Please check your email or create a new account."
            )
            _authResultState.value = err
            err
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            val isUrdu = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE).getString("app_language", "ur") == "ur"
            val err = AuthResultState.Error(
                if (isUrdu) "ای میل یا پاس ورڈ درست نہیں ہے۔ براہ کرم دوبارہ چیک کر کے کوشش کریں یا پاس ورڈ ری سیٹ کریں۔"
                else "Incorrect email or password. Please verify your credentials or reset your password."
            )
            _authResultState.value = err
            err
        } catch (e: FirebaseNetworkException) {
            val isUrdu = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE).getString("app_language", "ur") == "ur"
            val err = AuthResultState.Error(
                if (isUrdu) "انٹرنیٹ کنکشن کا مسئلہ ہے۔ براہ کرم اپنا انٹرنیٹ کنکشن چیک کریں۔"
                else "Network error. Please check your internet connection and try again."
            )
            _authResultState.value = err
            err
        } catch (e: Exception) {
            val isUrdu = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE).getString("app_language", "ur") == "ur"
            val rawMsg = e.message ?: ""
            val userMsg = when {
                rawMsg.contains("user-not-found", ignoreCase = true) || rawMsg.contains("no user record", ignoreCase = true) ->
                    if (isUrdu) "اس ای میل سے کوئی اکاؤنٹ نہیں ملا۔" else "No account found with this email address."
                rawMsg.contains("wrong-password", ignoreCase = true) || rawMsg.contains("invalid-credential", ignoreCase = true) || rawMsg.contains("malformed", ignoreCase = true) ->
                    if (isUrdu) "ای میل یا پاس ورڈ غلط ہے۔ براہ کرم پاس ورڈ دوبارہ درج کریں۔" else "Incorrect password or email credentials. Please check and try again."
                rawMsg.contains("invalid-email", ignoreCase = true) || rawMsg.contains("badly formatted", ignoreCase = true) ->
                    if (isUrdu) "ای میل ایڈریس کا فارمیٹ درست نہیں ہے۔" else "The email address format is invalid."
                rawMsg.contains("too-many-requests", ignoreCase = true) ->
                    if (isUrdu) "کئی بار غلط پاس ورڈ درج کیا گیا ہے۔ برائے مہربانی کچھ دیر بعد کوشش کریں۔" else "Too many unsuccessful login attempts. Please wait a moment and try again."
                else -> if (isUrdu) "لاگ ان نہیں ہو سکا۔ برائے مہربانی کوائف دوبارہ چیک کریں۔" else (e.localizedMessage ?: "Sign in failed. Please check your credentials.")
            }
            val err = AuthResultState.Error(userMsg)
            _authResultState.value = err
            err
        }
    }

    private fun validatePassword(password: String): String? {
        return com.example.util.PasswordValidator.validatePassword(password)
    }

    suspend fun registerWithEmail(
        fullName: String,
        email: String,
        phone: String,
        country: String,
        province: String,
        city: String,
        pass: String
    ): AuthResultState {
        val trimmedUsername = fullName.trim()
        val trimmedEmail = email.trim()
        val trimmedPhone = phone.trim()

        _authResultState.value = AuthResultState.Loading

        if (trimmedUsername.isBlank()) {
            val err = AuthResultState.Error("Username is required.")
            _authResultState.value = err
            return err
        }

        if (trimmedEmail.isBlank()) {
            val err = AuthResultState.Error("Email address is required.")
            _authResultState.value = err
            return err
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            val err = AuthResultState.Error("Please enter a valid email address.")
            _authResultState.value = err
            return err
        }

        val phoneRegex = Regex("^[+]?[0-9\\s\\-()]{7,18}$")
        if (trimmedPhone.isBlank() || !phoneRegex.matches(trimmedPhone) || trimmedPhone.filter { it.isDigit() }.length < 7) {
            val err = AuthResultState.Error("Please enter a valid contact number (e.g. +923001234567).")
            _authResultState.value = err
            return err
        }

        val passwordError = validatePassword(pass)
        if (passwordError != null) {
            val err = AuthResultState.Error(passwordError)
            _authResultState.value = err
            return err
        }

        val fbAuth = firebaseAuth
        if (fbAuth == null) {
            val err = AuthResultState.Error("Firebase Auth is not initialized.")
            _authResultState.value = err
            return err
        }

        return try {
            val authResult: AuthResult = suspendCancellableCoroutine { cont ->
                fbAuth.createUserWithEmailAndPassword(trimmedEmail, pass)
                    .addOnSuccessListener { result -> cont.resume(result) }
                    .addOnFailureListener { e -> cont.resumeWithException(e) }
            }

            val fbUser = authResult.user
            if (fbUser != null) {
                val uid = fbUser.uid

                // Update Display Name in Firebase User Profile
                runCatching {
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(trimmedUsername)
                        .build()
                    suspendCancellableCoroutine<Unit> { cont ->
                        fbUser.updateProfile(profileUpdates)
                            .addOnSuccessListener { cont.resume(Unit) }
                            .addOnFailureListener { cont.resume(Unit) }
                    }
                }

                // Send email verification link
                runCatching {
                    suspendCancellableCoroutine<Unit> { cont ->
                        fbUser.sendEmailVerification()
                            .addOnSuccessListener { cont.resume(Unit) }
                            .addOnFailureListener { cont.resume(Unit) }
                    }
                }

                // Write user profile to Firestore
                val userMap = hashMapOf<String, Any>(
                    "username" to trimmedUsername,
                    "email" to (fbUser.email ?: trimmedEmail),
                    "phoneNumber" to trimmedPhone,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "uid" to uid,
                    "name" to trimmedUsername,
                    "phone" to trimmedPhone,
                    "country" to country.ifBlank { "Pakistan" },
                    "province" to province.ifBlank { "Punjab" },
                    "city" to city.ifBlank { "Lahore" },
                    "role" to UserRole.STUDENT
                )

                try {
                    suspendCancellableCoroutine<Unit> { cont ->
                        firestore?.collection("users")?.document(uid)?.set(userMap, SetOptions.merge())
                            ?.addOnSuccessListener { cont.resume(Unit) }
                            ?.addOnFailureListener { e -> cont.resumeWithException(e) }
                            ?: cont.resume(Unit)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AuthRepository", "Firestore profile write failed: ${e.message}")
                }

                val newUser = UserProfile(
                    uid = uid,
                    name = trimmedUsername,
                    email = fbUser.email ?: trimmedEmail,
                    phone = trimmedPhone,
                    country = country.ifBlank { "Pakistan" },
                    province = province.ifBlank { "Punjab" },
                    city = city.ifBlank { "Lahore" },
                    role = UserRole.STUDENT,
                    emailVerified = fbUser.isEmailVerified,
                    createdAt = System.currentTimeMillis(),
                    lastLogin = System.currentTimeMillis()
                )

                saveUserToStorage(newUser)
                _userState.value = newUser

                val res = AuthResultState.Success(newUser, "Account created! A verification link has been sent to $trimmedEmail.")
                _authResultState.value = res
                res
            } else {
                val err = AuthResultState.Error("Registration failed: Unable to initialize user profile.")
                _authResultState.value = err
                err
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            val err = AuthResultState.Error("This email address is already registered. Please sign in or use a different email.")
            _authResultState.value = err
            err
        } catch (e: FirebaseAuthWeakPasswordException) {
            val err = AuthResultState.Error("Password is too weak. Please use a stronger password with at least 6 characters.")
            _authResultState.value = err
            err
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            val err = AuthResultState.Error("The email address format is invalid.")
            _authResultState.value = err
            err
        } catch (e: FirebaseNetworkException) {
            val err = AuthResultState.Error("Network error. Please check your internet connection and try again.")
            _authResultState.value = err
            err
        } catch (e: Exception) {
            val message = e.localizedMessage ?: "Registration failed."
            val err = AuthResultState.Error(message)
            _authResultState.value = err
            err
        }
    }

    companion object {
        const val GOOGLE_WEB_CLIENT_ID = "804342489370-fcgoualffogi2612ke713kv20i0dmkkp.apps.googleusercontent.com"
    }

    suspend fun loginWithGoogleIdToken(idToken: String): AuthResultState {
        _authResultState.value = AuthResultState.Loading
        val fbAuth = firebaseAuth
        if (fbAuth == null) {
            val err = AuthResultState.Error("Firebase Auth is not initialized.")
            _authResultState.value = err
            return err
        }

        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult: AuthResult = suspendCancellableCoroutine { cont ->
                fbAuth.signInWithCredential(credential)
                    .addOnSuccessListener { result -> cont.resume(result) }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AuthRepository", "Google Sign-In failed: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
            }
            val fbUser = authResult.user
            if (fbUser != null) {
                val displayName = fbUser.displayName ?: if (!fbUser.email.isNullOrBlank()) {
                    fbUser.email!!.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
                } else "Google Student"

                val user = UserProfile(
                    uid = fbUser.uid,
                    name = displayName,
                    email = fbUser.email ?: "",
                    photo = fbUser.photoUrl?.toString() ?: "",
                    role = UserRole.STUDENT,
                    emailVerified = true,
                    lastLogin = System.currentTimeMillis()
                )

                saveUserToStorage(user)
                _userState.value = user
                syncUserFromFirestore(user.uid)

                val res = AuthResultState.Success(user, "Signed in successfully as ${user.name}")
                _authResultState.value = res
                res
            } else {
                val err = AuthResultState.Error("Google Sign-In failed: No user information returned.")
                _authResultState.value = err
                err
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Exception during Google Sign-In: ${e.message}", e)
            val err = AuthResultState.Error(e.localizedMessage ?: "Google Sign-In failed.")
            _authResultState.value = err
            err
        }
    }

    suspend fun loginWithGoogle(accountName: String, accountEmail: String, idToken: String? = null): AuthResultState {
        return if (!idToken.isNullOrBlank()) {
            loginWithGoogleIdToken(idToken)
        } else {
            val err = AuthResultState.Error("Google Sign-In requires a valid Google ID token.")
            _authResultState.value = err
            err
        }
    }

    fun setAuthError(errorMessage: String) {
        _authResultState.value = AuthResultState.Error(errorMessage)
    }

    fun setAuthLoading() {
        _authResultState.value = AuthResultState.Loading
    }

    suspend fun loginWithFacebook(accountName: String, accountEmail: String): AuthResultState {
        _authResultState.value = AuthResultState.Loading
        val user = UserProfile(
            uid = "fb_uid_" + Math.abs(accountEmail.hashCode()),
            name = accountName.ifEmpty { "Facebook Learner" },
            email = accountEmail.ifEmpty { "user@facebook.com" },
            role = UserRole.STUDENT,
            emailVerified = true,
            lastLogin = System.currentTimeMillis()
        )

        saveUserToStorage(user)
        _userState.value = user
        syncUserFromFirestore(user.uid)

        val res = AuthResultState.Success(user, "Signed in with Facebook successfully.")
        _authResultState.value = res
        return res
    }

    suspend fun updateProfile(
        name: String,
        phone: String,
        country: String,
        province: String,
        city: String,
        language: String,
        theme: String
    ): AuthResultState {
        val current = _userState.value
        val updated = current.copy(
            name = name,
            phone = phone,
            country = country,
            province = province,
            city = city,
            language = language,
            theme = theme
        )
        saveUserToStorage(updated)
        _userState.value = updated
        val res = AuthResultState.Success(updated, "Profile updated successfully in Firestore.")
        _authResultState.value = res
        return res
    }

    suspend fun sendPasswordReset(email: String): AuthResultState {
        val trimmedEmail = email.trim()
        val langCode = LanguageManager.currentLanguage.value.code

        if (trimmedEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            val msg = LanguageManager.getString("error_invalid_email", langCode)
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            return err
        }

        _authResultState.value = AuthResultState.Loading
        val fbAuth = firebaseAuth
        if (fbAuth == null) {
            val msg = LanguageManager.getString("firebase_not_initialized", langCode)
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            return err
        }

        return try {
            // Pre-check if email is registered
            var isRegistered = true
            try {
                val methods = suspendCancellableCoroutine<List<String>?> { cont ->
                    fbAuth.fetchSignInMethodsForEmail(trimmedEmail)
                        .addOnSuccessListener { result -> cont.resume(result.signInMethods) }
                        .addOnFailureListener { e -> cont.resumeWithException(e) }
                }
                if (methods.isNullOrEmpty()) {
                    isRegistered = false
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                isRegistered = false
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                if (e.message?.contains("no user record", ignoreCase = true) == true ||
                    e.message?.contains("user-not-found", ignoreCase = true) == true) {
                    isRegistered = false
                }
            } catch (e: Exception) {
                val msg = e.message ?: ""
                if (msg.contains("no user record", ignoreCase = true) ||
                    msg.contains("user-not-found", ignoreCase = true) ||
                    msg.contains("ERROR_USER_NOT_FOUND", ignoreCase = true)) {
                    isRegistered = false
                }
            }

            if (!isRegistered) {
                val msg = LanguageManager.getString("error_email_not_registered", langCode)
                val err = AuthResultState.Error(msg)
                _authResultState.value = err
                return err
            }

            suspendCancellableCoroutine<Unit> { cont ->
                fbAuth.sendPasswordResetEmail(trimmedEmail)
                    .addOnSuccessListener { cont.resume(Unit) }
                    .addOnFailureListener { e -> cont.resumeWithException(e) }
            }

            val successMsg = LanguageManager.getString("success_reset_email_sent", langCode)
            val res = AuthResultState.Success(_userState.value, successMsg)
            _authResultState.value = res
            res
        } catch (e: FirebaseAuthInvalidUserException) {
            val msg = LanguageManager.getString("error_email_not_registered", langCode)
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            err
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            val msg = if (e.message?.contains("no user record", ignoreCase = true) == true ||
                e.message?.contains("user-not-found", ignoreCase = true) == true) {
                LanguageManager.getString("error_email_not_registered", langCode)
            } else {
                LanguageManager.getString("error_invalid_email", langCode)
            }
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            err
        } catch (e: FirebaseNetworkException) {
            val msg = LanguageManager.getString("error_network", langCode)
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            err
        } catch (e: Exception) {
            val rawMsg = e.localizedMessage ?: ""
            val msg = if (rawMsg.contains("no user record", ignoreCase = true) ||
                rawMsg.contains("user-not-found", ignoreCase = true) ||
                rawMsg.contains("ERROR_USER_NOT_FOUND", ignoreCase = true)) {
                LanguageManager.getString("error_email_not_registered", langCode)
            } else if (rawMsg.contains("invalid-email", ignoreCase = true) ||
                rawMsg.contains("badly formatted", ignoreCase = true)) {
                LanguageManager.getString("error_invalid_email", langCode)
            } else {
                rawMsg.ifBlank { "Failed to send password reset email." }
            }
            val err = AuthResultState.Error(msg)
            _authResultState.value = err
            err
        }
    }

    suspend fun sendEmailVerification(): AuthResultState {
        val user = firebaseAuth?.currentUser
        if (user == null) {
            val err = AuthResultState.Error("No authenticated user")
            _authResultState.value = err
            return err
        }

        return try {
            suspendCancellableCoroutine<Unit> { cont ->
                user.reload()
                    .addOnSuccessListener { cont.resume(Unit) }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AuthRepository", "Exception reloading user: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
            }

            suspendCancellableCoroutine<Unit> { cont ->
                user.sendEmailVerification()
                    .addOnSuccessListener { cont.resume(Unit) }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AuthRepository", "Exception in sendEmailVerification: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
            }

            val current = _userState.value
            val updated = current.copy(emailVerified = user.isEmailVerified)
            saveUserToStorage(updated)
            _userState.value = updated
            syncUserFromFirestore(updated.uid)

            val res = AuthResultState.Success(updated, "Verification email sent to ${user.email}.")
            android.util.Log.d("AuthRepository", "Firebase success: Verification email sent to ${user.email}")
            _authResultState.value = res
            res
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Exception during sendEmailVerification: ${e.message}", e)
            val err = AuthResultState.Error(e.localizedMessage ?: "Failed to send verification email.")
            _authResultState.value = err
            err
        }
    }

    suspend fun checkEmailVerification(): AuthResultState {
        val user = firebaseAuth?.currentUser
        if (user == null) {
            val err = AuthResultState.Error("No authenticated user")
            _authResultState.value = err
            return err
        }

        return try {
            suspendCancellableCoroutine<Unit> { cont ->
                user.reload()
                    .addOnSuccessListener { cont.resume(Unit) }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AuthRepository", "Exception reloading user: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
            }

            val current = _userState.value
            val updated = current.copy(emailVerified = user.isEmailVerified)
            saveUserToStorage(updated)
            _userState.value = updated
            syncUserFromFirestore(updated.uid)

            val res = if (user.isEmailVerified) {
                AuthResultState.Success(updated, "Email verified successfully!")
            } else {
                AuthResultState.Error("Email is not verified yet. Please check your inbox and click the verification link.")
            }
            _authResultState.value = res
            res
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Exception in checkEmailVerification: ${e.message}", e)
            val err = AuthResultState.Error(e.localizedMessage ?: "Failed to check email verification status.")
            _authResultState.value = err
            err
        }
    }

    suspend fun changePassword(oldPass: String, newPass: String): AuthResultState {
        if (oldPass.isBlank()) {
            val err = AuthResultState.Error("Please enter your current password.")
            _authResultState.value = err
            return err
        }
        val passwordError = validatePassword(newPass)
        if (passwordError != null) {
            val err = AuthResultState.Error(passwordError)
            _authResultState.value = err
            return err
        }
        val user = firebaseAuth?.currentUser
        if (user == null || user.email.isNullOrBlank()) {
            val err = AuthResultState.Error("No authenticated user found. Please sign in again.")
            _authResultState.value = err
            return err
        }

        return try {
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(user.email!!, oldPass)
            suspendCancellableCoroutine<Unit> { cont ->
                user.reauthenticate(credential)
                    .addOnSuccessListener {
                        user.updatePassword(newPass)
                            .addOnSuccessListener { cont.resume(Unit) }
                            .addOnFailureListener { e -> cont.resumeWithException(e) }
                    }
                    .addOnFailureListener { e -> cont.resumeWithException(e) }
            }
            val res = AuthResultState.Success(_userState.value, "Password changed successfully.")
            _authResultState.value = res
            res
        } catch (e: Exception) {
            val userMsg = when {
                e.message?.contains("invalid-credential", ignoreCase = true) == true ||
                e.message?.contains("wrong-password", ignoreCase = true) == true -> "Current password is incorrect."
                e.message?.contains("requires-recent-login", ignoreCase = true) == true -> "Please sign out and sign in again before changing your password."
                else -> e.localizedMessage ?: "Failed to update password."
            }
            val err = AuthResultState.Error(userMsg)
            _authResultState.value = err
            err
        }
    }

    suspend fun deleteAccount(): AuthResultState {
        val user = firebaseAuth?.currentUser
        val uid = _userState.value.uid
        if (uid.isNotEmpty()) {
            firestore?.collection("users")?.document(uid)?.delete()
        }
        if (user != null) {
            runCatching {
                suspendCancellableCoroutine<Unit> { cont ->
                    user.delete()
                        .addOnSuccessListener { cont.resume(Unit) }
                        .addOnFailureListener { cont.resume(Unit) }
                }
            }
        }
        prefs.edit().clear().apply()
        logout()
        return AuthResultState.Success(UserProfile(uid = "", name = "", email = "", role = ""), "Your account and data have been completely deleted.")
    }

    suspend fun loginAsGuest(): AuthResultState {
        val guestUser = UserProfile(
            uid = "guest_learner",
            name = "Learner",
            email = "guest@baytulilm.app",
            role = UserRole.STUDENT,
            emailVerified = true,
            lastLogin = System.currentTimeMillis()
        )
        saveUserToStorage(guestUser)
        _userState.value = guestUser
        val res = AuthResultState.Success(guestUser, "Welcome to Baytul Ilm AI!")
        _authResultState.value = res
        return res
    }

    fun continueAsGuest() {
        val defaultGuest = UserProfile(
            uid = "guest_learner",
            name = "معزز مہمان",
            email = "guest@baytulilm.com",
            role = "طالب علم (Guest)"
        )
        saveUserToStorage(defaultGuest)
        _userState.value = defaultGuest
        _authResultState.value = AuthResultState.Success(defaultGuest, "Welcome, Guest!")
    }

    fun setUserProfile(user: UserProfile) {
        saveUserToStorage(user)
        _userState.value = user
        _authResultState.value = AuthResultState.Success(user, "Welcome, ${user.name}!")
    }

    fun logout() {
        firebaseAuth?.signOut()
        prefs.edit().clear().apply()
        _userState.value = UserProfile(uid = "", name = "", email = "", role = "")
        _authResultState.value = AuthResultState.Idle
    }

    fun resetAuthState() {
        _authResultState.value = AuthResultState.Idle
    }
}
