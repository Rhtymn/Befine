package com.example.befine.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import com.example.befine.R
import com.example.befine.components.authentication.Header
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.components.ui.FormErrorText
import com.example.befine.components.ui.Logo
import com.example.befine.utils.*

@Composable
fun LoginScreen(
    navigateToUserRegister: () -> Unit,
    navigateToRegularHome: () -> Unit,
    navigateToRepairShopHome: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory())
) {
    val state: LoginState by loginViewModel.state.observeAsState(LoginState())

    // Context
    val context = LocalContext.current

    fun loginHandler() {
        loginViewModel.loginHandler(
            onSuccess = {
                Log.d("LOGIN", it)
                if (it == ROLE.CLIENT) {
                    navigateToRegularHome()
                } else if (it == ROLE.REPAIR_SHOP_OWNER) {
                    navigateToRepairShopHome()
                }
            },
            onFailure = {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        Modifier
            .padding(
                horizontal = Screen.paddingHorizontal,
                vertical = Screen.paddingVertical
            )
            .fillMaxHeight(), verticalArrangement = Arrangement.Center,
    ) {
        Logo()
        Header(text = "Login")
        InputField(
            value = state.email,
            onValueChange = { loginViewModel.onChangeEmailInput(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.isEmailError,
            errorMessage = state.emailErrorMsg,
            label = "Email",
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            value = state.password,
            onValueChange = { loginViewModel.onChangePasswordInput(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = "Password",
            isError = state.isPasswordError,
            errorMessage = state.passwordErrorMsg,
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
        )
        if (state.isFailed) {
            FormErrorText(errorMsg = state.formErrorMsg)
        }
        FilledButton(text = "Login", isLoading = state.isLoading, onClick = { loginHandler() })
        Link(leftText = "New to Befine?", rightText = "Register", linkTo = navigateToUserRegister)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LoginScreen(
                navigateToUserRegister = { /*TODO*/ },
                navigateToRegularHome = { /*TODO*/ },
                navigateToRepairShopHome = { /*TODO*/ })
        }
    }
}