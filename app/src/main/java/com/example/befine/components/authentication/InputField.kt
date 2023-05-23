package com.example.befine.components.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    label: String,
    icon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    if (icon != null) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            leadingIcon = { icon() },
            label = { Text(label) }
        )
    }
}