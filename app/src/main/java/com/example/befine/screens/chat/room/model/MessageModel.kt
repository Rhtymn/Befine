package com.example.befine.screens.chat.room.model

data class MessageModel(
    val channelId: String? = "",
    val datetime: String? = "",
    val message: String? = "",
    val isRead: Boolean? = false,
    val receiverID: String? = "",
    val senderID: String? = ""
)