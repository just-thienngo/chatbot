package com.example.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter // Import ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.commom_entity.Message

// Kế thừa ListAdapter và truyền vào MessageDiffCallback
class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftChatView: LinearLayout = itemView.findViewById(R.id.left_chat_view)
        val rightChatView: LinearLayout = itemView.findViewById(R.id.right_chat_view)
        val leftTextView: TextView = itemView.findViewById(R.id.left_chat_text_view)
        val rightTextView: TextView = itemView.findViewById(R.id.right_chat_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        // Sử dụng getItem(position) thay vì truy cập messageList trực tiếp
        val message = getItem(position)

        when (message.sentBy) {
            Message.SENT_BY_ME -> {
                holder.leftChatView.visibility = View.GONE
                holder.rightChatView.visibility = View.VISIBLE
                holder.rightTextView.text = message.message
            }
            else -> {
                holder.rightChatView.visibility = View.GONE
                holder.leftChatView.visibility = View.VISIBLE
                holder.leftTextView.text = message.message
            }
        }
    }


}
class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {

        return oldItem.timestamp == newItem.timestamp && oldItem.sentBy == newItem.sentBy

    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}