package com.example.chatbot.data.repository

import com.example.chatbot.domain.repository.SocialAuthRepository
import com.example.chatbot.presentation.utils.Resource
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SocialAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : SocialAuthRepository {

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Resource<FirebaseUser> {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(authResult.user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun signInWithFacebook(token: AccessToken): Resource<FirebaseUser> {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(authResult.user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

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