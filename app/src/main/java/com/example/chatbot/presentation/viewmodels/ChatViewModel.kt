package com.example.chatbot.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.model.Message
import com.example.chatbot.data.remote.ChatApiService
import com.example.chatbot.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatApiService: ChatApiService
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _messages.value += Message(message, Message.SENT_BY_ME)
            _isLoading.value = true
            
            when (val response = chatApiService.generateResponse(message)) {
                is Resource.Success -> {
                    response.data?.let { reply ->
                        _messages.value += Message(reply, Message.SENT_BY_BOT)
                    }
                }
                is Resource.Error -> {
                    _messages.value += Message(
                        response.message ?: "An error occurred", 
                        Message.SENT_BY_BOT
                    )
                }
                else -> Unit
            }
            _isLoading.value = false
        }
    }
} 