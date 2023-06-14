package com.example.befine.screens.chat.room

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.R
import com.example.befine.components.ui.chat.ChatMessage
import com.example.befine.components.ui.chat.MessageInput
import com.example.befine.firebase.Storage
import com.example.befine.utils.ViewModelFactory
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoom(
    chatRoomState: ChatRoomState,
    storage: FirebaseStorage = Storage.getInstance().getStorage(),
    navController: NavHostController,
    chatRoomViewModel: ChatRoomViewModel = viewModel(factory = ViewModelFactory())
) {
    val state: ChatRoomState by chatRoomViewModel.state.observeAsState(ChatRoomState())
    val message: String by chatRoomViewModel.message.observeAsState("")
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    val imageRef = storage.reference.child("images/${chatRoomState.repairShopPhoto}")

    LaunchedEffect(true) {
        chatRoomViewModel.setChatRoomState(chatRoomState)
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = imageUri, placeholder = painterResource(
                                id = R.drawable.default_image
                            )
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = chatRoomState.repairShopName ?: "",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            },
        )
    }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ChatMessage()
            }
            MessageInput(value = message, onValueChange = chatRoomViewModel::onChangeMessageValue) {
                chatRoomViewModel.onSendMessage()
            }
        }

    }
}

@Preview
@Composable
fun ChatRoomPreview() {
    BefineTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ChatRoom(
                chatRoomState = ChatRoomState(
                    repairShopName = "Bengkel Amanah",
                    repairShopId = "ihEoAEV6qWStpvVUoGHIqr4Qb3W2",
                    userId = "k1Wd33pFZ5USQmBjSFiIxIevpk12",
                    repairShopPhoto = "ihEoAEV6qWStpvVUoGHIqr4Qb3W2.jpg",
                    userName = "Muhammad Hafizh Roihan"
                ),
                navController = rememberNavController()
            )
        }
    }
}