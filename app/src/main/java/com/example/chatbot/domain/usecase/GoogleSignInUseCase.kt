package com.example.chatbot.domain.usecase

import com.example.chatbot.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class GoogleSignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(account: GoogleSignInAccount): Boolean {
        return authRepository.signInWithGoogle(account)
    }
}