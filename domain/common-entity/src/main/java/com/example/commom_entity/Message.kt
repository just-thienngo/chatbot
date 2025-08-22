package com.example.commom_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date
import java.util.UUID // Thêm import này

@JsonClass(generateAdapter = true)
data class Message(
    val id: String = UUID.randomUUID().toString(), // THÊM: ID duy nhất cho mỗi tin nhắn
    @Json(name = "message") val message: String = "",
    @Json(name = "sentBy") val sentBy: String = "",
    @Json(name = "timestamp") val timestamp: Date? = null,
    val isTyping: Boolean = false // THÊM: Thuộc tính để kiểm soát hiệu ứng typing
) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
}