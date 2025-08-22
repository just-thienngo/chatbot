package com.example.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ChatItemBinding
import com.example.commom_entity.Message

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    inner class MessageViewHolder(val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)

        with(holder.binding) {
            when (message.sentBy) {
                Message.SENT_BY_ME -> {
                    // Tin nhắn của tôi (bên phải)
                    leftChatView.visibility = View.GONE
                    rightChatView.visibility = View.VISIBLE
                    rightChatTextView.text = message.message
                }
                else -> {
                    // Tin nhắn của bot (bên trái)
                    rightChatView.visibility = View.GONE
                    leftChatView.visibility = View.VISIBLE

                    if (message.isTyping) {
                        // HIỂN THỊ: Hoạt ảnh typing khi isTyping là true
                        leftChatTextView.visibility = View.GONE
                        typingAnimation.visibility = View.VISIBLE
                        typingAnimation.playAnimation() // Bắt đầu hoạt ảnh
                    } else {
                        // ẨN: Hoạt ảnh typing và hiển thị văn bản tin nhắn
                        typingAnimation.cancelAnimation() // Dừng hoạt ảnh
                        typingAnimation.visibility = View.GONE
                        leftChatTextView.visibility = View.VISIBLE
                        leftChatTextView.text = message.message
                    }
                }
            }
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        // CẬP NHẬT: So sánh bằng ID để DiffUtil nhận diện các item giống nhau
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        // So sánh toàn bộ đối tượng (nếu là data class, nó sẽ so sánh tất cả các thuộc tính)
        return oldItem == newItem
    }
}