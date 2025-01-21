package com.example.chatbot.data.repository

import android.util.Log
import com.example.chatbot.data.model.Chat
import com.example.chatbot.data.model.Message
import com.example.chatbot.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ChatRepository{
    private val userUid = firebaseAuth.currentUser?.uid

    override suspend fun sendMessage(message: Message, chatId: String) {
        val chatDocument =  userUid?.let {  firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }

        chatDocument?.collection("messages")?.add(message)?.await()
            ?.let { documentReference ->
                Log.d(TAG, "Message added to Firestore with ID: ${documentReference.id} in chat $chatId")
                if (message.sentBy == Message.SENT_BY_ME) {
                    updateLastMessage(chatId, message.message)
                    updateChatTimestamp(chatId)
                }
            }
    }

    override suspend fun updateLastMessage(chatId: String, message: String) {
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }?.update("lastMessage", message)
            ?.await()
            ?.let {
                Log.d(TAG, "Last message updated for chat $chatId")
            }
    }

    override suspend fun updateChatTimestamp(chatId: String) {
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }?.update("timestamp", FieldValue.serverTimestamp())
            ?.await()
            ?.let {
                Log.d(TAG, "timestamp chat updated for chat $chatId")
            }
    }

    override suspend fun createNewChat(chatId: String) {
        userUid?.let {
            firestore.collection("users").document(it)
                .collection("chats").document(chatId)
                .set(hashMapOf("timestamp" to FieldValue.serverTimestamp()))
        }?.await()
            ?.let {
                Log.d(TAG, "New chat created with ID: $chatId")
            }
    }
    override fun fetchMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listenerRegistration =  userUid?.let {  firestore.collection("users").document(it)
            .collection("chats").document(chatId)
            .collection("messages").orderBy("timestamp")
        }
            ?.addSnapshotListener{snapshot, error ->
                if(error != null){
                    Log.e(TAG, "Error fetching messages", error)
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Message::class.java)
                } ?: emptyList()
                trySend(messages)
            }
        awaitClose {
            listenerRegistration?.remove()
        }
    }
    override suspend fun deleteChat(chatId: String) {
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats").document(chatId)
        }?.delete()
            ?.await()
            ?.let {
                Log.d(TAG, "Chat deleted successfully with ID: $chatId")
            }
    }
    override fun fetchAllChats(): Flow<List<Chat>> = callbackFlow {
        val listenerRegistration =  userUid?.let {
            firestore.collection("users").document(it)
                .collection("chats")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
        }
            ?.addSnapshotListener {querySnapshot, error ->
                if(error != null) {
                    Log.e(TAG, "Error fetching chats", error)
                    close(error)
                    return@addSnapshotListener
                }

                val chats = querySnapshot?.documents?.mapNotNull { document ->
                    document.toObject(Chat::class.java)?.copy(chatId = document.id)
                } ?: emptyList()
                trySend(chats)
            }
        awaitClose{
            listenerRegistration?.remove()
        }
    }
    override suspend fun deleteAllChats() {
        userUid?.let { firestore.collection("users").document(it)
            .collection("chats")
        }?.get()
            ?.await()
            ?.documents
            ?.forEach { document ->
                document.reference.delete()
            }
    }
    companion object {
        private const val TAG = "ChatRepositoryImpl"
    }
}