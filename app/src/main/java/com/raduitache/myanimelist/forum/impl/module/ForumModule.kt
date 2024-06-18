package com.raduitache.myanimelist.forum.impl.module

import com.raduitache.myanimelist.forum.ForumGraphRoute
import com.raduitache.myanimelist.forum.ForumNavGraph
import com.raduitache.myanimelist.forum.ForumNavRoute
import com.raduitache.myanimelist.forum.impl.ForumGraphRouteImpl
import com.raduitache.myanimelist.forum.impl.ForumNavGraphImpl
import com.raduitache.myanimelist.forum.impl.ForumNavRouteImpl
import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface ForumModule {
    @Binds
    @IntoSet
    fun bindForumNavGraphToNavGraphs(impl: ForumNavGraphImpl): NavGraph

    @Binds
    @IntoSet
    fun bindForumNavGraphToMainNavGraphs(impl: ForumNavGraphImpl): MainNavGraph

    @Binds
    fun bindForumNavGraph(impl: ForumNavGraphImpl): ForumNavGraph

    @Binds
    fun bindForumGraphRoute(impl: ForumGraphRouteImpl): ForumGraphRoute

    @Binds
    fun bindForumNavRoute(impl: ForumNavRouteImpl): ForumNavRoute
}