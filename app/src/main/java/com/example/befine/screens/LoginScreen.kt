package com.example.befine.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.befine.components.authentication.Header
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.authentication.InputField
import com.example.befine.components.authentication.Link

@Composable
fun LoginScreen() {
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(Modifier.padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical)) {
                Header(text = "Login")
                InputField(
                    label = "Email",
                    icon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "") }
                )
                InputField(
                    label = "Password",
                    icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "") }
                )
                FilledButton(text = "Login")
                Link(leftText = "New to Befine?", rightText = "Register")
            }
        }
    }
}