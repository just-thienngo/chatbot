package com.example.commom_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date
@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "chatId") val chatId: String? = null,
    @Json(name = "lastMessage") val lastMessage: String? = null,
    @Json(name = "timestamp") val timestamp: Date? = null
)
