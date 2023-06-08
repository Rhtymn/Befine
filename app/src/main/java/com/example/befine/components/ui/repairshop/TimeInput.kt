package com.example.befine.components.ui.repairshop

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.befine.components.authentication.InputField

@Composable
fun TimeInput(
    value: String,
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    InputField(
        value = value,
        onValueChange = { },
        label = label,
        modifier = modifier
            .padding(end = 8.dp),
        readOnly = true,
        enabled = enabled,
        interactionSource = remember {
            MutableInteractionSource()
        }.also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        onClick()
                    }
                }
            }
        }
    )
}