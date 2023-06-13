package com.example.befine.screens.repairshopowners

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.adevinta.leku.*
import com.example.befine.BuildConfig
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.R
import com.example.befine.components.authentication.InputField
import com.example.befine.components.ui.repairshop.*
import com.example.befine.utils.*
import java.util.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.ui.TopBar
import com.example.befine.screens.repairshopowners.edit.EditRepairShopFieldState
import com.example.befine.screens.repairshopowners.edit.EditRepairShopViewModel
import com.example.befine.screens.repairshopowners.edit.LocationFieldState
import com.example.befine.screens.repairshopowners.edit.SchedulesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRepairShopScreen(
    userId: String = "briWG2CqTAe7SVYEf3AYN2O42tq2",
    navController: NavHostController,
    editRepairShopViewModel: EditRepairShopViewModel = viewModel(factory = ViewModelFactory())
) {
    LaunchedEffect(true) {
        editRepairShopViewModel.getInitialData(userId)
    }
    // Context for this composable component
    val context = LocalContext.current

    // Geocoder to convert lat & lon to address
    val geocoder = Geocoder(context)

    // Create file object with unique file name that can be used to store the captured image.
    // stores it in external cache directory
    var file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    // State to save selected image uri
    val capturedImageUri: Uri by editRepairShopViewModel.capturedImageUri.observeAsState(Uri.EMPTY)
    val imageUri = capturedImageUri

    // State related to repair shop name input field
    val state: EditRepairShopFieldState by editRepairShopViewModel.fieldState.observeAsState(
        EditRepairShopFieldState()
    )

    // Progress Indicator state
    val isLoading: Boolean by editRepairShopViewModel.isLoading.observeAsState(false)

    // State related to time picker
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val startWeekdayHoursState = rememberTimePickerState(is24Hour = true)
    val endWeekdayHoursState = rememberTimePickerState(is24Hour = true)
    val startWeekendHoursState = rememberTimePickerState(is24Hour = true)
    val endWeekendHoursState = rememberTimePickerState(is24Hour = true)
    var activeSelectedTimePicker by remember { mutableStateOf("") }
    val schedulesState: SchedulesState by editRepairShopViewModel.schedulesState.observeAsState(
        SchedulesState()
    )

    // State related to location picker
    val locationState: LocationFieldState by editRepairShopViewModel.locationFieldState.observeAsState(
        LocationFieldState()
    )

    // Launcher for taking image from camera by implicit intent
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        editRepairShopViewModel.setCapturedImageUri(uri)
        editRepairShopViewModel.setUserIsUploadFile(true)
    }

    // Launcher for taking image from gallery by implicit intent
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            editRepairShopViewModel.setCapturedImageUri(it)
            editRepairShopViewModel.setUserIsUploadFile(true)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val latitude = it.data?.extras?.getDouble("latitude", 0.0) ?: 0.0
            val longitude = it.data?.extras?.getDouble("longitude", 0.0) ?: 0.0
            editRepairShopViewModel.setLocation(LocationFieldState(latitude, longitude))
        } else {
            Log.d("EDIT", "No data")
        }
    }

    fun onClickCameraButtonHandler() {
        // Check permission status
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // Launch Camera
            file.delete()
            file = context.createImageFile()
            cameraLauncher.launch(uri)
        } else {
            // Request a permission
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun onClickGalleryButtonHandler() {
        galleryLauncher.launch("image/*")
    }

    @Suppress("DEPRECATION")
    val pickedAddress = if (locationState.latitude != 0.0 && locationState.longitude != 0.0) {
        val addressData =
            geocoder.getFromLocation(locationState.latitude, locationState.longitude, 1)

        val addressLine = addressData?.get(0)?.getAddressLine(0)

        "$addressLine"
    } else {
        "Select location"
    }

    fun updateDataHandler() {
        editRepairShopViewModel.updateDataHandler(file = file, userId = userId, context = context)
    }

    Scaffold(
        topBar = {
            TopBar(title = "Edit Repair Shop") {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(
                    horizontal =
                    Screen.paddingHorizontal, vertical = Screen.paddingVertical
                )
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = if (imageUri.path?.isNotEmpty() == true) rememberAsyncImagePainter(
                        model = imageUri,
                        placeholder = painterResource(id = R.drawable.default_image)
                    ) else painterResource(id = R.drawable.default_image),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    LauncherButton(onClick = { onClickCameraButtonHandler() }, text = "Camera")
                    LauncherButton(onClick = { onClickGalleryButtonHandler() }, text = "Gallery")
                }
            }
            InputField(
                value = state.repairShopName,
                onValueChange = { editRepairShopViewModel.onChangeRepairShopName(it) },
                isError = state.isRepairShopNameError,
                errorMessage = state.repairShopNameErrorMsg,
                label = "Repair Shop Name",
                modifier = Modifier.padding(top = 10.dp)
            )
            InputField(
                value = state.address,
                onValueChange = { editRepairShopViewModel.onChangeAddress(it) },
                isError = state.isAddressError,
                errorMessage = state.addressErrorMsg,
                maxLines = 2,
                label = "Address"
            )
            InputField(
                value = state.description,
                onValueChange = { editRepairShopViewModel.onChangeDescription(it) },
                isError = state.isDescriptionError,
                errorMessage = state.descriptionErrorMsg,
                maxLines = 3,
                label = "Description"
            )
            InputField(
                value = state.phoneNumber,
                isError = state.isPhoneNumberError,
                errorMessage = state.phoneNumberErrorMsg,
                onValueChange = { editRepairShopViewModel.onChangePhoneNumber(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = "Telp / Phone Number"
            )
            PickerButton(
                text = pickedAddress,
                icon = { Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "") },
            ) {
                val locationPickerIntent =
                    LocationPickerActivity.Builder().build(context as Activity)
                locationPickerLauncher.launch(Intent(locationPickerIntent))
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                for (i in 0..6) {
                    DayPicker(
                        text = days[i][0].uppercase(),
                        schedulesState.selectedDay[days[i]]!!
                    ) {
                        editRepairShopViewModel.toggleSelectedDay(i)
                    }
                }
            }
            TimeInputRange(
                label = "Weekday Hours",
                enabled = schedulesState.selectedDay[Day.MONDAY]!! || schedulesState.selectedDay[Day.TUESDAY]!! || schedulesState.selectedDay[Day.WEDNESDAY]!! || schedulesState.selectedDay[Day.THURSDAY]!! || schedulesState.selectedDay[Day.FRIDAY]!!,
                onPickStartTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.START_WEEKDAYS
                },
                onPickEndTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.END_WEEKDAYS
                },
                startValue = schedulesState.startWeekdayHours,
                endValue = schedulesState.endWeekdayHours
            )
            TimeInputRange(
                label = "Weekend Hours",
                enabled = schedulesState.selectedDay[Day.SATURDAY]!! || schedulesState.selectedDay[Day.SUNDAY]!!,
                onPickStartTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.START_WEEKEND
                },
                onPickEndTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.END_WEEKEND
                },
                startValue = schedulesState.startWeekendHours,
                endValue = schedulesState.endWeekendHours
            )
            FilledButton(text = "Update", isLoading = isLoading, onClick = { updateDataHandler() })
            if (showTimePickerDialog) {
                Dialog(onDismissRequest = { showTimePickerDialog = false }) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(
                                RoundedCornerShape(
                                    14.dp
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CustomTimePicker(
                            onCancel = { showTimePickerDialog = false },
                            onConfirm = {
                                showTimePickerDialog = false
                                when (activeSelectedTimePicker) {
                                    ActiveTimePicker.START_WEEKDAYS -> editRepairShopViewModel.setStartWeekdayHours(
                                        convertTime(
                                            startWeekdayHoursState.hour,
                                            startWeekdayHoursState.minute
                                        )
                                    )
                                    ActiveTimePicker.END_WEEKDAYS -> editRepairShopViewModel.setEndWeekdayHours(
                                        convertTime(
                                            endWeekdayHoursState.hour,
                                            endWeekdayHoursState.minute
                                        )
                                    )
                                    ActiveTimePicker.START_WEEKEND -> editRepairShopViewModel.setStartWeekendHours(
                                        convertTime(
                                            startWeekendHoursState.hour,
                                            startWeekendHoursState.minute
                                        )
                                    )
                                    ActiveTimePicker.END_WEEKEND -> editRepairShopViewModel.setEndWeekendHours(
                                        convertTime(
                                            endWeekendHoursState.hour,
                                            endWeekendHoursState.minute
                                        )
                                    )
                                }
                            },
                            selectedState = {
                                when (it) {
                                    ActiveTimePicker.START_WEEKDAYS -> startWeekdayHoursState
                                    ActiveTimePicker.END_WEEKDAYS -> endWeekdayHoursState
                                    ActiveTimePicker.START_WEEKEND -> startWeekendHoursState
                                    else -> endWeekendHoursState
                                }
                            },
                            activeTimePicker = activeSelectedTimePicker,
                            buttonContainerModifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EditRepairShopScreenPreview() {
    BefineTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            EditRepairShopScreen(navController = rememberNavController())
        }
    }
}
