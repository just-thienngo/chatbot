package com.example.chatbot.data.repository

import com.example.chatbot.domain.repository.AuthRepositoryWithFacebook
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FacebookAuthRepositoryImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) :
    AuthRepositoryWithFacebook {

    override suspend fun signInWithFacebook(token: AccessToken): Boolean {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        return try {
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

