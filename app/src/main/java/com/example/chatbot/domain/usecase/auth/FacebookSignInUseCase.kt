package com.example.chatbot.domain.usecase.auth

import com.example.chatbot.domain.repository.AuthRepositoryWithFacebook
import com.facebook.AccessToken

class FacebookSignInUseCase(private val repository: AuthRepositoryWithFacebook) {
    suspend operator fun invoke(token: AccessToken): Boolean {
        return repository.signInWithFacebook(token)
    }
}