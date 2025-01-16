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
        val calendar = Calendar.getInstance()
        calendar.time = date

        val nowCalendar = Calendar.getInstance()
        val diffDays =
            TimeUnit.MILLISECONDS.toDays(nowCalendar.timeInMillis - calendar.timeInMillis)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            diffDays == 0L -> when {
                seconds < 60 -> "Just now"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                else -> "today"
            }

            diffDays == 1L -> "Yesterday"
            days < 7 -> "$days days ago"
            weeks < 4 -> "$weeks weeks ago"
            months < 12 -> "$months months ago"
            years < 5 -> "$years years ago"
            else -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.format(date)
            }
        }
    }

    override fun getItemCount() = chatHistoryItemList.size
}