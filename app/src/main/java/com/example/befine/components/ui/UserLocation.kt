package com.example.befine.components.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UserLocation(location: String, modifier: Modifier = Modifier) {
    Row(modifier) {
        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "")
        Text(
            location,
            modifier = Modifier.padding(start = 4.dp),
            color = Color(0xffaaaaaa),
            fontWeight = FontWeight.Bold
        )
    }
}