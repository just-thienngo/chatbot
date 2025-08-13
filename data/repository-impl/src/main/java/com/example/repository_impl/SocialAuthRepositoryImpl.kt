package com.example.repository_impl

import com.example.code.common.utils.Resource
import com.example.repository.SocialAuthRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
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