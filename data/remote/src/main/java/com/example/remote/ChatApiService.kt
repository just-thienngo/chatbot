package com.example.remote


import com.example.code.common.utils.Resource
import com.example.remote.models.ChatRequest
import com.example.remote.models.ChatResponse
import com.example.remote.models.Content
import com.example.remote.BuildConfig
import com.example.remote.models.Part
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
                Content(
                    parts = listOf(Part(text = question))
                )
            )
        )
        
        val response = generateResponse(
            apiKey = BuildConfig.API_KEY,
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
