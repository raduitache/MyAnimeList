package com.raduitache.myanimelist.seasonal.impl.module

import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalGraphRoute
import com.raduitache.myanimelist.seasonal.SeasonalNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalNavRoute
import com.raduitache.myanimelist.seasonal.impl.SeasonalGraphRouteImpl
import com.raduitache.myanimelist.seasonal.impl.SeasonalNavGraphImpl
import com.raduitache.myanimelist.seasonal.impl.SeasonalNavRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface SeasonalModule {
    @Binds
    @IntoSet
    fun bindSeasonalNavGraphToNavGraphs(impl: SeasonalNavGraphImpl): NavGraph

    @Binds
    @IntoSet
    fun bindSeasonalNavGraphToMainNavGraphs(impl: SeasonalNavGraphImpl): MainNavGraph

    @Binds
    fun bindSeasonalNavGraph(impl: SeasonalNavGraphImpl): SeasonalNavGraph

    @Binds
    fun bindSeasonalGraphRoute(impl: SeasonalGraphRouteImpl): SeasonalGraphRoute

    @Binds
    fun bindSeasonalNavRoute(impl: SeasonalNavRouteImpl): SeasonalNavRoute
}