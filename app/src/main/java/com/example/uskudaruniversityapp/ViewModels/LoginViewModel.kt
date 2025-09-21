package com.example.uskudaruniversityapp.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uskudaruniversityapp.dataClass.LoginUiState
import java.util.Locale

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null)
    }

    fun onRoleChange(role: String) {
        uiState = uiState.copy(selectedRole = role, emailError = null)
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(passwordVisible = !uiState.passwordVisible)
    }

    fun validate(): Boolean {
        val emailError = validateEmail(uiState.email, uiState.selectedRole)
        val passwordError = if (uiState.password.isBlank()) "Password is required" else null

        uiState = uiState.copy(emailError = emailError, passwordError = passwordError)

        return emailError == null && passwordError == null
    }


    private fun validateEmail(email: String, role: String): String? {
        val domain = if (role == "Student") "@st.uskudar.edu.tr" else "@uskudar.edu.tr"
        if (!email.endsWith(domain)) {
            return "Email must end with $domain"
        }

        val local = email.removeSuffix(domain)
        val valid = local.lowercase(Locale.ROOT).matches(Regex("^[a-zA-Z0-9.]+\$"))

        return when {
            !valid -> "Special Character are not allowed!"
            local.isBlank() -> "Email username is empty"
            else -> null
        }
    }
}
