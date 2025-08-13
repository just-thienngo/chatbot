    package com.example.repository
    import com.example.code.common.utils.Resource
    import com.example.commom_entity.User
    import com.google.firebase.auth.FirebaseUser


    interface AuthRepository {
        fun getCurrentUser():  FirebaseUser?
        suspend fun createNewUser(uid: String, fullName: String, email: String)
        suspend fun createAccountWithEmailAndPassword(user: User, password: String): Resource<FirebaseUser>
        suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser>
        suspend fun signOut()
        suspend fun resetPassword(email: String): Resource<String>
    }
