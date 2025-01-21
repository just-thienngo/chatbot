package com.example.chatbot.domain.usecase.chat

import com.example.chatbot.data.model.Chat
import com.example.chatbot.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAllChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        return chatRepository.fetchAllChats()
    }
}