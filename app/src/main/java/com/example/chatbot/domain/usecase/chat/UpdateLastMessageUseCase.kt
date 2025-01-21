package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.domain.repository.ChatRepository
import javax.inject.Inject

class UpdateLastMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, message: String) {
        return chatRepository.updateLastMessage(chatId, message)
    }
}