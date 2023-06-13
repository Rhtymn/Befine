package com.example.befine.screens.client.details

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.befine.R
import com.example.befine.components.ui.RepairShopName
import com.example.befine.firebase.Auth
import com.example.befine.firebase.Storage
import com.example.befine.model.ChatRoomState
import com.example.befine.model.RepairShop
import com.example.befine.preferences.PreferenceDatastore.Companion.userId
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
    db: FirebaseFirestore = Firebase.firestore,
    storage: FirebaseStorage = Storage.getInstance().getStorage(),
    repairShopId: String = "briWG2CqTAe7SVYEf3AYN2O42tq2",
    navigateToChatRoom: (chatRoomState: ChatRoomState) -> Unit = {},
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    navController: NavHostController
) {
    var location by remember { mutableStateOf(LatLng(-6.187198, 106.827342)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 14f)
    }
    var repairShop by remember {
        mutableStateOf(RepairShop())
    }
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }

    LaunchedEffect(true) {
        // Get repair shop data
        repairShop =
            db.collection("repairShops").document(repairShopId).get().await()
                .toObject<RepairShop>() ?: RepairShop()

        // Get repair shop image
        val imageRef = storage.reference.child("images/${repairShop.photo}")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri
        }

        // Update location
        location = LatLng(repairShop.latitude?.toDouble()!!, repairShop.longitude?.toDouble()!!)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 14f)
    }

    val closedHourThisDay =
        if (repairShop.schedule?.isNotEmpty() == true && repairShop.schedule!![getDay()].status == STATUS.OPEN) {
            repairShop.schedule?.get(getDay())?.operationalHours.toString().slice(6..10)
        } else {
            ""
        }
    val thisDaySchedule =
        if (repairShop.schedule?.let { isRepairShopOpen(it) } == STATUS.OPEN) "Open — Closed at $closedHourThisDay" else "Closed"

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
                    painter = if (imageUri.path?.isNotEmpty() == true) rememberAsyncImagePainter(
                        model = imageUri, placeholder = painterResource(
                            id = R.drawable.default_image
                        )
                    ) else painterResource(id = R.drawable.default_image),
                    modifier = Modifier.size(height = 200.dp, width = 250.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
                RepairShopName(
                    name = repairShop.name.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                SubText(text = repairShop.description.toString())
                SubText(text = repairShop.address.toString())
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
                        repairShop.schedule?.forEach { data ->
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
                        navigateToChatRoom(
                            ChatRoomState(
                                name = repairShop.name.toString(),
                                photo = repairShop.photo.toString(),
                                receiverId = repairShopId,
                                senderId = auth.currentUser?.uid!!
                            )
                        )
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
                if (location.latitude != -6.187198 && location.longitude != 106.827342) {
                    Marker(
                        state = MarkerState(position = location),
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