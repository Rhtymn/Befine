package com.example.befine.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.Header
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.R

@Composable
fun RepairShopSignUpScreen(
    goToLogin: () -> Unit,
    goToUserRegister: () -> Unit
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
            label = "Repair Shop Name",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_store_24),
                    contentDescription = ""
                )
            }
        )
        InputField(
            label = "Password",
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
        )
        FilledButton(text = "Continue")
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
                    label = "Repair Shop Name",
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_store_24),
                            contentDescription = ""
                        )
                    }
                )
                InputField(
                    label = "Password",
                    icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
                )
                FilledButton(text = "Continue")
                Link(leftText = "Sign up as", rightText = "Regular User")
                Link(
                    leftText = "Joined us before",
                    rightText = "Login",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}