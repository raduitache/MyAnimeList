package com.raduitache.myanimelist.services.anime.module

import com.raduitache.myanimelist.services.anime.AnimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AnimeServiceModule {
    @Provides
    fun provideAnimeService(retrofit: Retrofit): AnimeService = retrofit
            .create(AnimeService::class.java)
}