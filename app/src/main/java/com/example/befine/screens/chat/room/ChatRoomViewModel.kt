package com.example.befine.screens.chat.room

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.screens.chat.room.model.ClientModel
import com.example.befine.screens.chat.room.model.MessageModel
import com.example.befine.screens.chat.room.model.RepairShopModel
import com.example.befine.utils.ROLE
import com.google.firebase.database.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomViewModel : ViewModel() {
    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://befine-f1996-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val _state = MutableLiveData(ChatRoomState())
    val state: LiveData<ChatRoomState> = _state

    private val _message = MutableLiveData("")
    val snapshot: LiveData<String> = _message

    private var _messagesList = mutableStateListOf<MessageModel>()
    val messagesList: SnapshotStateList<MessageModel> = _messagesList

    fun addMessage(newMessage: MessageModel) {
        _messagesList.add(newMessage)
    }

    fun newMessageListener(channelID: String) {
        database.child("Messages").child(channelID)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val channelId = snapshot.child("channelId").value
                    val datetime = snapshot.child("datetime").value
                    val messageItm = snapshot.child("message").value
                    val read = snapshot.child("read").value.toString() == "true"
                    val receiverID = snapshot.child("receiverID").value
                    val senderID = snapshot.child("senderID").value
                    val messageItem = MessageModel(
                        channelId = channelId.toString(),
                        datetime = datetime.toString(),
                        message = messageItm.toString(),
                        isRead = read,
                        receiverID = receiverID.toString(),
                        senderID = senderID.toString()
                    )
                    if (!_messagesList.isEmpty()) {
                        addMessage(messageItem)
                        Log.d(TAG, messageItem.toString())
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getAllMessages(channelID: String) {
        runBlocking {
            database.child("Messages").child(channelID).get()
                .addOnSuccessListener { messages ->
                    for (message in messages.children) {
                        val channelId = message.child("channelId").value
                        val datetime = message.child("datetime").value
                        val messageItm = message.child("message").value
                        val read = message.child("read").value.toString() == "true"
                        val receiverID = message.child("receiverID").value
                        val senderID = message.child("senderID").value
                        val messageItem = MessageModel(
                            channelId = channelId.toString(),
                            datetime = datetime.toString(),
                            message = messageItm.toString(),
                            isRead = read,
                            receiverID = receiverID.toString(),
                            senderID = senderID.toString()
                        )
                        _messagesList.add(messageItem)
                    }
                }
        }
    }

    fun onChangeMessageValue(value: String) {
        _message.value = value
    }

    @SuppressLint("SimpleDateFormat")
    fun onSendMessage() {
        if (snapshot.value == "") return
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
            lastMessage = snapshot.value.toString(),
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
        database.child("Messages").child(channelID).push()
            .setValue(messageItem)
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