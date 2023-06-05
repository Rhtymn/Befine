package com.example.befine.model

data class RepairShopData(
    val userId: String,
    val address: String = "",
    val description: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val phone_number: String = "",
    val photo: String = "default.jpg",
)
