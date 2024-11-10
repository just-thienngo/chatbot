package com.example.chatbot.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepository {
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean
}