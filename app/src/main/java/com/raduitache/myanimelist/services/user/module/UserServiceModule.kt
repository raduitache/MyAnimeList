package com.raduitache.myanimelist.services.user.module

import com.raduitache.myanimelist.services.anime.AnimeService
import com.raduitache.myanimelist.services.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object UserServiceModule {
    @Provides
    fun provideUserService(retrofitFlow: Flow<Retrofit>): Flow<UserService> = retrofitFlow.map {
        it.create(UserService::class.java)
    }
}