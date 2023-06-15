package com.example.befine.screens.client.details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.befine.R
import com.example.befine.components.ui.RepairShopName
import com.example.befine.firebase.Auth
import com.example.befine.model.AuthData
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    repairShopId: String = "briWG2CqTAe7SVYEf3AYN2O42tq2",
    navigateToChatRoom: (chatRoomState: ChatRoomState) -> Unit = {},
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    navController: NavHostController,
    repairShopDetailsViewModel: RepairShopDetailsViewModel = viewModel(factory = ViewModelFactory())
) {
    val context = LocalContext.current
    val state: RepairShopDetailsState by repairShopDetailsViewModel.state.observeAsState(
        RepairShopDetailsState()
    )
    val userPreference: AuthData by repairShopDetailsViewModel.userPreference.observeAsState(
        AuthData()
    )

    val cameraPositionState = rememberCameraPositionState()

    if (state.location != null) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(state.location!!, 14f)
    }

    LaunchedEffect(true) {
        repairShopDetailsViewModel.getInitialData(repairShopId)
        CoroutineScope(Dispatchers.IO).launch {
            repairShopDetailsViewModel.getUserPreference(context)
        }
    }

    val closedHourThisDay =
        if (state.repairShop?.schedule?.isNotEmpty() == true && state.repairShop!!.schedule!![getDay()].status == STATUS.OPEN) {
            state.repairShop?.schedule?.get(getDay())?.operationalHours.toString().slice(6..10)
        } else {
            ""
        }
    val thisDaySchedule =
        if (state.repairShop?.schedule?.let { isRepairShopOpen(it) } == STATUS.OPEN) "Open — Closed at $closedHourThisDay" else "Closed"

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetContent = {
            Column(
                Modifier
                    .padding(
                        horizontal = Screen.paddingHorizontal,
                        vertical = Screen.paddingVertical
                    )
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = if (state.imageUri?.path?.isNotEmpty() == true) rememberAsyncImagePainter(
                        model = state.imageUri, placeholder = painterResource(
                            id = R.drawable.default_image
                        )
                    ) else painterResource(id = R.drawable.default_image),
                    modifier = Modifier.size(height = 200.dp, width = 250.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
                RepairShopName(
                    name = state.repairShop?.name.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                SubText(text = state.repairShop?.description.toString())
                SubText(text = state.repairShop?.address.toString())
                SubText(text = thisDaySchedule)
                SubText(text = "1.1km - 5 mnt")
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Row {
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
                        state.repairShop?.schedule?.forEach { data ->
                            val time =
                                if (data.operationalHours.isNullOrBlank()) "closed" else data.operationalHours.toString()
                            Schedule(
                                day = capitalize(data.day.toString()),
                                time = time
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Button(
                    onClick = {
                        val chatRoomState = ChatRoomState(
                            repairShopName = state.repairShop?.name.toString(),
                            repairShopPhoto = state.repairShop?.photo.toString(),
                            repairShopId = repairShopId,
                            userId = auth.currentUser?.uid!!,
                            userName = userPreference.name,
                            senderRole = ROLE.CLIENT
                        )
                        navigateToChatRoom(chatRoomState)
                    },
                    modifier = Modifier.padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "",
                        modifier = Modifier.padding(end = 4.dp),
                        tint = Color.White
                    )
                    Text("Send Message", color = Color.White)
                }
            }
        }) {
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 15.dp, top = 15.dp)
                    .zIndex(10f)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)

            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { navController.popBackStack() }
                )
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { scope.launch { scaffoldState.bottomSheetState.partialExpand() } },
            ) {
                if (state.location != null) {
                    Marker(
                        state = MarkerState(position = LatLng(state.location!!.latitude, state.location!!.longitude)),
                        title = "Singapore",
                        snippet = "Marker in Singapore"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RepairShopDetailsPreview() {
    BefineTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RepairShopDetailsScreen(navController = rememberNavController())
        }
    }
}