package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.UserProfile
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthResultState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    val userState: StateFlow<UserProfile> = authRepository.userState
    val authResult: StateFlow<AuthResultState> = authRepository.authResultState

    fun loginWithEmail(email: String, pass: String, rememberMe: Boolean = true) {
        viewModelScope.launch {
            authRepository.loginWithEmail(email, pass, rememberMe)
        }
    }

    fun registerWithEmail(
        fullName: String,
        email: String,
        phone: String,
        country: String,
        province: String,
        city: String,
        pass: String,
        role: String
    ) {
        viewModelScope.launch {
            authRepository.registerWithEmail(fullName, email, phone, country, province, city, pass, role)
        }
    }

    fun loginWithGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            authRepository.loginWithGoogleIdToken(idToken)
        }
    }

    fun loginWithGoogle(accountName: String, accountEmail: String, idToken: String? = null) {
        viewModelScope.launch {
            authRepository.loginWithGoogle(accountName, accountEmail, idToken)
        }
    }

    fun setAuthError(errorMessage: String) {
        authRepository.setAuthError(errorMessage)
    }

    fun setAuthLoading() {
        authRepository.setAuthLoading()
    }

    fun loginWithFacebook(accountName: String, accountEmail: String) {
        viewModelScope.launch {
            authRepository.loginWithFacebook(accountName, accountEmail)
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        country: String,
        province: String,
        city: String,
        language: String,
        theme: String
    ) {
        viewModelScope.launch {
            authRepository.updateProfile(name, phone, country, province, city, language, theme)
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            authRepository.sendPasswordReset(email)
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            authRepository.sendEmailVerification()
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            authRepository.checkEmailVerification()
        }
    }

    fun changePassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            authRepository.changePassword(oldPass, newPass)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun resetState() {
        authRepository.resetAuthState()
    }
}

