package com.example.befine.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.components.ui.UserLocation
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen

@Composable
fun HomeScreen() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                topBar = {
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
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Email, contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    Modifier
                        .padding(innerPadding)
                        .padding(
                            horizontal = Screen.paddingHorizontal,
                            vertical = Screen.paddingVertical
                        )
                ) {
                    UserLocation(location = "User Location")
                }
            }
        }
    }
}