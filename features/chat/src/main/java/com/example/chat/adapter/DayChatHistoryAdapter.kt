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
import com.example.code.common.utils.getTimeAgo


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
