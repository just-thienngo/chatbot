package com.example.chatbot.data.remote

import com.example.chatbot.presentation.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

interface ChatApiService {
    suspend fun generateResponse(question: String): Resource<String>
}

class ChatApiServiceImpl @Inject constructor(
    private val client: OkHttpClient
) : ChatApiService {
    
    override suspend fun generateResponse(question: String): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", question))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=")
                .post(RequestBody.create(JSON, jsonBody.toString()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Resource.Error("API call failed: ${response.code()}")
                }

                response.body()?.string()?.let { responseBody ->
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val result = jsonResponse
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        Resource.Success(result.trim())
                    } catch (e: Exception) {
                        Resource.Error("Failed to parse response: ${e.message}")
                    }
                } ?: Resource.Error("Empty response")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    companion object {
        private val JSON = MediaType.get("application/json; charset=utf-8")
    }
} 
//AIzaSyA13-9MbJYX-eYcVCD0eIGLNynTcVm5EYs
