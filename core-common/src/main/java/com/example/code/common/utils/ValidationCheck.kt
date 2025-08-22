package com.example.code.common.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun validateEmail(email: String):RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be emty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")
    return RegisterValidation.Sucsses
}
fun validatePassword(password:String):RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be emty")
    if (password.length < 6)
        return RegisterValidation.Failed("Password should contains 6 char")
    return RegisterValidation.Sucsses

}
 fun getTimeAgo(date: Date?): String {
    if (date == null) return "Unknown"
    val now = Date()
    val diffInMillis = now.time - date.time

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    val calendar = Calendar.getInstance().apply { time = date }
    val nowCalendar = Calendar.getInstance()

    // Kiểm tra xem ngày có phải là hôm nay không (dựa trên ngày lịch)
    val isToday = nowCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            nowCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

    // Kiểm tra xem ngày có phải là hôm qua không (dựa trên ngày lịch)
    val yesterdayCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = calendar.get(Calendar.YEAR) == yesterdayCalendar.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == yesterdayCalendar.get(Calendar.DAY_OF_YEAR)

    // Tính toán tháng và năm theo lịch
    var months = (nowCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)) * 12 +
            (nowCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH))
    val years = months / 12
    months %= 12 // Lấy số tháng còn lại sau khi đã tính số năm

    return when {
        // Hôm nay: Vừa xong, X phút trước, X giờ trước
        isToday && seconds < 60 -> "Just now"
        isToday && minutes < 60 -> "${minutes}m ago"
        isToday && hours < 24 -> "${hours}h ago"
        // Hôm qua (dựa trên ngày lịch)
        isYesterday -> "Yesterday"
        // X ngày trước (trong vòng 1 tuần)
        days < 7 -> "${days} day${if (days > 1) "s" else ""} ago"
        // X tuần trước (trong vòng 1 tháng)
        days < 30 -> {
            val numWeeks = days / 7
            "${numWeeks} week${if (numWeeks > 1) "s" else ""} ago"
        }
        // X tháng trước (trong vòng 1 năm)
        years == 0 && months > 0 -> "${months} month${if (months > 1) "s" else ""} ago"
        // X năm, Y tháng trước hoặc X năm trước
        years > 0 -> {
            val yearString = "${years} year${if (years > 1) "s" else ""}"
            if (months > 0) {
                "$yearString, ${months} month${if (months > 1) "s" else ""} ago"
            } else {
                "$yearString ago"
            }
        }
        // Quá cũ, hiển thị ngày tháng đầy đủ
        else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
    }
}