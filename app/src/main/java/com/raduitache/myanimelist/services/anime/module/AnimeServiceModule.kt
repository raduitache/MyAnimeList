package com.raduitache.myanimelist.services.anime.module

import com.raduitache.myanimelist.services.anime.AnimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AnimeServiceModule {
    @Provides
    fun provideAnimeService(retrofitFlow: Flow<Retrofit>): Flow<AnimeService> = retrofitFlow.map {
        it.create(AnimeService::class.java)
    }
}