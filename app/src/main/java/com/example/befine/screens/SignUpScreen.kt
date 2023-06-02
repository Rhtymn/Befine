package com.example.befine.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            label = "Email",
            icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
        )
        InputField(
            label = "Full name",
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
        )
        InputField(
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
                    label = "Email",
                    icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
                )
                InputField(
                    label = "Full name",
                    icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
                )
                InputField(
                    label = "Password",
                    icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "") }
                )
                FilledButton(text = "Continue")
                Link(leftText = "Sign up as", rightText = "Repair Shop Owner")
                Link(
                    leftText = "Joined us before?",
                    rightText = "Login",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}