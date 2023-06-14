package com.example.befine.screens.chat.room.model

data class MessageModel(
    val channelId: String? = null,
    val datetime: String? = null,
    val message: String? = null,
    val isRead: Boolean? = null,
    val receiverID: String? = null,
    val senderID: String? = null
)