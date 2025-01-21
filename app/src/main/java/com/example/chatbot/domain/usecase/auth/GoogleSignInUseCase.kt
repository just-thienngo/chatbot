package com.example.chatbot.domain.usecase.auth

import com.example.chatbot.domain.repository.AuthRepositoryWithGoogle
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class GoogleSignInUseCase(private val authRepositoryWithGoogle: AuthRepositoryWithGoogle) {
    suspend operator fun invoke(account: GoogleSignInAccount): Boolean {
        return authRepositoryWithGoogle.signInWithGoogle(account)
    }
}