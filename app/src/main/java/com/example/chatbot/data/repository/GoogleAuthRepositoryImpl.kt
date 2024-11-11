
package com.example.chatbot.data.repository

import com.example.chatbot.domain.repository.AuthRepositoryWithGoogle
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthRepositoryImpl : AuthRepositoryWithGoogle {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        return try {
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
