package com.example.chatbot.di

import com.example.chatbot.data.repository.AuthRepositoryImpl
import com.example.chatbot.data.repository.ChatRepositoryImpl
import com.example.chatbot.data.repository.SocialAuthRepositoryImpl
import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.domain.repository.ChatRepository
import com.example.chatbot.domain.repository.SocialAuthRepository
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
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firestore, firebaseAuth)

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): ChatRepository = ChatRepositoryImpl(firestore, firebaseAuth)
} 