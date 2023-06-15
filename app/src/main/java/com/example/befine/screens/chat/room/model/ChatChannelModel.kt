package com.example.befine.screens.chat.room.model


data class ChatChannelModel(
    val client: ClientModel? = ClientModel(),
    val lastMessage: String? = "",
    val lastSenderId: String? = "",
    val unreadedMessage: Int? = 0,
    val lastDatetime: String? = "",
    val repairShop: RepairShopModel? = RepairShopModel()
)