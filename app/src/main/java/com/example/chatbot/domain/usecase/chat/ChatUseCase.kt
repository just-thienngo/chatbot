package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.data.model.Chat
import com.example.chatbot.data.model.Message
import com.example.chatbot.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    // Message operations
    suspend operator fun invoke(message: Message, chatId: String) {
        return chatRepository.sendMessage(message, chatId)
    }

    fun fetchMessages(chatId: String): Flow<List<Message>> {
        return chatRepository.fetchMessages(chatId)
    }

    // Chat operations
    suspend fun createNewChat(chatId: String) {
        chatRepository.createNewChat(chatId)
    }

    fun fetchAllChats(): Flow<List<Chat>> {
        return chatRepository.fetchAllChats()
    }

    suspend fun deleteChat(chatId: String) {
        chatRepository.deleteChat(chatId)
    }

    suspend fun deleteAllChats() {
        chatRepository.deleteAllChats()
    }

    // Update operations
    suspend fun updateLastMessage(chatId: String, message: String) {
        chatRepository.updateLastMessage(chatId, message)
    }

    suspend fun updateChatTimestamp(chatId: String) {
        chatRepository.updateChatTimestamp(chatId)
    }
} 