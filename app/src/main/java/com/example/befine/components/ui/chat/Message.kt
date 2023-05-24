package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Message(value: String, modifier: Modifier = Modifier) {
    Text(
        text = value,
        fontSize = 12.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier.padding(end = 8.dp)
    )
}
