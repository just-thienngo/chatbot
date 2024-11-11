package com.example.chatbot.domain.repository

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepositoryWithFacebook {
    suspend fun signInWithFacebook(token: AccessToken): Boolean
}