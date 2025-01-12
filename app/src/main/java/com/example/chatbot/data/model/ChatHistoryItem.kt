package com.example.chatbot.data.model

import java.util.Date

data class ChatHistoryItem(
    val dateString: String,
    val chats: List<Chat>,
    val date: Date?
)