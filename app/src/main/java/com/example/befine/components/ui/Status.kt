package com.example.befine.components.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.R

object STATUS {
    const val OPEN = "open"
    const val CLOSED = "closed"
}

@Composable
fun Status(status: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_schedule_24),
            contentDescription = "",
            modifier = Modifier.width(12.dp),
            tint = if (status == STATUS.CLOSED) Color.Red else Color.Green
        )
        Text(
            text = if (status == STATUS.OPEN) "Open" else "Closed",
            fontSize = 9.sp,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}