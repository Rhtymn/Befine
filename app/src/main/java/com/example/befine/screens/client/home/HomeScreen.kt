package com.example.befine.screens.client.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.befine.components.ui.NearbyRepairShopItem
import com.example.befine.components.ui.RepairShopItem
import com.example.befine.components.ui.UserLocation
import com.example.befine.di.Injection
import com.example.befine.model.RepairShopWithId
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory
import com.example.befine.utils.isRepairShopOpen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val REQUEST_LOCATION_PERMISSION = 1

@Composable
fun GetUserLocation() {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var location by remember { mutableStateOf<Location?>(null) }
    var address by remember { mutableStateOf("") }
    val geocoder = Geocoder(context)

    DisposableEffect(Unit) {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(newLocation: Location) {
                location = newLocation
            }

            @Suppress("DEPRECATION")
            @Deprecated(
                "Deprecated in Java", ReplaceWith(
                    "super.onStatusChanged(provider, status, extras)",
                    "android.location.LocationListener"
                )
            )
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }
        }

        val permissionStatus = ActivityCompat.checkSelfPermission(context, locationPermission)
        if (isLocationEnabled && permissionStatus == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(locationPermission),
                REQUEST_LOCATION_PERMISSION
            )
        }

        onDispose {
            locationManager.removeUpdates(locationListener)
        }
    }

    if (location != null) {
        val temp = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
        address = "${temp?.get(0)?.thoroughfare}, ${temp?.get(0)?.locality}"
    }
    UserLocation(
        location = if (address.isNotEmpty()) address else "User location",
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToProfile: () -> Unit,
    navigateToChatChannel: () -> Unit,
    navigateToRepairShopDetail: (repairShopId: String) -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepairShopRepository())),
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.setUserPreferences(context)
        }
    }
    val repairShopData: List<RepairShopWithId> by homeViewModel.repairShopData.observeAsState(
        listOf()
    )
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
            GetUserLocation()
            HeaderText(value = "Nearby Repair Shops")
            NearbyShopList(
                data = repairShopData,
                navigateToRepairShopDetail = navigateToRepairShopDetail
            )
            HeaderText(value = "Others")
            OthersList(
                data = repairShopData,
                navigateToRepairShopDetail = navigateToRepairShopDetail
            )
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
fun NearbyShopList(
    data: List<RepairShopWithId>,
    navigateToRepairShopDetail: (repairShopId: String) -> Unit
) {
    LazyRow(modifier = Modifier.padding(bottom = 24.dp)) {
        items(data, key = { it.id }) {
            NearbyRepairShopItem(
                name = it.repairShop.name.toString(),
                distance = "0.7 km",
                status = isRepairShopOpen(it.repairShop.schedule!!),
                image = it.repairShop.photo.toString(),
                onClick = { navigateToRepairShopDetail(it.id) }
            )
        }
    }
}

@Composable
fun OthersList(
    data: List<RepairShopWithId>,
    navigateToRepairShopDetail: (repairShopId: String) -> Unit
) {
    LazyColumn {
        items(data, key = { it.id }) {
            RepairShopItem(
                name = it.repairShop.name.toString(),
                status = isRepairShopOpen(it.repairShop.schedule!!),
                address = it.repairShop.address.toString(),
                image = it.repairShop.photo.toString(),
                onClick = { navigateToRepairShopDetail(it.id) }
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
            HomeScreen(
                navigateToProfile = { /*TODO*/ },
                navigateToChatChannel = { /*TODO*/ },
                navigateToRepairShopDetail = {}
            )
        }
    }
}