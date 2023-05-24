package com.example.befine.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.R
import com.example.befine.ui.theme.Shapes


@Composable
fun NearbyRepairShopItem(name: String, distance: String, status: String) {
    Card(Modifier.width(150.dp), shape = Shapes.small) {
        Column() {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wolf),
                    contentDescription = "",
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Status(status = status,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                )
            }
            RepairShopName(
                name = name, modifier = Modifier
                    .width(150.dp)
                    .padding(top = 4.dp)
            )
            Distance(value = distance)
        }
    }
}

@Composable
fun Distance(value: String) {
    Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Light)
}

@Preview
@Composable
fun NearbyRepairShopItemPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        NearbyRepairShopItem(name = "Bengkel Amanah", distance = "0.7 km", status = STATUS.OPEN)
    }
}