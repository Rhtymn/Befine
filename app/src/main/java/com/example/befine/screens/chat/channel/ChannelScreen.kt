package com.example.befine.screens.chat.channel

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.ui.chat.ChatBox
import com.example.befine.components.ui.chat.Datetime
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import com.google.android.play.integrity.internal.c
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    channelList: List<ChatChannelModel>,
    role: String,
    navController: NavHostController,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        channelList.forEach {
            val name = if (role == ROLE.CLIENT) it.repairShop?.name else it.client?.name
            val photo = if (role == ROLE.CLIENT) it.repairShop?.photo else null
            val chatRoomState = ChatRoomState(
                repairShopName = it.repairShop?.name,
                repairShopPhoto = it.repairShop?.photo,
                repairShopId = it.repairShop?.id,
                userId = it.client?.id,
                userName = it.client?.name,
                senderRole = role
            )
            ChatBox(
                name = name!!,
                datetime = channelChatDatetime(it.lastDatetime.toString()),
                message = it.lastMessage.toString(),
                photo = photo,
                onClick = {
                    navController.navigate(
                        com.example.befine.navigation.Screen.ChatRoom.createRoute(
                            convertChatRoomStateToJSON(chatRoomState)!!
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    channelViewModel: ChannelViewModel = viewModel(
        factory = ViewModelFactory()
    )
) {
    val channelList = channelViewModel.channelList.toList()
    LaunchedEffect(true) {
        channelViewModel.getAllChannelList(ROLE.CLIENT)
        channelViewModel.channelListener(ROLE.CLIENT)
    }

    Log.d("CHANNEL_SCREEN", channelList.toString())
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                SearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    },
                    placeholder = { Text("Search") }
                ) {

                }
            },
            windowInsets = WindowInsets(bottom = 8.dp),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            }
        )
    }) { innerPadding ->
        if (channelList.isEmpty()) {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            ChatList(
                Modifier
                    .padding(innerPadding)
                    .padding(
                        horizontal = Screen.paddingHorizontal,
                        vertical = Screen.paddingVertical
                    ),
                channelList = channelList.distinct(),
                role = ROLE.CLIENT,
                navController = navController
            )
        }
    }

}

@Preview
@Composable
fun ChannelScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ChannelScreen(navController = rememberNavController())
        }
    }
}