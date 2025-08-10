package com.example.chatbot.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

// API Request Models
@JsonClass(generateAdapter = true)
data class ChatRequest(
    @Json(name = "contents") val contents: List<Content>
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String
)

// API Response Models
@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "candidates") val candidates: List<Candidate>
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content
)

// Database Models (for local storage)
@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "message") val message: String = "",
    @Json(name = "sentBy") val sentBy: String = "",
    @Json(name = "timestamp") val timestamp: Date? = null
) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
}

@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "chatId") val chatId: String? = null,
    @Json(name = "lastMessage") val lastMessage: String? = null,
    @Json(name = "timestamp") val timestamp: Date? = null
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "fullName") val fullName: String = "",
    @Json(name = "email") val email: String = ""
)

@JsonClass(generateAdapter = true)
data class ChatHistoryItem(
    @Json(name = "date") val date: String = "",
    @Json(name = "chats") val chats: List<Chat> = emptyList(),
    @Json(name = "timestamp") val timestamp: Date? = null
) 