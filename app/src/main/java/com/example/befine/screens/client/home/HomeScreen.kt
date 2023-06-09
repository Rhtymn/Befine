package com.example.befine.screens.client

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
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
import com.example.befine.model.RepairShop
import com.example.befine.screens.client.home.HomeViewModel
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.STATUS
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory

private const val REQUEST_LOCATION_PERMISSION = 1

@Composable
fun GetUserLocation() {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var location by remember { mutableStateOf<Location?>(null) }
    val Geocoder = Geocoder(context)

    DisposableEffect(Unit) {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(newLocation: Location) {
                location = newLocation
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
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
            // Requset location permission
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
        val address = Geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
        UserLocation(
            location = "${address?.get(0)?.thoroughfare}, ${address?.get(0)?.locality}",
            modifier = Modifier.padding(bottom = 8.dp)
        )
    } else {
        UserLocation(
            location = "User Location",
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }


}

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
            GetUserLocation()
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
                status = STATUS.CLOSED
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