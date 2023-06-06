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
import com.example.befine.model.RepairShop
import com.example.befine.firebase.Auth
import com.example.befine.model.UserData
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.example.befine.model.ROLE.REPAIR_SHOP_OWNER
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RepairShopSignUpScreen(
    navigateToLogin: () -> Unit,
    navigateToUserRegister: () -> Unit,
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    db: FirebaseFirestore = Firebase.firestore
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
                repairShopErrorMsg = InputFieldError.REQUIRED
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

            if (!isEmailError && !isPasswordError && !isRepairShopNameError) {
                Log.d("Repair Shop Sign Up", "$email $repairShopName $password")

                // SIgn up process
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            if (auth.currentUser != null) {
                                val user = UserData(
                                    name = repairShopName,
                                    role = REPAIR_SHOP_OWNER.name
                                )
                                val repairShopData = RepairShop(
                                    userId = auth.currentUser!!.uid,
                                    name = repairShopName,
                                    photo = "default.jpg"
                                )

                                val usersRef =
                                    db.collection("users").document(auth.currentUser!!.uid)
                                val repairShopsRef = db.collection("repairShops")

                                db.runBatch {
                                    // Create users in firestore database
                                    usersRef.set(user)

                                    // Create repairShops in firestore data
                                    repairShopsRef.add(repairShopData)
                                }.addOnFailureListener {
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
        Link(leftText = "Sign up as", rightText = "Regular User", linkTo = navigateToUserRegister)
        Link(
            leftText = "Joined us before",
            rightText = "Login",
            modifier = Modifier.padding(top = 16.dp),
            linkTo = navigateToLogin
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
            RepairShopSignUpScreen(navigateToLogin = { /*TODO*/ }, navigateToUserRegister = { /*TODO*/ })
        }
    }
}