package com.raduitache.myanimelist.signin.impl.module

import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.signin.SignInGraphRoute
import com.raduitache.myanimelist.signin.SignInNavGraph
import com.raduitache.myanimelist.signin.SignInNavRoute
import com.raduitache.myanimelist.signin.impl.SignInGraphRouteImpl
import com.raduitache.myanimelist.signin.impl.SignInNavGraphImpl
import com.raduitache.myanimelist.signin.impl.SignInNavRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface SignInModule {
    @Binds
    fun bindSignInGraphRoute(impl: SignInGraphRouteImpl): SignInGraphRoute

    @Binds
    fun bindSignInNavRoute(impl: SignInNavRouteImpl): SignInNavRoute

    @Binds
    @IntoSet
    fun bindSignInNavGraph(impl: SignInNavGraphImpl): NavGraph
}