package com.example.befine.screens.client.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.R
import com.example.befine.components.ui.RepairShopName
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SubText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .fillMaxWidth(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Schedule(day: String, time: String) {
    Row(
        modifier = Modifier
            .width(200.dp)
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day)
        Text(time)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairShopDetailsScreen(
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
) {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 14f)
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetContent = {
            Column(
                Modifier
                    .padding(horizontal = Screen.paddingHorizontal)
                    .height(550.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_image),
                    contentDescription = ""
                )
                RepairShopName(
                    name = "Bengkel Amanah",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                SubText(text = "Jl Merdeka No 14, Jakarta Selatan, Jakarta.")
                SubText(text = "Buka - Tutup jam 17.00")
                SubText(text = "1.1km - 5 mnt")
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Row() {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_schedule_24),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Schedule(day = "Senin", time = "07.00 - 15.00")
                        Schedule(day = "Selasa", time = "07.00 - 15.00")
                        Schedule(day = "Rabu", time = "07.00 - 15.00")
                        Schedule(day = "Kamis", time = "07.00 - 15.00")
                        Schedule(day = "Jum'at", time = "07.00 - 15.00")
                        Schedule(day = "Sabtu", time = "07.00 - 15.00")
                        Schedule(day = "Minggu", time = "07.00 - 15.00")
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 6.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Send Message")
                }
            }
        }) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { _ -> scope.launch { scaffoldState.bottomSheetState.partialExpand() } },
        ) {
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RepairShopDetailsPreview() {
    BefineTheme() {
        Surface(modifier = Modifier.fillMaxSize()) {
            RepairShopDetailsScreen()
        }
    }
}