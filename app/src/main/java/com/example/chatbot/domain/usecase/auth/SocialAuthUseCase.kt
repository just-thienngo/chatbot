package com.example.chatbot.domain.usecase.auth

import com.example.chatbot.domain.repository.SocialAuthRepository
import com.example.chatbot.presentation.utils.Resource
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SocialAuthUseCase @Inject constructor(
    private val socialAuthRepository: SocialAuthRepository
) {
    suspend operator fun invoke(account: GoogleSignInAccount): Resource<FirebaseUser> {
        return socialAuthRepository.signInWithGoogle(account)
    }

    suspend operator fun invoke(token: AccessToken): Resource<FirebaseUser> {
        return socialAuthRepository.signInWithFacebook(token)
    }

    suspend operator fun invoke(accessToken: String): Resource<FirebaseUser> {
        return socialAuthRepository.signInWithGithub(accessToken)
    }
} 