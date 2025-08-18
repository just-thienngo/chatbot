package com.example.remote.api

import com.example.code.common.utils.Resource
import com.example.remote.BuildConfig
import com.example.remote.ChatApiService
import com.example.remote.models.ChatRequest
import com.example.remote.models.Content
import com.example.remote.models.Part
import javax.inject.Inject


class ChatRemoteDataSource  @Inject constructor(
    private val api: ChatApiService
){
    suspend fun generateChatResponse(question: String): Resource<String> {
        return try {
            val request = ChatRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = question))
                    )
                )
            )

            val response = api.generateResponse(
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
}