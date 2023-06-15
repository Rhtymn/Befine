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
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import java.util.*

@Composable
fun ChannelList(
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
                horizontalPadding = Screen.paddingHorizontal,
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
fun ChannelScreenScaffold(
    navController: NavHostController,
    content: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
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
        content(innerPadding)
    }
}

@Composable
fun Content(
    channelList: List<ChatChannelModel>,
    innerPadding: PaddingValues,
    role: String,
    navController: NavHostController
) {
    if (channelList.isEmpty()) {
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        ChannelList(
            Modifier
                .padding(innerPadding),
            channelList = channelList.distinct(),
            role = role,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavHostController,
    channelViewModel: ChannelViewModel = viewModel(
        factory = ViewModelFactory()
    ),
    role: String = ROLE.CLIENT
) {
    val channelList = channelViewModel.channelList.toList()
    Log.d("CHANNEL", role)
    LaunchedEffect(true) {
        channelViewModel.getAllChannelList(role)
        channelViewModel.channelListener(role)
    }

    Log.d("CHANNEL_SCREEN", channelList.toString())
    if (role == ROLE.CLIENT) {
        ChannelScreenScaffold(navController = navController) {
            Content(
                channelList = channelList,
                innerPadding = it,
                role = role,
                navController = navController
            )
        }
    } else {
        Content(
            channelList = channelList,
            innerPadding = innerPadding,
            role = role,
            navController = navController
        )
    }
}

@Preview
@Composable
fun ChannelScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ChannelScreen(
                navController = rememberNavController(),
                innerPadding = PaddingValues(0.dp)
            )
        }
    }
}