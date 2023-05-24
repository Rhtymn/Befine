package com.example.befine.model


data class RepairShop (
    val id: Int,
    val name: String,
    val description: String? = null,
    val photo: String? = null,
    val address: String,
    val phoneNumber: String,
    val latitude: String,
    val longitude: String,
    val status: String
)