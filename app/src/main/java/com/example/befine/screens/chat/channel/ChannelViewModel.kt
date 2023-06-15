package com.example.befine.screens.chat.channel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.screens.chat.room.model.ChatChannelModel
import com.example.befine.screens.chat.room.model.ClientModel
import com.example.befine.screens.chat.room.model.RepairShopModel
import com.example.befine.utils.ROLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChannelViewModel() : ViewModel() {
    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://befine-f1996-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val _channelList = mutableStateListOf<ChatChannelModel>()
    val channelList: SnapshotStateList<ChatChannelModel> = _channelList

    private fun isChannelListContains(chatChannelModel: ChatChannelModel): Boolean {
        return _channelList.any {
            it.client == chatChannelModel.client && it.repairShop == chatChannelModel.repairShop
        }
    }

    fun updateChannelList(chatChannelModel: ChatChannelModel) {
        Log.d(TAG, "update")
        if (isChannelListContains(chatChannelModel)) {
            val index = _channelList.withIndex().first {
                it.value.client == chatChannelModel.client && it.value.repairShop == chatChannelModel.repairShop
            }.index
            _channelList[index] = chatChannelModel
        }
    }

    fun channelListener(role: String) {
        val roleChild = if (role == ROLE.CLIENT) ROLE.CLIENT else ROLE.REPAIR_SHOP_OWNER
        database.child("chatChannel").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child(roleChild)
                        .child("id").value.toString() == auth.currentUser?.uid
                ) {
                    val client = ClientModel(
                        id = snapshot.child("client").child("id").value.toString(),
                        name = snapshot.child("client").child("name").value.toString()
                    )
                    val repairShop = RepairShopModel(
                        id = snapshot.child("repairShop").child("id").value.toString(),
                        name = snapshot.child("repairShop")
                            .child("name").value.toString(),
                        photo = snapshot.child("repairShop")
                            .child("photo").value.toString()
                    )
                    val channelItem = ChatChannelModel(
                        client = client,
                        lastMessage = snapshot.child("lastMessage").value.toString(),
                        lastSenderId = snapshot.child("lastSenderId").value.toString(),
                        unreadedMessage = snapshot.child("unreadedMessage").value.toString()
                            .toInt(),
                        repairShop = repairShop,
                        lastDatetime = snapshot.child("lastDatetime").value.toString()
                    )
                    Log.d(TAG, channelItem.toString())
                    if (!isChannelListContains(channelItem)) {
                        _channelList.add(channelItem)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val client = ClientModel(
                    id = snapshot.child("client").child("id").value.toString(),
                    name = snapshot.child("client").child("name").value.toString()
                )
                val repairShop = RepairShopModel(
                    id = snapshot.child("repairShop").child("id").value.toString(),
                    name = snapshot.child("repairShop")
                        .child("name").value.toString(),
                    photo = snapshot.child("repairShop")
                        .child("photo").value.toString()
                )
                val channelItem = ChatChannelModel(
                    client = client,
                    lastMessage = snapshot.child("lastMessage").value.toString(),
                    lastSenderId = snapshot.child("lastSenderId").value.toString(),
                    unreadedMessage = snapshot.child("unreadedMessage").value.toString()
                        .toInt(),
                    repairShop = repairShop,
                    lastDatetime = snapshot.child("lastDatetime").value.toString()
                )
                updateChannelList(channelItem)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getAllChannelList(role: String) {
        val roleChild = if (role == ROLE.CLIENT) ROLE.CLIENT else "repairShop"
        Log.d(TAG, roleChild.toString())
        database.child("chatChannel").orderByKey().get()
            .addOnSuccessListener { chatChannels ->
                for (chatChannel in chatChannels.children) {
                    if (chatChannel.child(roleChild)
                            .child("id").value.toString() == auth.currentUser?.uid
                    ) {
                        val client = ClientModel(
                            id = chatChannel.child("client").child("id").value.toString(),
                            name = chatChannel.child("client").child("name").value.toString()
                        )
                        val repairShop = RepairShopModel(
                            id = chatChannel.child("repairShop").child("id").value.toString(),
                            name = chatChannel.child("repairShop")
                                .child("name").value.toString(),
                            photo = chatChannel.child("repairShop")
                                .child("photo").value.toString()
                        )
                        val channelItem = ChatChannelModel(
                            client = client,
                            lastMessage = chatChannel.child("lastMessage").value.toString(),
                            lastSenderId = chatChannel.child("lastSenderId").value.toString(),
                            unreadedMessage = chatChannel.child("unreadedMessage").value.toString()
                                .toInt(),
                            repairShop = repairShop,
                            lastDatetime = chatChannel.child("lastDatetime").value.toString()
                        )
                        if (!isChannelListContains(channelItem)) {
                            _channelList.add(channelItem)
                        }
                    }
                }
            }

    }

    companion object {
        const val TAG = "CHANNEL_VIEW_MODEL"
    }
}