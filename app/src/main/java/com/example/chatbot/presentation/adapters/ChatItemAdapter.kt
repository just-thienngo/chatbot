package com.example.chatbot.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.data.model.Chat

class ChatItemAdapter(private val chatList: List<Chat>) :
    RecyclerView.Adapter<ChatItemAdapter.ChatItemViewHolder>() {

    inner class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_tittle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_history, parent, false)
        return ChatItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chat = chatList[position]
        Log.d("ChatItemAdapter", "onBindViewHolder: position=$position, lastMessage=${chat.lastMessage}")
        holder.titleTextView.text = chat.lastMessage
    }

    override fun getItemCount() = chatList.size
}