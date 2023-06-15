package com.example.befine.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.dp
import com.example.befine.model.Schedule
import com.example.befine.screens.chat.room.ChatRoomState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

fun convertChatRoomStateToJSON(chatRoomState: ChatRoomState): String? {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(ChatRoomState::class.java).lenient()

    return jsonAdapter.toJson(chatRoomState)
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
    if (scheduleOnThisDay.operationalHours == null) {
        return STATUS.CLOSED
    }
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

fun getDay(): Int {
    val calendar = Calendar.getInstance()
    return if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6 else calendar.get(Calendar.DAY_OF_WEEK) - 2
}

fun capitalize(value: String): String {
    return "${value[0].uppercaseChar()}${value.slice(1 until value.length)}"
}

fun getYesterday(): Calendar {
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)

    yesterday.set(Calendar.HOUR_OF_DAY, 0)
    yesterday.set(Calendar.MINUTE, 0)
    yesterday.set(Calendar.SECOND, 0)
    yesterday.set(Calendar.MILLISECOND, 0)

    return yesterday
}

fun getThisDayAtMidnight(): Calendar {
    val thisDay = Calendar.getInstance()

    thisDay.set(Calendar.HOUR_OF_DAY, 0)
    thisDay.set(Calendar.MINUTE, 0)
    thisDay.set(Calendar.SECOND, 0)
    thisDay.set(Calendar.MILLISECOND, 0)

    return thisDay
}

fun convertStringToCalendar(datetime: String): Date {
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy")
    return dateFormat.parse(datetime)
}

fun channelChatDatetime(datetime: String): String {
    val yesterday = getYesterday()
    val thisDayAtMidnight = getThisDayAtMidnight()
    val lastMessageDatetime = Calendar.getInstance()
    lastMessageDatetime.time = convertStringToCalendar(datetime = datetime)

    Log.d("CHANNEL", yesterday.time.toString())
    Log.d("CHANNEL", lastMessageDatetime.time.toString())
    Log.d("CHANNEL", thisDayAtMidnight.time.toString())

    return if (lastMessageDatetime.after(yesterday) && lastMessageDatetime.before(thisDayAtMidnight)) {
        "yesterday"
    } else if (lastMessageDatetime.after(thisDayAtMidnight)) {
        val hour = lastMessageDatetime.get(Calendar.HOUR_OF_DAY)
        val minute = lastMessageDatetime.get(Calendar.MINUTE)
        val convertedHour = if (hour < 10) "0$hour" else "$hour"
        val convertedMinute = if (minute < 10) "0$minute" else "$minute"
        "$convertedHour:${convertedMinute}"
    } else {
        val month = lastMessageDatetime.get(Calendar.MONTH)
        val day = lastMessageDatetime.get(Calendar.DAY_OF_MONTH)
        val convertedMonth = if (month < 10) "0$month" else "$month"
        val convertedDay = if (day < 10) "0$day" else "$day"

        "$convertedDay/$convertedMonth/${
            lastMessageDatetime.get(Calendar.YEAR).toString().slice(2..3)
        }"
    }
}

fun messageTime(datetime: String): String {
    val messageDatetime = Calendar.getInstance()
    messageDatetime.time = convertStringToCalendar(datetime)

    val hour = messageDatetime.get(Calendar.HOUR_OF_DAY)
    val minute = messageDatetime.get(Calendar.MINUTE)
    val convertedHour = if (hour < 10) "0$hour" else "$hour"
    val convertedMinute = if (minute < 10) "0$minute" else "$minute"

    return "$convertedHour:$convertedMinute"
}
