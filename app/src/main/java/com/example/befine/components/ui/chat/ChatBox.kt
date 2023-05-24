package com.example.befine.components.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.befine.R

@Composable
fun ChatBox(name: String, datetime: String, message: String, numOfUnread: Int = 0) {
    Row(Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.wolf),
            contentDescription = "",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Name(value = name)
                Datetime(value = datetime)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Message(
                    value = message,
                    modifier = Modifier.weight(1f)
                )
                if (numOfUnread > 0) {
                    Badge(
                        backgroundColor = Color.Green,
                        content = { Text(text = if (numOfUnread <= 99) "$numOfUnread" else "99") }
                    )
                }
            }
        }
    }
}