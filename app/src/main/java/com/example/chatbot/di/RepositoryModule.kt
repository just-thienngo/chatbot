package com.example.chatbot.di


import com.example.remote.api.ChatRemoteDataSource
import com.example.repository.AuthRepository
import com.example.repository.ChatRepository
import com.example.repository.SocialAuthRepository
import com.example.repository_impl.AuthRepositoryImpl
import com.example.repository_impl.ChatRepositoryImpl
import com.example.repository_impl.SocialAuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSocialAuthRepository(firebaseAuth: FirebaseAuth): SocialAuthRepository =
        SocialAuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideAuthRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): AuthRepository = AuthRepositoryImpl(firestore, firebaseAuth)

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        chatRemoteDataSource: ChatRemoteDataSource
    ): ChatRepository = ChatRepositoryImpl(firestore, firebaseAuth,chatRemoteDataSource)
} 