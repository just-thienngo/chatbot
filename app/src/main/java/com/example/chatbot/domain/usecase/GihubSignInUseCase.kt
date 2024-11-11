package com.example.chatbot.domain.usecase

import com.example.chatbot.domain.repository.AuthRepositoryWithGithub
import javax.inject.Inject

class GithubSignInUseCase @Inject constructor(
    private val authRepositoryWithGithub: AuthRepositoryWithGithub
) {
    operator fun invoke(accessToken: String, onResult: (Boolean) -> Unit) {
        authRepositoryWithGithub.signInWithGithub(accessToken, onResult)
    }
}
