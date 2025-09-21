package com.example.uskudaruniversityapp.dataClass

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val selectedRole: String = "Student",
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordVisible: Boolean = false
)

