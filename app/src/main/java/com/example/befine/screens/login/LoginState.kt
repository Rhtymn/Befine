package com.example.befine.screens.login

data class LoginState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorMsg: String = "",

    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordErrorMsg: String = "",

    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
    val formErrorMsg: String = ""
)