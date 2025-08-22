package com.example.chat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.common.utils.Resource
import com.example.commom_entity.Message
import com.example.usecase.chat.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    private val _firestoreMessages = MutableStateFlow<List<Message>>(emptyList())
    private val _temporaryTypingMessage = MutableStateFlow<Message?>(null)

    val messages: StateFlow<List<Message>> = combine(
        _firestoreMessages,
        _temporaryTypingMessage
    ) { firestoreMsgs, typingMsg ->
        val combinedList = if (typingMsg != null) {
            firestoreMsgs + typingMsg
        } else {
            firestoreMsgs
        }
        combinedList.sortedBy { it.timestamp ?: Date() }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentChatId: String? = null

    fun setChatId(chatId: String) {
        if (currentChatId != chatId) {
            currentChatId = chatId
            fetchMessages()
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            val chatId = currentChatId ?: UUID.randomUUID().toString().also { newChatId ->
                currentChatId = newChatId
                chatUseCase.createNewChat(newChatId)
                // THÊM DÒNG NÀY: Gọi fetchMessages để bắt đầu lắng nghe chat mới
                fetchMessages()
            }

            // Tin nhắn người dùng
            val myMessage = Message(
                message = messageText,
                sentBy = Message.SENT_BY_ME,
                timestamp = Date()
            )
            chatUseCase(myMessage, chatId)

            // TẠO và hiển thị tin nhắn typing tạm thời của bot NGAY LẬP TỨC
            val typingMessage = Message(
                message = "",
                sentBy = Message.SENT_BY_BOT,
                isTyping = true,
                timestamp = Date(System.currentTimeMillis() + 1)
            )
            _temporaryTypingMessage.value = typingMessage

            _isLoading.value = true

            // Gọi bot trả lời
            val botResponseResource = chatUseCase.getBotResponse(messageText)

            // Khi có phản hồi, LOẠI BỎ tin nhắn typing tạm thời
            _temporaryTypingMessage.value = null

            when (botResponseResource) {
                is Resource.Success -> {
                    botResponseResource.data?.let { reply ->
                        val botMessage = Message(
                            message = reply,
                            sentBy = Message.SENT_BY_BOT,
                            isTyping = false,
                            timestamp = Date()
                        )
                        chatUseCase(botMessage, chatId)
                    }
                }
                is Resource.Error -> {
                    val errorMessage = Message(
                        message = botResponseResource.message ?: "Đã xảy ra lỗi",
                        sentBy = Message.SENT_BY_BOT,
                        isTyping = false,
                        timestamp = Date()
                    )
                    chatUseCase(errorMessage, chatId)
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    private fun fetchMessages() {
        currentChatId?.let { chatId ->
            viewModelScope.launch {
                // Đảm bảo rằng việc thu thập được hủy bỏ và bắt đầu lại khi chatId thay đổi
                // Mặc dù collect { ... } sẽ chạy liên tục, nhưng nếu launch này được gọi lại,
                // nó sẽ tạo một collector mới cho chatId mới.
                chatUseCase.fetchMessages(chatId).collect { messagesFromFirestore ->
                    _firestoreMessages.value = messagesFromFirestore
                }
            }
        }
    }

    private fun updateLastMessage(chatId: String, message: String){
        viewModelScope.launch {
            chatUseCase.updateLastMessage(chatId, message)
        }
    }

    private fun updateChatTimestamp(chatId: String){
        viewModelScope.launch {
            chatUseCase.updateChatTimestamp(chatId)
        }
    }
}