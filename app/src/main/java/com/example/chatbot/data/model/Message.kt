package com.example.chatbot.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val message: String,
    val sentBy: String,
    @ServerTimestamp val timestamp: Date? = null
) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
} 