package com.example.befine.model

import androidx.compose.runtime.MutableState

data class RepairShop(
    val name: String? = null,
    val address: String? = null,
    val description: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val phone_number: String? = null,
    val photo: String? = null,
    val schedule: List<Schedule>? = null
)
