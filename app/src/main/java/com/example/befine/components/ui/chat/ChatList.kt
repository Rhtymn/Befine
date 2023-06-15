package com.example.befine.components.ui.chat

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.befine.screens.chat.room.model.MessageModel
import com.example.befine.utils.messageTime

@Composable
fun ChatList(modifier: Modifier = Modifier, messageList: List<MessageModel>, senderId: String) {
    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
        messageList.distinct().forEach {
            if (it.senderID == senderId) {
                ChatMessage(
                    value = it.message.toString(),
                    modifier = Modifier.align(Alignment.End),
                    time = messageTime(
                        it.datetime
                            .toString()
                    )
                )
            } else {
                ChatMessage(
                    value = it.message.toString(),
                    time = messageTime(it.datetime.toString())
                )
            }
        }

        LaunchedEffect(scrollState) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
}