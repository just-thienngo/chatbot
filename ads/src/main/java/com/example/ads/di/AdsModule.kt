package com.example.ads.di

import android.content.Context
import com.example.ads.AdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideAdManager(@ApplicationContext context: Context): AdManager {
        val adManager = AdManager(context)
        adManager.initialize() // Khởi tạo SDK khi AdManager được tạo
        return adManager
    }
}