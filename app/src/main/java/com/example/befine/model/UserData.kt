package com.example.befine.model

enum class ROLE {
    REGULAR_USER,
    REPAIR_SHOP_OWNER
}

data class UserData(
    val name: String,
    val role: String
)