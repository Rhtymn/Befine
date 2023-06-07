package com.example.befine.components.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun RepairShopName(name: String, modifier: Modifier = Modifier, fontSize: TextUnit = 15.sp) {
    Text(
        text = name,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}