package com.example.commom_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "fullName") val fullName: String = "",
    @Json(name = "email") val email: String = ""
)
