package com.example.chat.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commom_entity.Chat
import com.example.usecase.chat.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>(emptyList())
    val chats: LiveData<List<Chat>> = _chats

    init {
        fetchAllChats()
    }

    private fun fetchAllChats() {
        viewModelScope.launch {
            chatUseCase.fetchAllChats().collect { chatList ->
                _chats.value = chatList
            }
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            chatUseCase.deleteChat(chatId)
        }
    }
}
