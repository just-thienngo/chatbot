package com.example.chatbot.di

import com.example.repository.AuthRepository
import com.example.repository.ChatRepository
import com.example.repository.SocialAuthRepository
import com.example.usecase.auth.AuthUseCase
import com.example.usecase.auth.SocialAuthUseCase
import com.example.usecase.chat.ChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSocialAuthUseCase(socialAuthRepository: SocialAuthRepository): SocialAuthUseCase {
        return SocialAuthUseCase(socialAuthRepository)
    }

    @Provides
    @Singleton
    fun provideChatUseCase(chatRepository: ChatRepository): ChatUseCase {
        return ChatUseCase(chatRepository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(authRepository)
    }
} 