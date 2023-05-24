package com.example.befine.screens.chat.channel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.befine.components.ui.chat.ChatBox
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen

@Composable
fun ChannelScreen() {
}

@Preview
@Composable
fun ChannelScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Screen.paddingHorizontal,
                    vertical = Screen.paddingVertical
                )
            ) {
                ChatBox(
                    name = "Andy Machisa",
                    datetime = "09.25",
                    message = "Halo, ada yang bisa kami bantu?"
                )
            }
        }
    }
}