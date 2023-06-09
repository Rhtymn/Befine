package com.example.befine.components.ui.repairshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DayPicker(text: String, active: Boolean = false, onClick: () -> Unit) {
    val backgroundColor = if (active) MaterialTheme.colorScheme.primary else Color.White
    val textColor = if (active) Color.White else MaterialTheme.colorScheme.primary
    val baseModifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(backgroundColor)
    var modifier = baseModifier
    if (!active) {
        modifier = baseModifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
    }

    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        Text(text = text, modifier = Modifier.align(Alignment.Center), color = textColor)
    }
}