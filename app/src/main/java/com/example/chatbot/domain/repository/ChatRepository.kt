
package com.example.chatbot.domain.repository

import com.example.chatbot.data.model.Chat
import com.example.chatbot.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: Message, chatId: String)
    suspend fun updateLastMessage(chatId: String, message: String)
    suspend fun updateChatTimestamp(chatId: String)
    suspend fun createNewChat(chatId: String)
    fun fetchMessages(chatId: String): Flow<List<Message>>
    suspend fun deleteChat(chatId: String)
    fun fetchAllChats(): Flow<List<Chat>>
    suspend fun deleteAllChats()
}
