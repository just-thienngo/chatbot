package com.example.chatbot.di

import com.example.chatbot.domain.repository.AuthRepository
import com.example.chatbot.domain.repository.ChatRepository
import com.example.chatbot.domain.repository.SocialAuthRepository
import com.example.chatbot.domain.usecase.auth.AuthUseCase
import com.example.chatbot.domain.usecase.auth.SocialAuthUseCase
import com.example.chatbot.domain.usecase.chat.ChatUseCase
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