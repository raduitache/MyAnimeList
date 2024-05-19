package com.raduitache.myanimelist.services.module

import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>): Flow<Retrofit> = userFlow
        .distinctUntilChanged()
        .map { currentUser ->
            Retrofit.Builder().apply {
                baseUrl("https://api.myanimelist.net/v2/")
                addConverterFactory(GsonConverterFactory.create())
                client(
                    OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            val request = chain.request().newBuilder()
                            val token = currentUser?.getIdToken(false)?.result?.token
                            if (token == null) {
                                request
                                    .addHeader(
                                        "X-MAL-CLIENT-ID",
                                        "98ed5bd620cb5e1182e416e994b5b62f"
                                    )
                            } else {
                                request
                                    .addHeader("Authorization", "Bearer $token")
                            }
                            chain.proceed(request.build())
                        }
                    }
                    .build()
                )
            }
            .build()
        }
}