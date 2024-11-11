package com.example.chatbot.domain.repository

interface AuthRepositoryWithGithub {
    fun signInWithGithub(accessToken: String, onResult: (Boolean) -> Unit)
}
