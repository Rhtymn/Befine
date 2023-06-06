package com.example.befine.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.befine.components.ui.NearbyRepairShopItem
import com.example.befine.components.ui.RepairShopItem
import com.example.befine.components.ui.UserLocation
import com.example.befine.di.Injection
import com.example.befine.model.RepairShop
import com.example.befine.screens.client.home.HomeViewModel
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.STATUS
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToProfile: () -> Unit,
    navigateToChatChannel: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepairShopRepository())),
) {
    val data = homeViewModel.getAllRepairShop()
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
                actions = {
                    IconButton(onClick = { navigateToChatChannel() }) {
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
            UserLocation(
                location = "User Location",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HeaderText(value = "Nearby Repair Shops")
            NearbyShopList(data = data)
            HeaderText(value = "Others")
            OthersList(data = data)
        }
    }
}

@Composable
fun HeaderText(value: String, modifier: Modifier = Modifier) {
    Text(
        value,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun NearbyShopList(data: List<RepairShop>) {
    LazyRow(modifier = Modifier.padding(bottom = 24.dp)) {
        items(data, key = { it.userId.toString() }) {
            NearbyRepairShopItem(
                name = it.name.toString(),
                distance = "0.7 km",
                status = STATUS.OPEN
            )
        }
    }
}

@Composable
fun OthersList(data: List<RepairShop>) {
    LazyColumn() {
        items(data, key = { it.userId.toString() }) {
            RepairShopItem(
                name = it.name.toString(),
                status = STATUS.OPEN,
                address = it.address.toString()
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    BefineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            HomeScreen(navigateToProfile = { /*TODO*/ }, navigateToChatChannel = { /*TODO*/ })
        }
    }
}