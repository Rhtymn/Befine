package com.example.befine.utils

import androidx.compose.ui.unit.dp

object STATUS {
    const val OPEN = "open"
    const val CLOSED = "closed"
}

object Screen {
    val paddingVertical = 16.dp
    val paddingHorizontal = 16.dp
}

object ROLE {
    const val CLIENT = "client"
    const val REPAIR_SHOP_OWNER = "repair_shop_owner"
}

object INPUT_FIELD_ERROR {
    const val REQUIRED = "Required"
}

object EMAIL_ERROR {
    const val INVALID_FORMAT = "Invalid email format"
}

object PASSWORD_ERROR {
    const val MIN_CHARS = "Minimum 8 characters"
}

fun inputFieldValidation(value: String, callbackWhenEmpty: () -> Unit) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    }
}

fun isValidEmail(value: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

fun emailValidation(
    value: String,
    callbackWhenEmpty: () -> Unit,
    callbackWhenInvalidFormat: () -> Unit
) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    } else if (!isValidEmail(value)) {
        callbackWhenInvalidFormat()
    }
}

fun passwordValidation(
    value: String,
    callbackWhenEmpty: () -> Unit,
    callbackWhenLessThanEightChar: () -> Unit
) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    } else if (value.length < 8) {
        callbackWhenLessThanEightChar()
    }
}