package com.raduitache.myanimelist.auth.impl.module

import com.raduitache.myanimelist.auth.AuthGraphRoute
import com.raduitache.myanimelist.auth.AuthNavGraph
import com.raduitache.myanimelist.auth.AuthNavRoute
import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import com.raduitache.myanimelist.auth.impl.AuthGraphRouteImpl
import com.raduitache.myanimelist.auth.impl.AuthNavGraphImpl
import com.raduitache.myanimelist.auth.impl.AuthNavRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    @IntoSet
    fun bindSeasonalNavGraphToNavGraphs(impl: AuthNavGraphImpl): NavGraph

    @Binds
    fun bindSeasonalNavGraph(impl: AuthNavGraphImpl): AuthNavGraph

    @Binds
    fun bindSeasonalGraphRoute(impl: AuthGraphRouteImpl): AuthGraphRoute

    @Binds
    fun bindSeasonalNavRoute(impl: AuthNavRouteImpl): AuthNavRoute
}