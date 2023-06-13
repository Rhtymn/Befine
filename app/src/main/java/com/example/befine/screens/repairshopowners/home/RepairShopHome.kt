package com.example.befine.screens.repairshopowners.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.befine.screens.chat.channel.ChatList
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairShopHome(
    navigateToProfile: () -> Unit,
    repairShopHomeViewModel: RepairShopHomeViewModel = viewModel(factory = ViewModelFactory())
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.IO).launch {
            repairShopHomeViewModel.setUserPreferences(context = context)
        }
    }

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
                    IconButton(onClick = { navigateToProfile() }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        ChatList(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical)
        )
    }
}

@Preview
@Composable
fun RepairShopHomePreview() {
    BefineTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RepairShopHome(navigateToProfile = { /*TODO*/ })
        }
    }
}