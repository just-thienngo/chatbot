package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.data.model.Message
import com.example.chatbot.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message, chatId: String) {
        return chatRepository.sendMessage(message, chatId)
    }
}