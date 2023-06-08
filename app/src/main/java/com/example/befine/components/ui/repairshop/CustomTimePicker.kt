package com.example.befine.components.ui.repairshop

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    selectedState: (String) -> TimePickerState,
    activeTimePicker: String,
    buttonContainerModifier: Modifier = Modifier
) {
    Text(text = "Select time", modifier = Modifier.padding(bottom = 10.dp))
    val state = selectedState(activeTimePicker)
    TimePicker(state = state)
    Row(modifier = buttonContainerModifier) {
        DialogButton(onClick = { onCancel() }, text = "Cancel")
        DialogButton(onClick = { onConfirm() }, text = "OK")
    }
}