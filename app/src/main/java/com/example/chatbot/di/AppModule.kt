package com.example.chatbot.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.chatbot.data.remote.ChatApiService
import com.example.chatbot.data.remote.ChatApiServiceImpl
import com.example.chatbot.data.repository.AuthRepositoryImpl
import com.example.chatbot.data.repository.ChatRepositoryImpl
import com.example.chatbot.data.repository.FacebookAuthRepositoryImpl
import com.example.chatbot.data.repository.GithubAuthRepositoryImpl
import com.example.chatbot.data.repository.GoogleAuthRepositoryImpl
import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.domain.repository.AuthRepositoryWithFacebook
import com.example.chatbot.domain.repository.AuthRepositoryWithGithub
import com.example.chatbot.domain.repository.AuthRepositoryWithGoogle
import com.example.chatbot.domain.repository.ChatRepository
import com.example.chatbot.domain.usecase.DeleteAllChatsUseCase
import com.example.chatbot.domain.usecase.FacebookSignInUseCase
import com.example.chatbot.domain.usecase.GithubSignInUseCase
import com.example.chatbot.domain.usecase.GoogleSignInUseCase
import com.example.chatbot.presentation.utils.Constants.INTRODUCTION_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideGoogleAuthRepository(): AuthRepositoryWithGoogle = GoogleAuthRepositoryImpl()

    @Provides
    fun provideFacebookAuthRepository(): AuthRepositoryWithFacebook = FacebookAuthRepositoryImpl()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideGithubAuthRepository(firebaseAuth: FirebaseAuth): AuthRepositoryWithGithub =
        GithubAuthRepositoryImpl(firebaseAuth)
    @Provides
    fun provideGoogleSignInUseCase(authRepositoryWithGoogle: AuthRepositoryWithGoogle): GoogleSignInUseCase {
        return GoogleSignInUseCase(authRepositoryWithGoogle)
    }

    @Provides
    fun provideFacebookSignInUseCase(authRepositoryWithFacebook: AuthRepositoryWithFacebook): FacebookSignInUseCase {
        return FacebookSignInUseCase(authRepositoryWithFacebook)
    }
    @Provides
    fun provideGithubSignInUseCase(authRepositoryWithGithub: AuthRepositoryWithGithub): GithubSignInUseCase {
        return GithubSignInUseCase(authRepositoryWithGithub)
    }

    @Provides
    fun provideIntroductionSP(
        application: Application
    )= application.getSharedPreferences(INTRODUCTION_SP, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    // Add this method to provide ChatApiService
    @Provides
    @Singleton
    fun provideChatApiService(client: OkHttpClient): ChatApiService {
        return ChatApiServiceImpl(client)
    }
    @Provides
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): ChatRepository = ChatRepositoryImpl(firestore, firebaseAuth)

    @Provides
    fun provideAuthRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firestore, firebaseAuth)

    // Provide OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
    @Provides
    @Singleton
    fun provideDeleteAllChatsUseCase(
        chatRepository: ChatRepository
    ): DeleteAllChatsUseCase {
        return DeleteAllChatsUseCase(chatRepository)
    }
}