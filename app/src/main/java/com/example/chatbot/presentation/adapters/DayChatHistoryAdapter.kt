package com.example.chatbot.presentation.adapters

import ChatItemAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.data.model.ChatHistoryItem
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DayChatHistoryAdapter(
    private val chatHistoryItemList: List<ChatHistoryItem>,
    private val onDeleteChat: (String) -> Unit,
    private val onChatClick: (String) -> Unit
) :
    RecyclerView.Adapter<DayChatHistoryAdapter.ChatHistoryViewHolder>() {

    inner class ChatHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
        val chatHistoryRecyclerView: RecyclerView = itemView.findViewById(R.id.rcv_chatHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_chat_history, parent, false)
        return ChatHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHistoryViewHolder, position: Int) {
        val chatHistoryItem = chatHistoryItemList[position]
        val lastMessageTime =
            chatHistoryItem.chats.maxByOrNull { it.timestamp?.time ?: 0 }?.timestamp
        holder.timeTextView.text = getTimeAgo(lastMessageTime)
        val sortedChats = chatHistoryItem.chats.sortedByDescending {
            it.timestamp
        }
        val chatItemAdapter =
            ChatItemAdapter(sortedChats, onDeleteChat = onDeleteChat, onChatClick = onChatClick)
        holder.chatHistoryRecyclerView.apply {
            adapter = chatItemAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getTimeAgo(date: Date?): String {
        if (date == null) return "Unknown"

        val now = Date()
        val diffInMillis = now.time - date.time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        // Get calendar instances for more accurate month/year calculations
        val calendar = Calendar.getInstance().apply { time = date }
        val nowCalendar = Calendar.getInstance()

        // Calculate months and years difference
        var months = (nowCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)) * 12 +
                (nowCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH))
        val years = months / 12
        months %= 12

        return when {
            // Recent times
            seconds < 60 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"

            // Check for yesterday (accounting for time of day)
            days == 1L || (days == 2L && hours < 24) -> "Yesterday"

            // Days and weeks
            days < 7 -> "$days days ago"
            days < 30 -> "${days / 7} weeks ago"

            // Months and years
            years == 0 && months > 0 -> "$months months ago"
            years > 0 -> if (months > 0) {
                "$years years, $months months ago"
            } else {
                "$years years ago"
            }

            // Fallback for very old dates
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        }
    }

    override fun getItemCount() = chatHistoryItemList.size
}