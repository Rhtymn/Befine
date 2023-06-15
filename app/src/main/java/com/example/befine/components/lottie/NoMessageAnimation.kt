package com.example.befine.components.lottie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*

@Composable
fun NoMessageAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_y0RfCl.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(100.dp)
        )
        Text("No message yet", modifier = Modifier.padding(top = 10.dp), fontSize = 20.sp)
    }
}