package com.example.befine.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.R
import com.example.befine.ui.theme.Shapes

@Composable
fun RepairShopItem(name: String, status: String, address: String) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), shape = Shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.wolf),
                contentDescription = "",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RepairShopName(name = name)
                    Status(status = status)
                }
                RepairShopAddress(
                    value = address,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun RepairShopItemPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        RepairShopItem(
            name = "Bengkel Amanah",
            status = STATUS.OPEN,
            address = "Jl Merdeka No 14, Jakarta Selatan, Jakarta."
        )
    }
}