package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreenScaffold(
    navController: NavHostController,
    content: @Composable (innerPadding: PaddingValues) -> Unit = {},
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