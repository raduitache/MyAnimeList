package com.raduitache.myanimelist.services.auth.module

import com.raduitache.myanimelist.services.anime.AnimeService
import com.raduitache.myanimelist.services.auth.AuthService
import com.raduitache.myanimelist.services.auth.db.UserDao
import com.raduitache.myanimelist.services.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthServiceModule {
    @Provides
    fun provideAuthService(appDb: AppDatabase): AuthService = AuthService(appDb.userDao())
}