package com.example.commom_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

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

