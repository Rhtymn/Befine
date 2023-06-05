package com.example.befine.screens

import android.app.Activity
import android.os.Handler
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.Header
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.R
import com.example.befine.components.ui.FormErrorText
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RepairShopSignUpScreen(
    goToLogin: () -> Unit,
    goToUserRegister: () -> Unit,
    auth: FirebaseAuth = Firebase.auth
) {
    // Context
    val context = LocalContext.current

    // Email Input Field related state
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var emailErrorMsg by remember { mutableStateOf("") }

    // Repair shop name Input Field related state
    var repairShopName by remember { mutableStateOf("") }
    var isRepairShopNameError by remember { mutableStateOf(false) }
    var repairShopErrorMsg by remember { mutableStateOf("") }

    // Password Input Field related state
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMsg by remember { mutableStateOf("") }

    // Form related state
    var isFailed by remember { mutableStateOf(false) }
    var formErrorMsg by remember { mutableStateOf("") }

    // Progress indicator state
    var isLoading by remember { mutableStateOf(false) }

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

        repairShopName = ""
        isRepairShopNameError = false
        repairShopErrorMsg = ""
    }

    val signUpHandler: () -> Unit = {
        try {
            isLoading = true

            // Repair shop name validation
            inputFieldValidation(repairShopName, callbackWhenEmpty = {
                isRepairShopNameError = true
                repairShopErrorMsg = INPUT_FIELD_ERROR.REQUIRED
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

            if (!isEmailError && !isPasswordError && !isRepairShopNameError) {
                Log.d("Repair Shop Sign Up", "$email $repairShopName $password")

                // SIgn up process
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            resetInputField()

                            Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT)
                                .show()

                            @Suppress("DEPRECATION")
                            Handler().postDelayed(
                                {
                                    goToLogin()
                                }, 2000
                            )
                        } else {
                            // If sign in fails, display a message to the user.
                            isFailed = true
                            formErrorMsg =
                                if (task.exception is FirebaseAuthException) {
                                    "Email already in use"
                                } else {
                                    "Server is down, please try again later"
                                }
                        }
                        isLoading = false
                    }
            }
        } catch (e: Exception) {
            Log.d("Repair Shop Sign Up", e.message.toString())
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

    fun onChangeRepairShopName(value: String) {
        if (isRepairShopNameError) isRepairShopNameError = false
        if (isFailed) resetFormState()
        repairShopName = value
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
            value = repairShopName,
            onValueChange = { onChangeRepairShopName(it) },
            label = "Repair Shop Name",
            isError = isRepairShopNameError,
            errorMessage = repairShopErrorMsg,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_store_24),
                    contentDescription = ""
                )
            }
        )
        InputField(
            value = password,
            onValueChange = { onChangePasswordField(it) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Password",
            isError = isPasswordError,
            errorMessage = passwordErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
        )
        if (isFailed) {
            FormErrorText(errorMsg = formErrorMsg)
        }
        FilledButton(text = "Continue", isLoading = isLoading, onClick = signUpHandler)
        Link(leftText = "Sign up as", rightText = "Regular User", linkTo = goToUserRegister)
        Link(
            leftText = "Joined us before",
            rightText = "Login",
            modifier = Modifier.padding(top = 16.dp),
            linkTo = goToLogin
        )
    }
}

@Preview
@Composable
fun RepairShopSignUpScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            RepairShopSignUpScreen(goToLogin = { /*TODO*/ }, goToUserRegister = { /*TODO*/ })
        }
    }
}