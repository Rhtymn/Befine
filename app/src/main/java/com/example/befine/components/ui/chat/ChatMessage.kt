package com.example.befine.components.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatMessage() {
    Column(
        Modifier
            .widthIn(min = 50.dp, max = 280.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Hi",
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = "12.12",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 6.dp, bottom = 4.dp),
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}