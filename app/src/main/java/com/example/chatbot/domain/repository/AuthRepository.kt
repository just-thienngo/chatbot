package com.example.chatbot.domain.repository
import com.example.chatbot.data.model.User
import kotlinx.coroutines.flow.Flow
import com.example.chatbot.presentation.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun getCurrentUser():  com.google.firebase.auth.FirebaseUser?
    suspend fun createNewUser(uid: String, fullName: String, email: String)
    suspend fun createAccountWithEmailAndPassword(user: User, password: String): Resource<FirebaseUser>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser>
}
