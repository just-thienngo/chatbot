package com.example.chatbot.domain.usecase

import com.example.chatbot.domain.repository.AuthRepositoryWithGithub
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GithubSignInUseCase @Inject constructor(
    private val authRepositoryWithGithub: AuthRepositoryWithGithub
) {
    suspend operator fun invoke(accessToken: String): Resource<FirebaseUser> {
        return authRepositoryWithGithub.signInWithGithub(accessToken)
    }
}