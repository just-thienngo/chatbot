package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatbot.data.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>(emptyList())
    val chats: LiveData<List<Chat>> = _chats

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
                    chat?.copy(chatId = document.id)
                } ?: emptyList()
                _chats.value = chats
                Log.d(TAG, "fetchChats: chats=$chats")
            }
    }
}