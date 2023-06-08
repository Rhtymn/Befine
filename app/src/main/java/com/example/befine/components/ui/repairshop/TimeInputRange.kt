package com.example.befine.components.ui.repairshop

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeInputRange(
    label: String,
    onPickStartTime: () -> Unit,
    onPickEndTime: () -> Unit,
    startValue: String,
    endValue: String
) {
    Text(text = label, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
    Row() {
        TimeInput(value = startValue, modifier = Modifier.weight(1f), label = "Start") {
            onPickStartTime()
        }
        TimeInput(value = endValue, modifier = Modifier.weight(1f), label = "End") {
            onPickEndTime()
        }
    }
}