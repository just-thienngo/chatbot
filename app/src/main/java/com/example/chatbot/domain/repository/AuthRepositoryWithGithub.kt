package com.example.chatbot.domain.repository

import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryWithGithub {
    suspend fun signInWithGithub(accessToken: String): Resource<FirebaseUser>
}