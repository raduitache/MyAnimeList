package com.raduitache.myanimelist.anime_details.impl.module

import com.raduitache.myanimelist.anime_details.AnimeGraphRoute
import com.raduitache.myanimelist.anime_details.AnimeNavGraph
import com.raduitache.myanimelist.anime_details.AnimeNavRoute
import com.raduitache.myanimelist.anime_details.impl.AnimeGraphRouteImpl
import com.raduitache.myanimelist.anime_details.impl.AnimeNavGraphImpl
import com.raduitache.myanimelist.anime_details.impl.AnimeNavRouteImpl
import com.raduitache.myanimelist.main.NavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface AnimeDetailsModule {
    @Binds
    @IntoSet
    fun bindAnimeDetailsToNavGraphsSet(impl: AnimeNavGraphImpl): NavGraph

    @Binds
    fun bindAnimeDetailsNavGraph(impl: AnimeNavGraphImpl): AnimeNavGraph

    @Binds
    fun bindAnimeDetailsNavRoute(impl: AnimeNavRouteImpl): AnimeNavRoute

    @Binds
    fun bindAnimeDetailsGraphRoute(impl: AnimeGraphRouteImpl): AnimeGraphRoute
}