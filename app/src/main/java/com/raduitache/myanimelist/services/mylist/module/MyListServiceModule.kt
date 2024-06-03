package com.raduitache.myanimelist.services.mylist.module

import com.raduitache.myanimelist.services.mylist.MyListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MyListServiceModule {
    @Provides
    fun provideMyListService(retrofitFlow: Flow<Retrofit>): Flow<MyListService> = retrofitFlow.map {
        it.create(MyListService::class.java)
    }
}