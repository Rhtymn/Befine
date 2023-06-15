package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.befine.components.lottie.NoMessageAnimation
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.example.befine.utils.channelChatDatetime
import com.example.befine.utils.convertChatRoomStateToJSON

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
        if (channelList.isEmpty()) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NoMessageAnimation()
            }
        } else {
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
}