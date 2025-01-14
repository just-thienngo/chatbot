package com.example.chatbot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.model.Message
import com.example.chatbot.data.remote.ChatApiService
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
    private val firestore: FirebaseFirestore,
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
                createNewChat()
            }
            val myMessage = Message(message, Message.SENT_BY_ME)
            _messages.value += myMessage
            currentChatId?.let { addMessageToFirestore(myMessage, it) }

            _isLoading.value = true
            when (val response = chatApiService.generateResponse(message)) {
                is Resource.Success -> {
                    response.data?.let { reply ->
                        val botMessage = Message(reply, Message.SENT_BY_BOT)
                        _messages.value += botMessage
                        currentChatId?.let { addMessageToFirestore(botMessage, it) }
                    }
                }
                is Resource.Error -> {
                    val errorMessage = Message(
                        response.message ?: "An error occurred",
                        Message.SENT_BY_BOT
                    )
                    _messages.value += errorMessage
                    currentChatId?.let { addMessageToFirestore(errorMessage, it) }
                }
                else -> Unit
            }
            _isLoading.value = false
        }
    }

    private fun addMessageToFirestore(message: Message, chatId: String) {
        val chatDocument =  userUid?.let {  firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }
        chatDocument?.collection("messages")?.add(message)
            ?.addOnSuccessListener { documentReference ->
                Log.d(TAG, "Message added to Firestore with ID: ${documentReference.id} in chat $chatId")
                if (message.sentBy == Message.SENT_BY_ME) {
                    updateLastMessage(chatId, message.message)
                    updateChatTimestamp(chatId)
                }
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error adding message to Firestore", e)
            }
    }
    private fun updateLastMessage(chatId: String, message: String) {
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }?.update("lastMessage", message)
            ?.addOnSuccessListener {
                Log.d(TAG, "Last message updated for chat $chatId")
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error updating last message for chat $chatId", e)
            }
    }
    private fun updateChatTimestamp(chatId: String){
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }?.update("timestamp", FieldValue.serverTimestamp())
            ?.addOnSuccessListener {
                Log.d(TAG, "timestamp chat updated for chat $chatId")
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error timestamp chat updated for chat $chatId", e)
            }
    }
    private fun createNewChat(){
        currentChatId = UUID.randomUUID().toString()
        userUid?.let {
            firestore.collection("users").document(it)
                .collection("chats").document(currentChatId!!)
                .set(hashMapOf("timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()))
        }
            ?.addOnSuccessListener {
                Log.d(TAG, "New chat created with ID: $currentChatId")
                fetchMessages()
            }
            ?.addOnFailureListener{e ->
                Log.e(TAG, "Error creating chat", e)
            }
    }
    private fun fetchMessages() {
        currentChatId?.let { chatId ->
            viewModelScope.launch {
                userUid?.let { firestore.collection("users").document(it)
                    .collection("chats").document(chatId)
                    .collection("messages").orderBy("timestamp").get()
                }
                    ?.addOnSuccessListener { querySnapshot ->
                        val fetchedMessages = querySnapshot.documents.map { document ->
                            document.toObject(Message::class.java) ?: Message("", "")
                        }
                        _messages.value = fetchedMessages
                    }
                    ?.addOnFailureListener { e ->
                        Log.e(TAG, "Error fetching messages from Firestore", e)
                    }
            }
        }
    }
}