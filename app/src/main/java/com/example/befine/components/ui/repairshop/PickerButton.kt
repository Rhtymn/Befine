package com.example.befine.components.ui.repairshop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PickerButton(text: String, icon: @Composable (() -> Unit)? = null) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(50.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (icon != null) {
                icon()
            }
            Text(text = text, color = Color.Black)
        }
    }
}