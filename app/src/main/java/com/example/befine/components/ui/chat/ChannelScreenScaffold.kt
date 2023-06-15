package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreenScaffold(
    navController: NavHostController,
    searchValue: String,
    onChangeSearchValue: (String) -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit = {},
) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = { },
            windowInsets = WindowInsets(bottom = 8.dp),
            navigationIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                    SearchBar(
                        query = searchValue,
                        onQueryChange = { onChangeSearchValue(it) },
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                        },
                        modifier = Modifier.padding(end = 12.dp),
                        placeholder = { Text("Search") }
                    ) {

                    }
                }
            }
        )
    }) { innerPadding ->
        content(innerPadding)
    }
}