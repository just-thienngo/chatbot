package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.domain.repository.ChatRepository
import javax.inject.Inject

class UpdateChatTimestampUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String) {
        return chatRepository.updateChatTimestamp(chatId)
    }
}