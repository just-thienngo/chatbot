package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.model.Chat
import com.example.chatbot.domain.repository.ChatRepository
import com.example.chatbot.domain.usecase.chat.FetchAllChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChatViewModel @Inject constructor(
    private val fetchAllChatsUseCase: FetchAllChatsUseCase
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>(emptyList())
    val chats: LiveData<List<Chat>> = _chats
    companion object {
        private const val TAG = "AllChatViewModel"
    }
    init {
        fetchAllChats()
    }
    private fun fetchAllChats() {
        viewModelScope.launch {
            fetchAllChatsUseCase().collect { chatList ->
                _chats.value = chatList
            }
        }
    }
}