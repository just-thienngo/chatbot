package com.example.repository


import com.example.code.common.utils.Resource
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface SocialAuthRepository {
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Resource<FirebaseUser>
    suspend fun signInWithFacebook(token: AccessToken): Resource<FirebaseUser>
    suspend fun signInWithGithub(accessToken: String): Resource<FirebaseUser>
} 