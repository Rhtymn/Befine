package com.example.befine.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.befine.components.authentication.Header
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.components.ui.FormErrorText
import com.example.befine.firebase.Auth
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun LoginScreen(
    goToUserRegister: () -> Unit,
    auth: FirebaseAuth = Auth.getInstance().getAuth()
) {
    // Context
    val context = LocalContext.current

    // Email Input Field related state
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var emailErrorMsg by remember { mutableStateOf("") }

    // Password Input Field related state
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMsg by remember { mutableStateOf("") }

    // Progress Indicator state
    var isLoading by remember { mutableStateOf(false) }

    // Form related state
    var isFailed by remember { mutableStateOf(false) }
    var formErrorMsg by remember { mutableStateOf("") }

    fun resetFormState() {
        isFailed = false
        formErrorMsg = ""
    }

    fun resetInputField() {
        email = ""
        isEmailError = false
        emailErrorMsg = ""

        password = ""
        isPasswordError = false
        passwordErrorMsg = ""
    }

    val loginHandler: () -> Unit = {
        try {
            isLoading = true

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
            if (!isEmailError && !isPasswordError) {

                // Sign in process
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            resetInputField()

                            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            // If sign in fails, display a message to the user
                            isFailed = true
                            formErrorMsg =
                                if (task.exception is FirebaseAuthInvalidUserException || task.exception is FirebaseAuthInvalidCredentialsException) {
                                    "Invalid email or password"
                                } else {
                                    "Server is down, please try again later"
                                }
                        }
                        isLoading = false
                    }

            }
        } catch (e: Exception) {
            Log.d("LOGIN", e.message.toString())
        }
    }

    fun onChangeEmailField(value: String) {
        if (isEmailError) isEmailError = false
        if (isFailed) resetFormState()
        email = value
    }

    fun onChangePasswordField(value: String) {
        if (isPasswordError) isPasswordError = false
        if (isFailed) resetFormState()
        password = value
    }

    Column(
        Modifier
            .padding(
                horizontal = Screen.paddingHorizontal,
                vertical = Screen.paddingVertical
            )
            .fillMaxHeight(), verticalArrangement = Arrangement.Center
    ) {
        Header(text = "Login")
        InputField(
            value = email,
            onValueChange = { onChangeEmailField(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isEmailError,
            errorMessage = emailErrorMsg,
            label = "Email",
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            value = password,
            onValueChange = { onChangePasswordField(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = "Password",
            isError = isPasswordError,
            errorMessage = passwordErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
        )
        if (isFailed) {
            FormErrorText(errorMsg = formErrorMsg)
        }
        FilledButton(text = "Login", isLoading = isLoading, onClick = loginHandler)
        Link(leftText = "New to Befine?", rightText = "Register", linkTo = goToUserRegister)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LoginScreen(goToUserRegister = { /*TODO*/ })
        }
    }
}