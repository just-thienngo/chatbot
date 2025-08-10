package com.example.chatbot.domain.usecase.auth

import com.example.chatbot.data.model.User
import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    // Email/Password authentication
    suspend operator fun invoke(email: String, password: String): Resource<FirebaseUser> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }

    // Password reset
    suspend fun resetPassword(email: String): Resource<String> {
        return authRepository.resetPassword(email)
    }

    // Sign out
    suspend fun signOut() {
        authRepository.signOut()
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    // Create account with email and password
    suspend fun createAccountWithEmailAndPassword(user: User, password: String): Resource<FirebaseUser> {
        return authRepository.createAccountWithEmailAndPassword(user, password)
    }
} 