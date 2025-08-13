package com.example.usecase.chat

import com.example.commom_entity.Chat
import com.example.commom_entity.Message
import com.example.repository.ChatRepository
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChatUseCaseTest {

    private lateinit var chatRepository: ChatRepository
    private lateinit var chatUseCase: ChatUseCase

    @Before
    fun setUp() {
        chatRepository = mockk()
        chatUseCase = ChatUseCase(chatRepository)
    }

    @After
    fun tearDown() {
        clearMocks(chatRepository)
    }

    @Test
    fun `invoke should call sendMessage with correct parameters`() = runBlocking {
        val message: Message = mockk()
        val chatId = "chat123"
        coEvery { chatRepository.sendMessage(message, chatId) } just Runs

        chatUseCase(message, chatId)

        coVerify { chatRepository.sendMessage(message, chatId) }
    }

    @Test
    fun `fetchMessages should return flow of messages`() = runBlocking { // Sẽ sửa runBlocking sau
        val chatId = "chat123"
        // Tạo các đối tượng Message thực tế để so sánh, không dùng mockk() cho các phần tử list
        val expectedMessages = listOf(Message("id1", "Content 1"), Message("id2", "Content 2"))

        // Mock repository để trả về một Flow phát ra *một* lần danh sách này
        every { chatRepository.fetchMessages(chatId) } returns flowOf(expectedMessages)

        // Act: Lấy *giá trị đầu tiên* được phát ra từ Flow, sẽ là List<Message>
        val result = chatUseCase.fetchMessages(chatId).first()

        // Assert
        assertEquals(expectedMessages, result) // So sánh List<Message> với List<Message>
        verify { chatRepository.fetchMessages(chatId) }
    }

    @Test
    fun `createNewChat should call repository with correct chatId`() = runBlocking {
        val chatId = "chat123"
        coEvery { chatRepository.createNewChat(chatId) } just Runs

        chatUseCase.createNewChat(chatId)

        coVerify { chatRepository.createNewChat(chatId) }
    }

    @Test
    fun `fetchAllChats should return flow of chats`() = runBlocking { // Sẽ sửa runBlocking sau
        // Tạo các đối tượng Chat thực tế để so sánh, không dùng mockk() cho các phần tử list
        val expectedChats = listOf(Chat("chatId1", "My Chat"), Chat("chatId2", "Another Chat"))

        // Mock repository để trả về một Flow phát ra *một* lần danh sách này
        every { chatRepository.fetchAllChats() } returns flowOf(expectedChats)

        // Act: Lấy *giá trị đầu tiên* được phát ra từ Flow, sẽ là List<Chat>
        val result = chatUseCase.fetchAllChats().first()

        // Assert
        assertEquals(expectedChats, result) // So sánh List<Chat> với List<Chat>
        verify { chatRepository.fetchAllChats() }
    }

    @Test
    fun `deleteChat should call repository with correct chatId`() = runBlocking {
        val chatId = "chat123"
        coEvery { chatRepository.deleteChat(chatId) } just Runs

        chatUseCase.deleteChat(chatId)

        coVerify { chatRepository.deleteChat(chatId) }
    }

    @Test
    fun `deleteAllChats should call repository`() = runBlocking {
        coEvery { chatRepository.deleteAllChats() } just Runs

        chatUseCase.deleteAllChats()

        coVerify { chatRepository.deleteAllChats() }
    }

    @Test
    fun `updateLastMessage should call repository with correct parameters`() = runBlocking {
        val chatId = "chat123"
        val message = "Last message"
        coEvery { chatRepository.updateLastMessage(chatId, message) } just Runs

        chatUseCase.updateLastMessage(chatId, message)

        coVerify { chatRepository.updateLastMessage(chatId, message) }
    }

    @Test
    fun `updateChatTimestamp should call repository with correct chatId`() = runBlocking {
        val chatId = "chat123"
        coEvery { chatRepository.updateChatTimestamp(chatId) } just Runs

        chatUseCase.updateChatTimestamp(chatId)

        coVerify { chatRepository.updateChatTimestamp(chatId) }
    }
}