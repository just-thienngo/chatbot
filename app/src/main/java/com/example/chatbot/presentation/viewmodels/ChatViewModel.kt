package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.model.Message
import com.example.chatbot.data.remote.ChatApiService
import com.example.chatbot.domain.repository.ChatRepository
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatApiService: ChatApiService,
    private val chatRepository: ChatRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentChatId: String? = null
    private val userUid = firebaseAuth.currentUser?.uid

    companion object {
        private const val TAG = "ChatViewModel"
    }


    fun setChatId(chatId: String){
        currentChatId = chatId
        fetchMessages()
    }
    fun sendMessage(message: String) {
        viewModelScope.launch {
            if (currentChatId == null) {
                currentChatId = UUID.randomUUID().toString()
                currentChatId?.let { chatRepository.createNewChat(it) }
            }
            val myMessage = Message(message, Message.SENT_BY_ME)
            _messages.value += myMessage
            currentChatId?.let {  chatRepository.sendMessage(myMessage, it) }

            _isLoading.value = true
            when (val response = chatApiService.generateResponse(message)) {
                is Resource.Success -> {
                    response.data?.let { reply ->
                        val botMessage = Message(reply, Message.SENT_BY_BOT)
                        _messages.value += botMessage
                        currentChatId?.let { chatRepository.sendMessage(botMessage, it) }
                    }
                }
                is Resource.Error -> {
                    val errorMessage = Message(
                        response.message ?: "An error occurred",
                        Message.SENT_BY_BOT
                    )
                    _messages.value += errorMessage
                    currentChatId?.let { chatRepository.sendMessage(errorMessage, it) }
                }
                else -> Unit
            }
            _isLoading.value = false
        }
    }
    private fun updateLastMessage(chatId: String, message: String){
        viewModelScope.launch {
            chatRepository.updateLastMessage(chatId, message)
        }
    }
    private fun updateChatTimestamp(chatId: String){
        viewModelScope.launch {
            chatRepository.updateChatTimestamp(chatId)
        }
    }
    private fun createNewChat(){
        viewModelScope.launch {
            currentChatId = UUID.randomUUID().toString()
            currentChatId?.let { chatRepository.createNewChat(it) }
        }
        fetchMessages()
    }
    private fun fetchMessages() {
        currentChatId?.let { chatId ->
            viewModelScope.launch {
                chatRepository.fetchMessages(chatId).collect{
                    _messages.value = it
                }
            }
        }
    }
}