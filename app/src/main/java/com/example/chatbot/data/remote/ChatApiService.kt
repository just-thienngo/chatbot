package com.example.chatbot.data.remote

import com.example.chatbot.data.model.ChatRequest
import com.example.chatbot.data.model.ChatResponse
import com.example.chatbot.data.model.Part
import com.example.chatbot.presentation.utils.Resource
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateResponse(
        @Query("key") apiKey: String,
        @Body request: ChatRequest
    ): ChatResponse
}

// Extension function to convert to Resource
suspend fun ChatApiService.generateChatResponse(question: String): Resource<String> {
    return try {
        val request = ChatRequest(
            contents = listOf(
                com.example.chatbot.data.model.Content(
                    parts = listOf(Part(text = question))
                )
            )
        )
        
        val response = generateResponse(
            apiKey = "AIzaSyA13-9MbJYX-eYcVCD0eIGLNynTcVm5EYs",
            request = request
        )
        
        val result = response.candidates.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text?.trim()
            ?: throw Exception("Empty response from API")
            
        Resource.Success(result)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Unknown error occurred")
    }
} 
