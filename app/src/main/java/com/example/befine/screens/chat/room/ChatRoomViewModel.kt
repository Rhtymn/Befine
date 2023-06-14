package com.example.befine.screens.chat.room

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.preferences.PreferenceDatastore.Companion.userId
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.screens.chat.room.model.ClientModel
import com.example.befine.screens.chat.room.model.MessageModel
import com.example.befine.screens.chat.room.model.RepairShopModel
import com.example.befine.utils.ROLE
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ChatRoomViewModel : ViewModel() {
    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://befine-f1996-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val _state = MutableLiveData(ChatRoomState())
    val state: LiveData<ChatRoomState> = _state

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    fun onChangeMessageValue(value: String) {
        _message.value = value
    }

    @SuppressLint("SimpleDateFormat")
    fun onSendMessage() {
        if (message.value == "") return
        val client = ClientModel(id = state.value?.userId, name = state.value?.userName)
        val repairShop = RepairShopModel(
            id = state.value?.repairShopId,
            name = state.value?.repairShopName,
            photo = state.value?.repairShopPhoto
        )
        val lastSenderId =
            if (state.value?.senderRole == ROLE.CLIENT) state.value?.userId else state.value?.repairShopId
        val receiverId =
            if (state.value?.senderRole == ROLE.CLIENT) state.value?.repairShopId else state.value?.userId

        val chatChannel = ChatChannelModel(
            client = client,
            lastMessage = message.value.toString(),
            lastSenderId = lastSenderId,
            repairShop = repairShop
        )

        // Update ChatChannel
        val channelID = "${state.value?.userId}_${state.value?.repairShopId}"
        database.child("chatChannel").child(channelID)
            .setValue(chatChannel).addOnFailureListener {
                Log.d(TAG, it.message.toString())
            }

        // Create a message
        val dateTime = SimpleDateFormat().format(Date())
        val messageItem = MessageModel(
            channelId = channelID,
            message = _message.value.toString(),
            isRead = false,
            receiverID = receiverId,
            senderID = lastSenderId,
            datetime = dateTime
        )
        database.child("Messages").child(UUID.randomUUID().toString()).setValue(messageItem)
            .addOnFailureListener {
                Log.d(TAG, it.message.toString())
            }

        _message.value = ""
    }

    fun setChatRoomState(chatRoomState: ChatRoomState) {
        _state.value = _state.value?.copy(
            repairShopName = chatRoomState.repairShopName,
            repairShopPhoto = chatRoomState.repairShopPhoto,
            repairShopId = chatRoomState.repairShopId,
            userId = chatRoomState.userId,
            userName = chatRoomState.userName,
            senderRole = chatRoomState.senderRole
        )
    }

    companion object {
        const val TAG = "CHAT_ROOM_VIEW_MODEL"
    }
}