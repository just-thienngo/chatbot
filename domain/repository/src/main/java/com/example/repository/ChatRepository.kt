
package com.example.repository

import com.example.commom_entity.Chat
import com.example.commom_entity.Message
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
