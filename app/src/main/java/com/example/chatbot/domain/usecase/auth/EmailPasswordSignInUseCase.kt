package com.example.chatbot.domain.usecase.auth

import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class EmailPasswordSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<FirebaseUser> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}