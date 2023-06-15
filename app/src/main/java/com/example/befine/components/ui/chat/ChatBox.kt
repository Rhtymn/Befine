package com.example.befine.components.ui.chat

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.befine.R
import com.example.befine.firebase.Storage
import com.google.firebase.storage.FirebaseStorage

@Composable
fun ChatBox(
    horizontalPadding: Dp = 0.dp,
    name: String,
    datetime: String,
    message: String,
    numOfUnread: Int = 0,
    photo: String?,
    storage: FirebaseStorage = Storage.getInstance().getStorage(),
    onClick: () -> Unit
) {
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    val imageRef = storage.reference.child("images/${photo}")

    LaunchedEffect(true) {
        if (photo != null) {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                imageUri = uri
            }
        }
    }

    Row(
        Modifier
            .height(70.dp)
            .clickable { onClick() }
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (photo != null) rememberAsyncImagePainter(
                model = imageUri, placeholder = painterResource(
                    id = R.drawable.default_image
                )
            ) else painterResource(id = R.drawable.user),
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