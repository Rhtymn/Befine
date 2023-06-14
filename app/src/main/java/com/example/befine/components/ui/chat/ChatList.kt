package com.example.befine.components.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.befine.screens.chat.room.model.MessageModel

@Composable
fun ChatList(modifier: Modifier = Modifier, messageList: List<MessageModel>, senderId: String) {
    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
//        LazyColumn() {
//            items(
//                messageList,
//                key = { it.senderID.toString() + "${UUID.randomUUID()}".slice(0..8) }) {
//                if (it.senderID == senderId) {
//                    ChatMessage(
//                        value = it.message.toString(),
//                        modifier = Modifier.align(Alignment.End)
//                    )
//                } else {
//                    ChatMessage(value = it.message.toString())
//                }
//            }
//        }
        messageList.forEach {
            if (it.senderID == senderId) {
                ChatMessage(value = it.message.toString(), modifier = Modifier.align(Alignment.End))
            } else {
                ChatMessage(value = it.message.toString())
            }
        }
    }
}