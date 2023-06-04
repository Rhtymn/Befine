package com.example.befine.screens

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
import com.example.befine.utils.Screen

@Composable
fun SignUpScreen(
    goToLogin: () -> Unit,
    goToRepairShopRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = "Email",
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full name",
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
        )
        InputField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Password",
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
        )
        FilledButton(text = "Continue")
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