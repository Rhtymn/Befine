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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.screens.chat.channel.ChannelList
import com.example.befine.screens.chat.channel.ChannelScreen
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairShopHome(
    navigateToProfile: () -> Unit,
    repairShopHomeViewModel: RepairShopHomeViewModel = viewModel(factory = ViewModelFactory()),
    navController: NavHostController
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
        ChannelScreen(
            navController = navController,
            innerPadding = innerPadding,
            role = ROLE.REPAIR_SHOP_OWNER
        )
    }
}

@Preview
@Composable
fun RepairShopHomePreview() {
    BefineTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RepairShopHome(
                navigateToProfile = { /*TODO*/ }, navController = rememberNavController()
            )
        }
    }
}