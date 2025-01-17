package com.example.chatbot.data.repository

import android.util.Log
import com.example.chatbot.data.model.User
import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): AuthRepository{
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    override suspend fun createNewUser(uid: String, fullName: String, email: String) {
        val user = hashMapOf("fullName" to fullName, "email" to email)
        firestore.collection("users").document(uid).set(user).await()
            .let{
                Log.d(TAG, "New user created with ID: $uid")
            }
    }
    override suspend fun createAccountWithEmailAndPassword(user: User, password: String): Resource<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            authResult.user?.let {
                createNewUser(it.uid, user.fullName, user.email)
            }
            Resource.Success(authResult.user)
        }
        catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(authResult.user)
        }catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }
    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}