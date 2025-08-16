package com.example.chat.adapter

import ChatItemAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.commom_entity.ChatHistoryItem
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DayChatHistoryAdapter(
    private val onDeleteChat: (String) -> Unit,
    private val onChatClick: (String) -> Unit
) : ListAdapter<ChatHistoryItem, DayChatHistoryAdapter.ChatHistoryViewHolder>(DayChatDiffCallback()) {

    inner class ChatHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
        val chatHistoryRecyclerView: RecyclerView = itemView.findViewById(R.id.rcv_chatHistory)
        val chatItemAdapter = ChatItemAdapter(onDeleteChat, onChatClick).also {
            chatHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_chat_history, parent, false)
        return ChatHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHistoryViewHolder, position: Int) {
        val chatHistoryItem = getItem(position)

        val lastMessageTime =
            chatHistoryItem.chats.maxByOrNull { it.timestamp?.time ?: 0 }?.timestamp
        holder.timeTextView.text = getTimeAgo(lastMessageTime)

        val sortedChats = chatHistoryItem.chats.sortedByDescending { it.timestamp }
        holder.chatItemAdapter.submitList(sortedChats)
    }

    private fun getTimeAgo(date: Date?): String {
        if (date == null) return "Unknown"

        val now = Date()
        val diffInMillis = now.time - date.time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        val calendar = Calendar.getInstance().apply { time = date }
        val nowCalendar = Calendar.getInstance()

        var months = (nowCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)) * 12 +
                (nowCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH))
        val years = months / 12
        months %= 12

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days == 1L || (days == 2L && hours < 24) -> "Yesterday"
            days < 7 -> "$days days ago"
            days < 30 -> "${days / 7} weeks ago"
            years == 0 && months > 0 -> "$months months ago"
            years > 0 -> if (months > 0) {
                "$years years, $months months ago"
            } else {
                "$years years ago"
            }
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        }
    }
}

class DayChatDiffCallback : DiffUtil.ItemCallback<ChatHistoryItem>() {
    override fun areItemsTheSame(oldItem: ChatHistoryItem, newItem: ChatHistoryItem): Boolean {
        // so sánh theo ngày hoặc key unique mà bạn định nghĩa cho ChatHistoryItem
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: ChatHistoryItem, newItem: ChatHistoryItem): Boolean {
        return oldItem == newItem
    }
}
