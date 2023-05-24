package com.example.befine.components.ui.chat

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun Datetime(value: String) {
    Text(text = value, fontSize = 10.sp)
}