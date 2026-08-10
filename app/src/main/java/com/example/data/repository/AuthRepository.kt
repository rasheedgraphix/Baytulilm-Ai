package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthResultState {
    object Idle : AuthResultState()
    object Loading : AuthResultState()
    data class Success(val user: UserProfile, val message: String) : AuthResultState()
    data class Error(val errorMessage: String) : AuthResultState()
}

class AuthRepository(context: Context) {

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
        } else if (savedUid != null) {
            val user = loadUserFromStorage()
            _userState.value = user
            syncUserFromFirestore(savedUid)
        } else {
            _userState.value = UserProfile(uid = "", name = "", email = "", role = "")
        }
    }

    private fun syncUserFromFirestore(uid: String) {
        val db = firestore ?: return
        db.collection("users").document(uid).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val remoteUser = snapshot.toObject(UserProfile::class.java)
                if (remoteUser != null) {
                    _userState.value = remoteUser
                    saveUserToStorage(remoteUser)
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

        firestore?.collection("users")?.document(user.uid)?.set(user, SetOptions.merge())
    }

    suspend fun loginWithEmail(email: String, pass: String, rememberMe: Boolean = true): AuthResultState {
        android.util.Log.d("AuthRepository", "Button clicked: Sign In with email=$email")
        _authResultState.value = AuthResultState.Loading
        android.util.Log.d("AuthRepository", "Current AuthState: Loading")
        if (email.isBlank() || pass.isBlank()) {
            val err = AuthResultState.Error("Please enter valid email and password.")
            android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
            _authResultState.value = err
            return err
        }

        android.util.Log.d("AuthRepository", "Firebase request started: signInWithEmailAndPassword")
        val fbAuth = firebaseAuth
        if (fbAuth != null) {
            try {
                val authResult: AuthResult = suspendCancellableCoroutine { cont ->
                    fbAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener { result -> cont.resume(result) }
                        .addOnFailureListener { e ->
                            android.util.Log.e("AuthRepository", "Exception message: ${e.message}", e)
                            cont.resumeWithException(e)
                        }
                }
                val fbUser = authResult.user
                if (fbUser != null) {
                    suspendCancellableCoroutine<Unit> { cont ->
                        fbUser.reload()
                            .addOnSuccessListener { cont.resume(Unit) }
                            .addOnFailureListener { cont.resume(Unit) }
                    }

                    val role = when {
                        email.contains("superadmin", ignoreCase = true) -> UserRole.SUPER_ADMIN
                        email.contains("admin", ignoreCase = true) -> UserRole.ADMIN
                        email.contains("teacher", ignoreCase = true) -> UserRole.TEACHER
                        else -> UserRole.STUDENT
                    }
                    val user = UserProfile(
                        uid = fbUser.uid,
                        name = fbUser.displayName ?: email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() },
                        email = fbUser.email ?: email,
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
                    return res
                } else {
                    val err = AuthResultState.Error("Authentication failed: User profile is null.")
                    android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
                    _authResultState.value = err
                    return err
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthRepository", "Exception message during signInWithEmailAndPassword: ${e.message}", e)
                val err = AuthResultState.Error(e.localizedMessage ?: "Authentication failed.")
                android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
                _authResultState.value = err
                return err
            }
        } else {
            val err = AuthResultState.Error("Firebase Auth is not initialized.")
            android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
            _authResultState.value = err
            return err
        }
    }

    suspend fun registerWithEmail(
        fullName: String,
        email: String,
        phone: String,
        country: String,
        province: String,
        city: String,
        pass: String,
        role: String
    ): AuthResultState {
        android.util.Log.d("AuthRepository", "Button clicked: Register with email=$email")
        _authResultState.value = AuthResultState.Loading
        android.util.Log.d("AuthRepository", "Current AuthState: Loading")
        if (fullName.isBlank() || email.isBlank() || pass.isBlank()) {
            val err = AuthResultState.Error("Please fill in all mandatory registration fields.")
            android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
            _authResultState.value = err
            return err
        }

        android.util.Log.d("AuthRepository", "Firebase request started: createUserWithEmailAndPassword")
        val fbAuth = firebaseAuth
        if (fbAuth != null) {
            try {
                val authResult: AuthResult = suspendCancellableCoroutine { cont ->
                    fbAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener { result -> cont.resume(result) }
                        .addOnFailureListener { e ->
                            android.util.Log.e("AuthRepository", "Exception message: ${e.message}", e)
                            cont.resumeWithException(e)
                        }
                }
                val fbUser = authResult.user
                if (fbUser != null) {
                    try {
                        suspendCancellableCoroutine<Unit> { cont ->
                            fbUser.sendEmailVerification()
                                .addOnSuccessListener { cont.resume(Unit) }
                                .addOnFailureListener { e ->
                                    android.util.Log.e("AuthRepository", "Failed to send verification email: ${e.message}", e)
                                    cont.resumeWithException(e)
                                }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthRepository", "Exception during sendEmailVerification: ${e.message}", e)
                        val err = AuthResultState.Error(e.localizedMessage ?: "Failed to send verification email.")
                        android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
                        _authResultState.value = err
                        return err
                    }

                    suspendCancellableCoroutine<Unit> { cont ->
                        fbUser.reload()
                            .addOnSuccessListener { cont.resume(Unit) }
                            .addOnFailureListener { cont.resume(Unit) }
                    }

                    val newUser = UserProfile(
                        uid = fbUser.uid,
                        name = fullName,
                        email = fbUser.email ?: email,
                        phone = phone,
                        country = country.ifBlank { "Pakistan" },
                        province = province.ifBlank { "Punjab" },
                        city = city.ifBlank { "Lahore" },
                        role = role,
                        emailVerified = fbUser.isEmailVerified,
                        createdAt = System.currentTimeMillis(),
                        lastLogin = System.currentTimeMillis()
                    )

                    saveUserToStorage(newUser)
                    _userState.value = newUser
                    syncUserFromFirestore(newUser.uid)

                    val res = AuthResultState.Success(newUser, "Account created! A verification link has been sent to $email.")
                    android.util.Log.d("AuthRepository", "Firebase success: Created account ${newUser.email}")
                    _authResultState.value = res
                    return res
                } else {
                    val err = AuthResultState.Error("Registration failed: User profile is null.")
                    android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
                    _authResultState.value = err
                    return err
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthRepository", "Exception message during createUserWithEmailAndPassword: ${e.message}", e)
                val err = AuthResultState.Error(e.localizedMessage ?: "Registration failed.")
                android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
                _authResultState.value = err
                return err
            }
        } else {
            val err = AuthResultState.Error("Firebase Auth is not initialized.")
            android.util.Log.d("AuthRepository", "Firebase failure: ${err.errorMessage}")
            _authResultState.value = err
            return err
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
        if (email.isBlank() || !email.contains("@")) {
            return AuthResultState.Error("Please enter a valid email address.")
        }
        firebaseAuth?.sendPasswordResetEmail(email)
        return AuthResultState.Success(_userState.value, "Password reset instructions sent to $email. Please check your email.")
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
        if (oldPass.isBlank() || newPass.length < 6) {
            return AuthResultState.Error("New password must be at least 6 characters.")
        }
        return AuthResultState.Success(_userState.value, "Password changed successfully.")
    }

    suspend fun deleteAccount(): AuthResultState {
        val uid = _userState.value.uid
        firestore?.collection("users")?.document(uid)?.delete()
        prefs.edit().clear().apply()
        logout()
        return AuthResultState.Success(_userState.value, "Your account and data have been completely deleted.")
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
