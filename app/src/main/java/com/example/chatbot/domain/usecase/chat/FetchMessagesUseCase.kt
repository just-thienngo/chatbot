package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.data.model.Message
import com.example.chatbot.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return chatRepository.fetchMessages(chatId)
    }
}