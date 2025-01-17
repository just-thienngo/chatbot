package com.example.chatbot.domain.usecase

import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.presentation.utils.Resource
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): Resource<String> {
        return authRepository.resetPassword(email)
    }
}
