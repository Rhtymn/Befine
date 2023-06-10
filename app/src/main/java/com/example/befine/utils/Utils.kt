package com.example.befine.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.unit.dp
import com.example.befine.model.Schedule
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object STATUS {
    const val OPEN = "open"
    const val CLOSED = "closed"
}

val days =
    listOf(
        Day.MONDAY,
        Day.TUESDAY,
        Day.WEDNESDAY,
        Day.THURSDAY,
        Day.FRIDAY,
        Day.SATURDAY,
        Day.SUNDAY
    )
object ActiveTimePicker {
    val START_WEEKDAYS = "startWeekdays"
    val END_WEEKDAYS = "endWeekdays"
    val START_WEEKEND = "startWeekend"
    val END_WEEKEND = "endWeekend"
}

object Screen {
    val paddingVertical = 16.dp
    val paddingHorizontal = 16.dp
}

object ROLE {
    const val CLIENT = "client"
    const val REPAIR_SHOP_OWNER = "repair_shop_owner"
}

object InputFieldError {
    const val REQUIRED = "Required"
}

object EmailError {
    const val INVALID_FORMAT = "Invalid email format"
}

object PasswordError {
    const val MIN_CHARS = "Minimum 8 characters"
}

object SignUpError {
    const val FAILED = "Register failed, try again later"
    const val EMAIL_IN_USE = "Email already in use"
    const val DEFAULT = "Server is down, please try again later"
}

object LoginError {
    const val INVALID = "Invalid email or password"
    const val DEFAULT = "Server is down, please try again later"
}

object Day {
    const val SUNDAY = "sunday"
    const val MONDAY = "monday"
    const val TUESDAY = "tuesday"
    const val WEDNESDAY = "wednesday"
    const val THURSDAY = "thursday"
    const val FRIDAY = "friday"
    const val SATURDAY = "saturday"
}

fun inputFieldValidation(value: String, callbackWhenEmpty: () -> Unit) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    }
}

fun isValidEmail(value: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

fun emailValidation(
    value: String,
    callbackWhenEmpty: () -> Unit,
    callbackWhenInvalidFormat: () -> Unit
) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    } else if (!isValidEmail(value)) {
        callbackWhenInvalidFormat()
    }
}

fun passwordValidation(
    value: String,
    callbackWhenEmpty: () -> Unit,
    callbackWhenLessThanEightChar: () -> Unit
) {
    if (value.isEmpty()) {
        callbackWhenEmpty()
    } else if (value.length < 8) {
        callbackWhenLessThanEightChar()
    }
}

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

fun isRepairShopOpen(schedule: List<Schedule>): String {
    // Current time
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val scheduleOnThisDay = schedule[if (day == 1) 6 else day - 2]
    val openHour = scheduleOnThisDay.operationalHours.toString().slice(0..1).toInt()
    val openMinute = scheduleOnThisDay.operationalHours.toString().slice(3..4).toInt()
    val closeHour = scheduleOnThisDay.operationalHours.toString().slice(6..7).toInt()
    val closeMinute = scheduleOnThisDay.operationalHours.toString().slice(9..10).toInt()

    return if (scheduleOnThisDay.status == "closed") {
        STATUS.CLOSED
    } else {
        if (hour in openHour..closeHour) {
            if (hour == closeHour && minute > closeMinute) {
                STATUS.CLOSED
            } else if (hour == openHour && minute < openMinute) {
                STATUS.CLOSED
            } else {
                STATUS.OPEN
            }
        } else {
            STATUS.CLOSED
        }
    }
}