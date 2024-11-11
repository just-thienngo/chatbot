package com.example.chatbot.data.repository

import com.example.chatbot.domain.repository.AuthRepositoryWithGithub
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import javax.inject.Inject

class GithubAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepositoryWithGithub {
    override fun signInWithGithub(accessToken: String, onResult: (Boolean) -> Unit) {
        val credential = GithubAuthProvider.getCredential(accessToken)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
    }
}
