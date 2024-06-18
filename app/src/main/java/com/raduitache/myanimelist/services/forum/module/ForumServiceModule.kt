package com.raduitache.myanimelist.services.forum.module

import com.raduitache.myanimelist.services.anime.AnimeService
import com.raduitache.myanimelist.services.forum.ForumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ForumServiceModule {
    @Provides
    fun provideForumService(retrofitFlow: Flow<Retrofit>): Flow<ForumService> = retrofitFlow.map {
        it.create(ForumService::class.java)
    }
}