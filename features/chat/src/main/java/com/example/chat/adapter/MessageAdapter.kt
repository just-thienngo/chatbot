package com.example.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.commom_entity.Message

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

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
        val message = messageList[position]

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

    override fun getItemCount() = messageList.size
} 