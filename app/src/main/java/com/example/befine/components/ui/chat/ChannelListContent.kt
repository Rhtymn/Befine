package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.befine.components.lottie.ChannelScreenLoader
import com.example.befine.screens.chat.room.model.ChatChannelModel

@Composable
fun ChannelListContent(
    channelList: List<ChatChannelModel>,
    innerPadding: PaddingValues,
    role: String,
    navController: NavHostController,
    isLoading: Boolean
) {
    if (channelList.isEmpty() && isLoading) {
        ChannelScreenLoader()
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