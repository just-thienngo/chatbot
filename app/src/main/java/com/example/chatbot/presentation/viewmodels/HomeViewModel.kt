package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.model.Chat
import com.example.chatbot.data.model.ChatHistoryItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _chatHistory = MutableLiveData<List<ChatHistoryItem>>(emptyList())
    val chatHistory: LiveData<List<ChatHistoryItem>> = _chatHistory

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        fetchChats()
    }

    private fun fetchChats() {
        firestore.collection("chats")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                val chats = querySnapshot?.documents?.mapNotNull { document ->
                    val chat = document.toObject(Chat::class.java)
                    chat?.copy(chatId = document.id, timestamp = document.getTimestamp("timestamp")?.toDate() )
                } ?: emptyList()

                val groupedChats = chats.groupBy { chat ->
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    dateFormat.format(chat.timestamp ?: Date())
                }.map { (dateString, chatList) ->
                    ChatHistoryItem(dateString, chatList, chats.firstOrNull{it.timestamp != null}?.timestamp)
                }.sortedByDescending {
                    it.date
                }
                _chatHistory.value = groupedChats
                Log.d(TAG, "fetchChats: chats=$groupedChats")
            }
    }
    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            firestore.collection("chats").document(chatId).delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Chat deleted successfully with ID: $chatId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error deleting chat with ID: $chatId", e)
                }
        }
    }
}