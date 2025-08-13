package com.example.chat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.common.utils.Resource
import com.example.commom_entity.Message
import com.example.remote.ChatApiService
import com.example.remote.generateChatResponse
import com.example.usecase.chat.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatApiService: ChatApiService,
    private val chatUseCase: ChatUseCase
    // FirebaseAuth is not needed here as the repository handles it
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

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
                fetchMessages()
            }

            val myMessage = Message(
                message = messageText,
                sentBy = Message.SENT_BY_ME,
                timestamp = Date()
            )

            chatUseCase(myMessage, chatId)

            _isLoading.value = true

            when (val response = chatApiService.generateChatResponse(messageText)) {
                is Resource.Success -> {
                    response.data?.let { reply ->
                        val botMessage = Message(
                            message = reply,
                            sentBy = Message.SENT_BY_BOT,
                            timestamp = Date()
                        )
                        // FIX 1: Use the 'invoke' operator here as well
                        chatUseCase(botMessage, chatId)
                    }
                }
                is Resource.Error -> {
                    val errorMessage = Message(
                        message = response.message ?: "An error occurred",
                        sentBy = Message.SENT_BY_BOT,
                        timestamp = Date()
                    )
                    // FIX 1: Use the 'invoke' operator here too
                    chatUseCase(errorMessage, chatId)
                }
                // FIX 2: Add the 'else' branch to make the 'when' exhaustive
                else -> {
                    // Handle other cases like Loading or Unspecified, or do nothing
                }
            }
            _isLoading.value = false
        }
    }

    private fun fetchMessages() {
        currentChatId?.let { chatId ->
            viewModelScope.launch {
                chatUseCase.fetchMessages(chatId).collect { messagesFromFirestore ->
                    _messages.value = messagesFromFirestore
                }
            }
        }
    }

    // These helper functions remain unchanged
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