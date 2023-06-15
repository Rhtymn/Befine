package com.example.befine.components.ui.profile

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.befine.R
import com.example.befine.firebase.Storage
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.google.firebase.storage.FirebaseStorage

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
fun ProfileInformation(
    name: String,
    email: String,
    role: String,
    photo: String,
    storage: FirebaseStorage = Storage.getInstance().getStorage()
) {
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    val imageRef = storage.reference.child("images/$photo")

    Log.d("PROFILE_INFORMATION", photo)
    Log.d("PROFILE_INFORMATION", role)

    if (role == ROLE.REPAIR_SHOP_OWNER && photo != "null")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri
        }

    Row(Modifier.padding(horizontal = Screen.paddingHorizontal)) {
        Image(
            painter = if (role == ROLE.REPAIR_SHOP_OWNER && imageUri != Uri.EMPTY) rememberAsyncImagePainter(
                model = imageUri, placeholder = painterResource(id = R.drawable.user)
            ) else painterResource(id = R.drawable.user),
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