package com.example.befine.screens.chat.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatRoomViewModel : ViewModel() {
    private val _state = MutableLiveData(ChatRoomState())
    val state: LiveData<ChatRoomState> = _state

    fun setChatRoomState(chatRoomState: ChatRoomState) {
        _state.value = _state.value?.copy(
            name = chatRoomState.name,
            photo = chatRoomState.photo,
            receiverId = chatRoomState.receiverId,
            senderId = chatRoomState.senderId
        )
    }
}