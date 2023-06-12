package com.example.befine.model

import com.squareup.moshi.JsonClass

data class ChatRoomState(
    val name: String,
    val photo: String? = null,
    val receiverId: String,
    val senderId: String
)
