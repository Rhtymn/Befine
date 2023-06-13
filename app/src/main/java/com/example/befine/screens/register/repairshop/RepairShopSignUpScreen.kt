package com.example.befine.screens.register.repairshop

import android.os.Handler
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.Header
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.R
import com.example.befine.components.ui.FormErrorText
import com.example.befine.components.ui.Logo
import com.example.befine.utils.*

@Composable
fun RepairShopSignUpScreen(
    navigateToLogin: () -> Unit,
    navigateToUserRegister: () -> Unit,
    repairShopSignUpViewModel: RepairShopSignUpViewModel = viewModel(factory = ViewModelFactory())
) {
    // Context
    val context = LocalContext.current
    val state: RepairShopSignUpState by repairShopSignUpViewModel.state.observeAsState(
        RepairShopSignUpState()
    )

    fun signUpHandler() {
        repairShopSignUpViewModel.signUpHandler {
            Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT)
                .show()

            @Suppress("DEPRECATION")
            Handler().postDelayed(
                {
                    navigateToLogin()
                }, 2000
            )
        }
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
        Logo()
        Header(text = "Sign Up")
        InputField(
            value = state.email,
            onValueChange = { repairShopSignUpViewModel.onChangeEmailField(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = "Email",
            isError = state.isEmailError,
            errorMessage = state.emailErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            value = state.repairShopName,
            onValueChange = { repairShopSignUpViewModel.onChangeRepairShopNameField(it) },
            label = "Repair Shop Name",
            isError = state.isRepairShopNameError,
            errorMessage = state.repairShopNameErrorMsg,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_store_24),
                    contentDescription = ""
                )
            }
        )
        InputField(
            value = state.password,
            onValueChange = { repairShopSignUpViewModel.onChangePasswordField(it) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Password",
            isError = state.isPasswordError,
            errorMessage = state.passwordErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
        )
        if (state.isFailed) {
            FormErrorText(errorMsg = state.formErrorMsg)
        }
        FilledButton(text = "Continue", isLoading = state.isLoading, onClick = { signUpHandler() })
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
            RepairShopSignUpScreen(
                navigateToLogin = { /*TODO*/ },
                navigateToUserRegister = { /*TODO*/ })
        }
    }
}

