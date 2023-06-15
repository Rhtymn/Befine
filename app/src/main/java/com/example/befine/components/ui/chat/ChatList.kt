package com.example.befine.components.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.befine.screens.chat.room.model.MessageModel
import com.example.befine.utils.*
import java.util.Calendar

@Composable
fun ChannelDatetime(datetime: Calendar, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = chatRoomDatetime(datetime.time.toString()),
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

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

        var headDate: Calendar = Calendar.getInstance()
        val currentMessageDate: Calendar = Calendar.getInstance()
        var isAlreadyWrited: Boolean = false
        if (messageList.isNotEmpty()) {
            headDate.time = convertStringToCalendar(messageList[0].datetime.toString())
        }
        messageList.distinct().forEach {
            currentMessageDate.time = convertStringToCalendar(it.datetime.toString())

            if (headDate.get(Calendar.DAY_OF_MONTH) != currentMessageDate.get(Calendar.DAY_OF_MONTH)) {
                headDate.time = currentMessageDate.time
                isAlreadyWrited = false
            }

            if (headDate.get(Calendar.DAY_OF_MONTH) == currentMessageDate.get(Calendar.DAY_OF_MONTH) && !isAlreadyWrited) {
                ChannelDatetime(
                    datetime = currentMessageDate,
                )
                isAlreadyWrited = true
            }

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
                    time = messageTime(
                        it.datetime.toString(),
                    ),
                    backgroundColor = Color.White,
                    timeColor = Color.Gray
                )
            }
        }

        LaunchedEffect(scrollState) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
}