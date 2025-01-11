package com.example.chatbot.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat(
    val chatId: String? = null,
    val lastMessage: String? = null,
    @ServerTimestamp val timestamp: Date? = null
)