package com.example.befine.components.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val actionButtonIconModifier = Modifier
    .width(36.dp)
    .height(36.dp)

@Composable
fun ActionButton(icon: @Composable (() -> Unit), text: String, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(70.dp)
            .clickable { onClick() }
    ) {
        icon()
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 20.dp)
                .weight(1f),
            fontSize = 18.sp
        )
    }
}