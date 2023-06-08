package com.example.befine.components.ui.repairshop

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DialogButton(onClick: () -> Unit, text: String) {
    TextButton(onClick = onClick) {
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}