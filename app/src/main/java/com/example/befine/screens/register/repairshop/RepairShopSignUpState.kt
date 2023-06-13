package com.example.befine.screens.register.repairshop

data class RepairShopSignUpState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorMsg: String = "",

    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordErrorMsg: String = "",

    val repairShopName: String = "",
    val isRepairShopNameError: Boolean = false,
    val repairShopNameErrorMsg: String = "",

    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
    val formErrorMsg: String = ""
)