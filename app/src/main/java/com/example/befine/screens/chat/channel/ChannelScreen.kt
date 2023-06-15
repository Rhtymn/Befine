package com.example.befine.screens.chat.channel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.ui.chat.ChannelListContent
import com.example.befine.components.ui.chat.ChannelScreenScaffold
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
    val searchValue: String by channelViewModel.searchValue.observeAsState("")

    LaunchedEffect(true) {
        channelViewModel.getAllChannelList(role)
        channelViewModel.channelListener(role)
    }

    var filteredChannelList: List<ChatChannelModel> = listOf()
    if (searchValue != "" && channelList.isNotEmpty()) {
        filteredChannelList = channelList.filter {
            if (role == ROLE.CLIENT) {
                searchValue in it.repairShop?.name.toString().lowercase(Locale.ROOT)
            } else {
                searchValue in it.client?.name.toString().lowercase(Locale.ROOT)
            }
        }
    }
    if (role == ROLE.CLIENT) {
        ChannelScreenScaffold(
            navController = navController,
            searchValue = searchValue,
            onChangeSearchValue = channelViewModel::onChangeSearchValue
        ) {
            ChannelListContent(
                channelList = if (searchValue != "") filteredChannelList else channelList,
                innerPadding = it,
                role = role,
                navController = navController,
                isLoading = isLoading
            )
        }
    } else {
        ChannelListContent(
            channelList = if (searchValue != "") filteredChannelList else channelList,
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