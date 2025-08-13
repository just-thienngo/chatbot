package com.example.repository_impl

import android.util.Log
import com.example.code.common.utils.Resource
import com.example.commom_entity.User
import com.example.repository.AuthRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
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

    override suspend fun createAccountWithEmailAndPassword(
        user: User,
        password: String
    ): Resource<FirebaseUser> {
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
    override suspend fun resetPassword(email: String): Resource<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success("Password reset email sent")
        } catch (e: Exception) {
            if (e is FirebaseAuthInvalidUserException){
                return  Resource.Error("User not found or email is incorrect")
            }
            Resource.Error(e.message.toString())

        }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}