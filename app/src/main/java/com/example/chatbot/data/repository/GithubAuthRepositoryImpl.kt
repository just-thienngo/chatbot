package com.example.chatbot.data.repository

import com.example.chatbot.domain.repository.AuthRepositoryWithGithub
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GithubAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepositoryWithGithub {
    override suspend fun signInWithGithub(accessToken: String): Resource<FirebaseUser> {
        val credential = GithubAuthProvider.getCredential(accessToken)
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(authResult.user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}