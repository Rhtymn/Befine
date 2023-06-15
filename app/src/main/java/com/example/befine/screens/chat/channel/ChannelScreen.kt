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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.ui.chat.ChannelList
import com.example.befine.components.ui.chat.ChannelListContent
import com.example.befine.components.ui.chat.ChannelScreenScaffold
import com.example.befine.components.ui.chat.ChatBox
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import java.util.*

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
    val isLoading: Boolean by channelViewModel.isLoading.observeAsState(false)

    LaunchedEffect(true) {
        channelViewModel.getAllChannelList(role)
        channelViewModel.channelListener(role)
    }

    Log.d("CHANNEL_SCREEN", channelList.toString())
    if (role == ROLE.CLIENT) {
        ChannelScreenScaffold(navController = navController) {
            ChannelListContent(
                channelList = channelList,
                innerPadding = it,
                role = role,
                navController = navController,
                isLoading = isLoading
            )
        }
    } else {
        ChannelListContent(
            channelList = channelList,
            innerPadding = innerPadding,
            role = role,
            navController = navController,
            isLoading = isLoading
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