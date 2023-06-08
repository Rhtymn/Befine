package com.example.befine.screens.repairshopowners

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.example.befine.BuildConfig
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.Screen
import com.example.befine.R
import com.example.befine.components.authentication.InputField
import com.example.befine.components.ui.repairshop.*
import com.example.befine.utils.ActiveTimePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun convertTime(hour: Int, minute: Int): String {
    val convertedHour = if (hour > 9) hour else "0$hour"
    val convertedMinute = if (minute > 9) minute else "0$minute"
    return "$convertedHour:$convertedMinute"
}


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRepairShopScreen(
) {
    // Context for this composable component
    val context = LocalContext.current


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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical)
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
            value = "",
            onValueChange = {},
            label = "Repair Shop Name",
            modifier = Modifier.padding(top = 10.dp)
        )
        InputField(value = "", onValueChange = {}, label = "Address")
        InputField(value = "", onValueChange = {}, label = "Description")
        InputField(value = "", onValueChange = {}, label = "Telp / Phone Number")
        PickerButton(
            text = "Select Location",
            icon = { Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "") }
        )
        TimeInputRange(
            label = "Weekday Hours",
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
                    Text(text = "Select time", modifier = Modifier.padding(bottom = 10.dp))
                    val state = when (activeSelectedTimePicker) {
                        ActiveTimePicker.START_WEEKDAYS -> startWeekdayHoursState
                        ActiveTimePicker.END_WEEKDAYS -> endWeekdayHoursState
                        ActiveTimePicker.START_WEEKEND -> startWeekendHoursState
                        else -> endWeekendHoursState
                    }
                    TimePicker(state = state)
                    Row(modifier = Modifier.align(Alignment.End)) {
                        DialogButton(onClick = { showTimePickerDialog = false }, text = "Cancel")
                        DialogButton(onClick = {
                            showTimePickerDialog = false
                            when (activeSelectedTimePicker) {
                                ActiveTimePicker.START_WEEKDAYS -> startWeekdayHours = convertTime(
                                    startWeekdayHoursState.hour,
                                    startWeekdayHoursState.minute
                                )
                                ActiveTimePicker.END_WEEKDAYS -> endWeekdaysHours = convertTime(
                                    endWeekdayHoursState.hour,
                                    endWeekdayHoursState.minute
                                )
                                ActiveTimePicker.START_WEEKEND -> startWeekendHours = convertTime(
                                    startWeekendHoursState.hour,
                                    startWeekendHoursState.minute
                                )
                                ActiveTimePicker.END_WEEKEND -> endWeekendHours = convertTime(
                                    endWeekendHoursState.hour,
                                    endWeekendHoursState.minute
                                )
                            }
                        }, text = "OK")
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
