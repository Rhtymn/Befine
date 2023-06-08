package com.example.befine.components.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SupportingText(errorMessage: String) {
    Text(
        text = errorMessage,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String = "",
    label: String,
    icon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    if (icon != null) {
        if (isError) {
            OutlinedTextField(
                value = value,
                isError = true,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                supportingText = { SupportingText(errorMessage = errorMessage) },
                leadingIcon = { icon() },
                label = { Text(label) },
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource
            )
        } else {
            OutlinedTextField(
                value = value,
                isError = false,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                leadingIcon = { icon() },
                label = { Text(label) },
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource
            )
        }
    } else {
        if (isError) {
            OutlinedTextField(
                value = value,
                isError = true,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                supportingText = { SupportingText(errorMessage = errorMessage) },
                label = { Text(label) },
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource
            )
        } else {
            OutlinedTextField(
                value = value,
                isError = false,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = { Text(label) },
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource
            )
        }
    }
}