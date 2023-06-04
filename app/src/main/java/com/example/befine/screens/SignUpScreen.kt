package com.example.befine.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.Header
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*

@Composable
fun SignUpScreen(
    goToLogin: () -> Unit,
    goToRepairShopRegister: () -> Unit
) {
    // Email Input Field related state
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var emailErrorMsg by remember { mutableStateOf("") }

    // Fullname Input Field related state
    var fullName by remember { mutableStateOf("") }
    var isFullNameError by remember { mutableStateOf(false) }
    var fullNameErrorMsg by remember { mutableStateOf("") }

    // Password Input Field related state
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMsg by remember { mutableStateOf("") }

    // Progress indicator state
    var isLoading by remember { mutableStateOf(false) }

    val signUpHandler: () -> Unit = {
        try {
            isLoading = true

            // Fullname validation
            inputFieldValidation(fullName, callbackWhenEmpty = {
                isFullNameError = true
                fullNameErrorMsg = INPUT_FIELD_ERROR.REQUIRED
            })

            emailValidation(email, callbackWhenEmpty = {
                isEmailError = true
                emailErrorMsg = INPUT_FIELD_ERROR.REQUIRED
            }, callbackWhenInvalidFormat = {
                isEmailError = true
                emailErrorMsg = EMAIL_ERROR.INVALID_FORMAT
            })

            passwordValidation(password, callbackWhenEmpty = {
                isPasswordError = true
                passwordErrorMsg = INPUT_FIELD_ERROR.REQUIRED
            }, callbackWhenLessThanEightChar = {
                isPasswordError = true
                passwordErrorMsg = PASSWORD_ERROR.MIN_CHARS
            })

            // Checking error availability
            if (!isEmailError && !isPasswordError && !isFullNameError) {
                Log.d("Sign Up Screen", "$email $fullName $password")
            }
        } catch (e: Exception) {
            Log.d("Sign Up Screen", e.message.toString())
        } finally {
            isLoading = false
        }
    }

    fun onChangeEmailField(value: String) {
        if (isEmailError) isEmailError = false
        email = value
    }

    fun onChangePasswordField(value: String) {
        if (isPasswordError) isPasswordError = false
        password = value
    }

    fun onChangeFullNameField(value: String) {
        if (isFullNameError) isFullNameError = false
        fullName = value
    }

    Column(
        Modifier
            .padding(
                horizontal = Screen.paddingHorizontal,
                vertical = Screen.paddingVertical
            )
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Header(text = "Sign Up")
        InputField(
            value = email,
            onValueChange = { onChangeEmailField(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = "Email",
            isError = isEmailError,
            errorMessage = emailErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            value = fullName,
            onValueChange = { onChangeFullNameField(it) },
            label = "Full name",
            isError = isFullNameError,
            errorMessage = fullNameErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
        )
        InputField(
            value = password,
            onValueChange = { onChangePasswordField(it) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Password",
            isError = isPasswordError,
            errorMessage = passwordErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
        )
        FilledButton(text = "Continue", isLoading = isLoading, onClick = signUpHandler)
        Link(
            leftText = "Sign up as",
            rightText = "Repair Shop Owner",
            linkTo = goToRepairShopRegister
        )
        Link(
            leftText = "Joined us before?",
            rightText = "Login",
            modifier = Modifier.padding(top = 16.dp),
            linkTo = goToLogin
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SignUpScreen(goToLogin = { /*TODO*/ }) {

            }
        }
    }
}