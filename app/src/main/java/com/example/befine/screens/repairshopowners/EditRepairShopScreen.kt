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
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.components.authentication.FilledButton
import com.example.befine.components.ui.TopBar
import com.example.befine.firebase.Auth
import com.example.befine.firebase.Storage
import com.example.befine.model.RepairShop
import com.example.befine.model.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRepairShopScreen(
    db: FirebaseFirestore = Firebase.firestore,
    storage: FirebaseStorage = Storage.getInstance().getStorage(),
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    userId: String = "briWG2CqTAe7SVYEf3AYN2O42tq2",
    navController: NavHostController
) {
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

    // State related to select image state
    var isUserUploadFile by remember { mutableStateOf(false) }

    fun resetInputField() {
        isRepairShopError = false
        repairShopErrorMsg = ""

        isAddressError = false
        addressErrorMsg = ""

        isDescriptionError = false
        descriptionErrorMsg = ""

        isPhoneNumberError = false
        phoneNumberErrorMsg = ""

        isUserUploadFile = false
    }

    // Launcher for taking image from camera by implicit intent
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        capturedImageUri = uri
        isUserUploadFile = true
    }

    // Launcher for taking image from gallery by implicit intent
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            capturedImageUri = it
            isUserUploadFile = true
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
            latitude = it.data?.extras?.getDouble("latitude", 0.0) ?: 0.0
            longitude = it.data?.extras?.getDouble("longitude", 0.0) ?: 0.0
            Log.d("EDIT", "${it.data?.extras?.getDouble("latitude", 0.0)}")
            Log.d("EDIT", "${it.data?.extras?.getDouble("longitude", 0.0)}")
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
    val pickedAddress = if (latitude != 0.0 && longitude != 0.0) {
        val addressData = geocoder.getFromLocation(latitude, longitude, 1)

        val addressLine = addressData?.get(0)?.getAddressLine(0)

        "$addressLine"
    } else {
        "Select location"
    }

    Log.d("EDIT", capturedImageUri.toString())

    LaunchedEffect(true) {
        val repairShop: RepairShop =
            db.collection("repairShops").document(userId)
                .get().await().toObject<RepairShop>() ?: RepairShop()

        repairShopName = if (repairShop.name.isNullOrBlank()) "" else repairShop.name.toString()
        address = if (repairShop.address.isNullOrBlank()) "" else repairShop.address.toString()
        description = if (repairShop.description.isNullOrBlank()) "" else repairShop.description.toString()
        phoneNumber = if (repairShop.phone_number.isNullOrBlank()) "" else repairShop.phone_number.toString()
        latitude = if (repairShop.latitude.isNullOrBlank()) 0.0 else repairShop.latitude.toDouble()
        longitude = if (repairShop.longitude.isNullOrBlank()) 0.0 else repairShop.longitude.toDouble()

        for (i in 0..6) {
            selectedDay[days[i]] =
                if (repairShop.schedule?.get(i)?.status.isNullOrBlank()) false else repairShop.schedule?.get(
                    i
                )?.status == "open"

            if (i < 5) { // weekdays
                if (!repairShop.schedule?.get(i)?.operationalHours.isNullOrBlank()) {
                    startWeekdayHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(0..4) ?: "00:00"
                    endWeekdaysHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(6..10) ?: "00:00"
                }
            } else {
                if (!repairShop.schedule?.get(i)?.operationalHours.isNullOrBlank()) {
                    startWeekendHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(0..4) ?: "00:00"
                    endWeekendHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(6..10) ?: "00:00"
                }
            }
        }

        if (!repairShop.photo.isNullOrBlank()) {
            val imageRef = storage.reference.child("images/${repairShop.photo}")
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                capturedImageUri = uri
            }
        }
    }


    fun updateDataHandler() {
        try {
            isLoading = true

            // repair shop name validation
            inputFieldValidation(repairShopName) {
                isRepairShopError = true
                repairShopErrorMsg = "Required"
            }

            // address validation
            inputFieldValidation(address) {
                isAddressError = true
                addressErrorMsg = "Required"
            }

            // description validation
            inputFieldValidation(description) {
                isDescriptionError = true
                descriptionErrorMsg = "Required"
            }

            // phone number validation
            inputFieldValidation(phoneNumber) {
                isPhoneNumberError = true
                phoneNumberErrorMsg = "Required"
            }

            if (phoneNumber.length < 10) {
                isPhoneNumberError = true
                phoneNumberErrorMsg = "Minimum 10 characters"
            } else if (phoneNumber.length > 15) {
                isPhoneNumberError = true
                phoneNumber = "Maximum 15 characters"
            }

            // Checking error availability
            if (!isRepairShopError && !isAddressError && !isDescriptionError && !isPhoneNumberError) {

                var newSchedules = mutableListOf<Schedule>()
                for (i in 0..6) {
                    val day = days[i]
                    val status = if (selectedDay[days[i]] == true) "open" else "closed"

                    if (i < 5) { // weekdays
                        val operationalHours =
                            if (status == "open") "${startWeekdayHours}—${endWeekdaysHours}" else null

                        newSchedules.add(
                            Schedule(
                                day = day,
                                status = status,
                                operationalHours = operationalHours
                            )
                        )

                    } else { // weekend
                        val operationalHours =
                            if (status == "open") "${startWeekendHours}—${endWeekendHours}" else null

                        newSchedules.add(
                            Schedule(
                                day = day,
                                status = status,
                                operationalHours = operationalHours
                            )
                        )

                    }
                }
                val photo =
                    if (isUserUploadFile) "$userId.jpg" else "default.jpg"

                val newRepairShopData = RepairShop(
                    name = repairShopName,
                    address = address,
                    description = description,
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    phone_number = phoneNumber,
                    photo = photo,
                    schedule = newSchedules
                )

                // upload file process
                if (isUserUploadFile) {
                    val storageRef = storage.reference
                    val userRef =
                        storageRef.child("images/$userId.${file.extension}")

                    userRef.putFile(file.toUri()).addOnSuccessListener {
                        // update process
                        db.collection("repairShops").document(userId)
                            .set(newRepairShopData).addOnSuccessListener {
                                isLoading = false
                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                userRef.delete()
                            }
                    }
                } else {
                    // update process
                    db.collection("repairShops").document(userId)
                        .set(newRepairShopData).addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                }
                isUserUploadFile = false
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
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
                maxLines = 2,
                label = "Address"
            )
            InputField(
                value = description,
                onValueChange = { description = it },
                isError = isDescriptionError,
                errorMessage = descriptionErrorMsg,
                maxLines = 3,
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
            EditRepairShopScreen(navController = rememberNavController())
        }
    }
}
