package com.example.befine.screens.chat.channel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.ui.chat.ChatBox
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen


@Composable
fun ChatList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ChatBox(
            name = "Andy Machisa",
            datetime = "09.25",
            message = "Halo, ada yang bisa kami bantu?",
            numOfUnread = 109
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(modifier: Modifier = Modifier, navController: NavHostController) {
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
        ChatList(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical)
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
            ChannelScreen(navController = rememberNavController())
        }
    }
}