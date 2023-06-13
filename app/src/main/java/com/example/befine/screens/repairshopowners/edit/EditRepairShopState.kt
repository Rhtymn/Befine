package com.example.befine.screens.repairshopowners.edit

data class EditRepairShopFieldState(
    val repairShopName: String = "",
    val isRepairShopNameError: Boolean = false,
    val repairShopNameErrorMsg: String = "",

    val address: String = "",
    val isAddressError: Boolean = false,
    val addressErrorMsg: String = "",

    val description: String = "",
    val isDescriptionError: Boolean = false,
    val descriptionErrorMsg: String = "",

    val phoneNumber: String = "",
    val isPhoneNumberError: Boolean = false,
    val phoneNumberErrorMsg: String = "",

    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
    val formErrorMsg: String = ""
)

data class LocationFieldState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class SchedulesState(
    val startWeekdayHours: String = "00:00",
    val endWeekdayHours: String = "00:00",
    val startWeekendHours: String = "00:00",
    val endWeekendHours: String = "00:00",

    val selectedDay: Map<String, Boolean> = mapOf(
        "monday" to false,
        "tuesday" to false,
        "wednesday" to false,
        "thursday" to false,
        "friday" to false,
        "saturday" to false,
        "sunday" to false
    )
)