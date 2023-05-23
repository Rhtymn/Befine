package com.example.befine.components.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp)
    )
}