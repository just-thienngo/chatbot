package com.example.commom_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ChatHistoryItem(
    @Json(name = "date") val date: String = "",
    @Json(name = "chats") val chats: List<Chat> = emptyList(),
    @Json(name = "timestamp") val timestamp: Date? = null
)
