package com.example.befine.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.R
import com.example.befine.utils.STATUS

@Composable
fun Status(status: String, modifier: Modifier = Modifier) {
    val backgroundColor =
        if (status == STATUS.OPEN) Color(red = 175, green = 228, blue = 142) else Color(
            red = 224,
            green = 116,
            blue = 116
        )
    val textColor = if (status == STATUS.OPEN) Color(red = 5, green = 119, blue = 2) else Color(
        red = 161,
        green = 19,
        blue = 9
    )
    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(0.dp, backgroundColor)
            )
            .clip(RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_schedule_24),
            contentDescription = "",
            modifier = Modifier.width(12.dp),
            tint = textColor
        )
        Text(
            text = if (status == STATUS.OPEN) "Open" else "Closed",
            fontSize = 9.sp,
            modifier = Modifier.padding(start = 2.dp),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview
@Composable
fun StatusPreview() {
    Status(status = STATUS.CLOSED)
}