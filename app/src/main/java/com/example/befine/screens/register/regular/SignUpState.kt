package com.example.befine.screens.register.regular

data class SignUpState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorMsg: String = "",

    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordErrorMsg: String = "",

    val fullName: String = "",
    val isFullNameError: Boolean = false,
    val fullNameErrorMsg: String = "",

    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
    val formErrorMsg: String = ""
)