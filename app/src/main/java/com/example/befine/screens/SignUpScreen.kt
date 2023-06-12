package com.example.befine.screens

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.Header
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.components.ui.FormErrorText
import com.example.befine.firebase.Auth
import com.example.befine.model.ROLE.REGULAR_USER
import com.example.befine.model.UserData
import com.example.befine.repository.UsersRepository
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun SignUpScreen(
    navigateToLogin: () -> Unit,
    navigateToRepairShopRegister: () -> Unit,
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    usersRepository: UsersRepository = UsersRepository.getInstance()
) {
    // Context
    val context = LocalContext.current

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

        fullName = ""
        isFullNameError = false
        fullNameErrorMsg = ""
    }

    val signUpHandler: () -> Unit = {
        try {
            isLoading = true

            // Fullname validation
            inputFieldValidation(fullName, callbackWhenEmpty = {
                isFullNameError = true
                fullNameErrorMsg = InputFieldError.REQUIRED
            })

            emailValidation(email, callbackWhenEmpty = {
                isEmailError = true
                emailErrorMsg = InputFieldError.REQUIRED
            }, callbackWhenInvalidFormat = {
                isEmailError = true
                emailErrorMsg = EmailError.INVALID_FORMAT
            })

            passwordValidation(password, callbackWhenEmpty = {
                isPasswordError = true
                passwordErrorMsg = InputFieldError.REQUIRED
            }, callbackWhenLessThanEightChar = {
                isPasswordError = true
                passwordErrorMsg = PasswordError.MIN_CHARS
            })

            // Checking error availability
            if (!isEmailError && !isPasswordError && !isFullNameError) {
                Log.d("Sign Up Screen", "$email $fullName $password")

                // Sign up process
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            if (auth.currentUser != null) {
                                val user = UserData(
                                    name = fullName,
                                    role = ROLE.CLIENT
                                )

                                // Create users in firestore database
                                usersRepository.createUser(auth.currentUser!!.uid, user) {
                                    // Callback when failed
                                    auth.currentUser!!.delete()
                                    isFailed = true
                                    formErrorMsg = SignUpError.FAILED
                                }
                            }
                            resetInputField()

                            Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT)
                                .show()

                            @Suppress("DEPRECATION")
                            Handler().postDelayed(
                                {
                                    navigateToLogin()
                                }, 2000
                            )

                        } else {
                            // If sign in fails, display a message to the user.
                            isFailed = true
                            formErrorMsg =
                                if (task.exception is FirebaseAuthException) {
                                    SignUpError.EMAIL_IN_USE
                                } else {
                                    SignUpError.DEFAULT
                                }
                        }
                        isLoading = false
                    }
            }
        } catch (e: Exception) {
            Log.d("Sign Up Screen", e.message.toString())
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

    fun onChangeFullNameField(value: String) {
        if (isFullNameError) isFullNameError = false
        if (isFailed) resetFormState()
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
        if (isFailed) {
            FormErrorText(errorMsg = formErrorMsg)
        }
        FilledButton(text = "Continue", isLoading = isLoading, onClick = { signUpHandler() })
        Link(
            leftText = "Sign up as",
            rightText = "Repair Shop Owner",
            linkTo = navigateToRepairShopRegister
        )
        Link(
            leftText = "Joined us before?",
            rightText = "Login",
            modifier = Modifier.padding(top = 16.dp),
            linkTo = navigateToLogin
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
            SignUpScreen(navigateToLogin = { /*TODO*/ }, navigateToRepairShopRegister = { /*TODO*/ })
        }
    }
}