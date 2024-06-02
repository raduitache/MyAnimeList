package com.raduitache.myanimelist.mylist.impl.module

import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.mylist.MyListGraphRoute
import com.raduitache.myanimelist.mylist.MyListNavGraph
import com.raduitache.myanimelist.mylist.MyListNavRoute
import com.raduitache.myanimelist.mylist.impl.MyListGraphRouteImpl
import com.raduitache.myanimelist.mylist.impl.MyListNavGraphImpl
import com.raduitache.myanimelist.mylist.impl.MyListNavRouteImpl
import com.raduitache.myanimelist.navigation.MainNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
interface MyListModule {
    @Binds
    fun bindMyListNavRoute(impl: MyListNavRouteImpl): MyListNavRoute

    @Binds
    fun bindMyListGraphRoute(impl: MyListGraphRouteImpl): MyListGraphRoute

    @Binds
    @IntoSet
    fun bindMyListNavGraphToNavGraphs(impl: MyListNavGraphImpl): NavGraph

    @Binds
    @IntoSet
    fun bindMyListNavGraphToMainNavGraphs(impl: MyListNavGraphImpl): MainNavGraph
}