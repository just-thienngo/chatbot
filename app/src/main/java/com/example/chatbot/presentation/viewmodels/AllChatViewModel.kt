package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatbot.data.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>(emptyList())
    val chats: LiveData<List<Chat>> = _chats
    private val userUid = firebaseAuth.currentUser?.uid

    companion object {
        private const val TAG = "AllChatViewModel"
    }

    init {
        fetchAllChats()
    }

    private fun fetchAllChats() {
        userUid?.let {
            firestore.collection("users").document(it)
                .collection("chats")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val chats = querySnapshot?.documents?.mapNotNull { document ->
                        document.toObject(Chat::class.java)?.copy(chatId = document.id)
                    } ?: emptyList()
                    _chats.value = chats
                    Log.d(TAG, "fetchChats: chats=$chats")
                }
        }
    }
}