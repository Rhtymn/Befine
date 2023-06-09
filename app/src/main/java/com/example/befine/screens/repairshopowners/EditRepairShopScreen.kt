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
import androidx.compose.ui.text.input.KeyboardType
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.ui.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRepairShopScreen(
    navigateToProfileScreen: () -> Unit = {}
) {
    // Context for this composable component
    val context = LocalContext.current

    // Geocoder to convert lat & lon to address
    val geocoder = Geocoder(context)

    // Create file object with unique file name that can be used to store the captured image.
    // stores it in external cache directory
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    // State to save selected image uri
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    // State related to repair shop name input field
    var repairShopName by remember { mutableStateOf("") }
    var isRepairShopError by remember { mutableStateOf(false) }
    var repairShopErrorMsg by remember { mutableStateOf("") }

    // State related to address input field
    var address by remember { mutableStateOf("") }
    var isAddressError by remember { mutableStateOf(false) }
    var addressErrorMsg by remember { mutableStateOf("") }

    // State related to description input field
    var description by remember { mutableStateOf("") }
    var isDescriptionError by remember { mutableStateOf(false) }
    var descriptionErrorMsg by remember { mutableStateOf("") }

    // State related to phone number input field
    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneNumberError by remember { mutableStateOf(false) }
    var phoneNumberErrorMsg by remember { mutableStateOf("") }

    // Progress Indicator state
    var isLoading by remember { mutableStateOf(false) }

    // Form related state
    var isFailed by remember { mutableStateOf(false) }
    var formErrorMsg by remember { mutableStateOf("") }

    fun resetFormState() {
        isFailed = false
        formErrorMsg = ""
    }

    // State related to time picker
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val startWeekdayHoursState = rememberTimePickerState(is24Hour = true)
    val endWeekdayHoursState = rememberTimePickerState(is24Hour = true)
    val startWeekendHoursState = rememberTimePickerState(is24Hour = true)
    val endWeekendHoursState = rememberTimePickerState(is24Hour = true)
    var activeSelectedTimePicker by remember { mutableStateOf("") }
    var startWeekdayHours by remember { mutableStateOf("00:00") }
    var endWeekdaysHours by remember { mutableStateOf("00:00") }
    var startWeekendHours by remember { mutableStateOf("00:00") }
    var endWeekendHours by remember { mutableStateOf("00:00") }

    // State related to location picker
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    // State related to selected day
    val selectedDay = remember {
        mutableStateMapOf(*days.map { it to false }.toTypedArray())
    }

    fun resetInputField() {
        repairShopName = ""
        isRepairShopError = false
        repairShopErrorMsg = ""

        address = ""
        isAddressError = false
        addressErrorMsg = ""

        description = ""
        isDescriptionError = false
        descriptionErrorMsg = ""

        phoneNumber = ""
        isPhoneNumberError = false
        phoneNumberErrorMsg = ""

        capturedImageUri = Uri.EMPTY

        latitude = 0.0
        longitude = 0.0

        startWeekdayHours = "00:00"
        endWeekdaysHours = "00:00"
        startWeekendHours = "00:00"
        endWeekendHours = "00:00"

        selectedDay[Day.MONDAY] = false
        selectedDay[Day.TUESDAY] = false
        selectedDay[Day.WEDNESDAY] = false
        selectedDay[Day.THURSDAY] = false
        selectedDay[Day.FRIDAY] = false
        selectedDay[Day.SATURDAY] = false
        selectedDay[Day.SUNDAY] = false
    }

    // Launcher for taking image from camera by implicit intent
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        capturedImageUri = uri
    }

    // Launcher for taking image from gallery by implicit intent
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        capturedImageUri = uri
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
            latitude = it.data?.extras?.getDouble("latitude", 0.0) ?: 0.0
            longitude = it.data?.extras?.getDouble("longitude", 0.0) ?: 0.0
            Log.d("EDIT", "${it.data?.extras?.getDouble("latitude", 0.0)}")
            Log.d("EDIT", "${it.data?.extras?.getDouble("longitude", 0.0)}")
        } else {
            Log.d("EDIT", "No data")
        }
    }

    fun updateHandler() {
        Log.d("EDIT", "Update!")
    }

    fun onClickCameraButtonHandler() {
        // Check permission status
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // Launch Camera
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
    val pickedAddress = if (latitude != 0.0 && longitude != 0.0) {
        val addressData = geocoder.getFromLocation(latitude, longitude, 1)
        "${addressData?.get(0)?.thoroughfare}, ${addressData?.get(0)?.locality}"
    } else {
        "Select location"
    }

    Scaffold(
        topBar = {
            TopBar(title = "Edit Repair Shop") {
                navigateToProfileScreen()
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
                    painter = if (capturedImageUri.path?.isNotEmpty() == true) rememberAsyncImagePainter(
                        model = capturedImageUri,
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
                value = repairShopName,
                onValueChange = { repairShopName = it },
                isError = isRepairShopError,
                errorMessage = repairShopErrorMsg,
                label = "Repair Shop Name",
                modifier = Modifier.padding(top = 10.dp)
            )
            InputField(
                value = address,
                onValueChange = { address = it },
                isError = isAddressError,
                errorMessage = addressErrorMsg,
                label = "Address"
            )
            InputField(
                value = description,
                onValueChange = { description = it },
                isError = isDescriptionError,
                errorMessage = descriptionErrorMsg,
                label = "Description"
            )
            InputField(
                value = phoneNumber,
                isError = isPhoneNumberError,
                errorMessage = phoneNumberErrorMsg,
                onValueChange = { phoneNumber = it },
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
                DayPicker(text = "M", selectedDay[Day.MONDAY]!!) {
                    selectedDay[Day.MONDAY] = !selectedDay[Day.MONDAY]!!
                }
                DayPicker(text = "T", selectedDay[Day.TUESDAY]!!) {
                    selectedDay[Day.TUESDAY] = !selectedDay[Day.TUESDAY]!!
                }
                DayPicker(text = "W", selectedDay[Day.WEDNESDAY]!!) {
                    selectedDay[Day.WEDNESDAY] = !selectedDay[Day.WEDNESDAY]!!
                }
                DayPicker(text = "T", selectedDay[Day.THURSDAY]!!) {
                    selectedDay[Day.THURSDAY] = !selectedDay[Day.THURSDAY]!!
                }
                DayPicker(text = "F", selectedDay[Day.FRIDAY]!!) {
                    selectedDay[Day.FRIDAY] = !selectedDay[Day.FRIDAY]!!
                }
                DayPicker(text = "S", selectedDay[Day.SATURDAY]!!) {
                    selectedDay[Day.SATURDAY] = !selectedDay[Day.SATURDAY]!!
                }
                DayPicker(text = "S", selectedDay[Day.SUNDAY]!!) {
                    selectedDay[Day.SUNDAY] = !selectedDay[Day.SUNDAY]!!
                }
            }
            TimeInputRange(
                label = "Weekday Hours",
                enabled = selectedDay[Day.MONDAY]!! || selectedDay[Day.TUESDAY]!! || selectedDay[Day.WEDNESDAY]!! || selectedDay[Day.THURSDAY]!! || selectedDay[Day.FRIDAY]!!,
                onPickStartTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.START_WEEKDAYS
                },
                onPickEndTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.END_WEEKDAYS
                },
                startValue = startWeekdayHours,
                endValue = endWeekdaysHours
            )
            TimeInputRange(
                label = "Weekend Hours",
                enabled = selectedDay[Day.SATURDAY]!! || selectedDay[Day.SUNDAY]!!,
                onPickStartTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.START_WEEKEND
                },
                onPickEndTime = {
                    showTimePickerDialog = true
                    activeSelectedTimePicker = ActiveTimePicker.END_WEEKEND
                },
                startValue = startWeekendHours,
                endValue = endWeekendHours
            )
            FilledButton(text = "Update", isLoading = isLoading, onClick = { updateHandler() })
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
                                    ActiveTimePicker.START_WEEKDAYS -> startWeekdayHours =
                                        convertTime(
                                            startWeekdayHoursState.hour,
                                            startWeekdayHoursState.minute
                                        )
                                    ActiveTimePicker.END_WEEKDAYS -> endWeekdaysHours =
                                        convertTime(
                                            endWeekdayHoursState.hour,
                                            endWeekdayHoursState.minute
                                        )
                                    ActiveTimePicker.START_WEEKEND -> startWeekendHours =
                                        convertTime(
                                            startWeekendHoursState.hour,
                                            startWeekendHoursState.minute
                                        )
                                    ActiveTimePicker.END_WEEKEND -> endWeekendHours =
                                        convertTime(
                                            endWeekendHoursState.hour,
                                            endWeekendHoursState.minute
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
            EditRepairShopScreen()
        }
    }
}
