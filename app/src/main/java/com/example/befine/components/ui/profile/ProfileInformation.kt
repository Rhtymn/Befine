package com.example.befine.components.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.R

@Composable
fun ProfileName(value: String) {
    Text(
        value,
        modifier = Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun ProfileEmail(value: String) {
    Text(
        value,
        modifier = Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis,
        fontSize = 16.sp
    )
}

@Composable
fun ProfileInformation(name: String, email: String) {
    Row() {
        Image(
            painter = painterResource(id = R.drawable.wolf),
            contentDescription = "",
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Column(
            Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            ProfileName(value = name)
            ProfileEmail(value = email)
        }
    }
}