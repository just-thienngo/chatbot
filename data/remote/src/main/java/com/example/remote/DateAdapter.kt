package com.example.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    
    @ToJson
    fun toJson(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }
    
    @FromJson
    fun fromJson(dateString: String?): Date? {
        return dateString?.let { 
            try {
                dateFormat.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }
} 