package com.raduitache.myanimelist.services.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import com.raduitache.myanimelist.responses.AuthData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideRetrofitInstance(databaseFlow: Flow<@JvmSuppressWildcards DatabaseReference?>): Flow<Retrofit> =
        databaseFlow.flatMapLatest { database ->
            (database?.child("authData")?.snapshots?.map { snapshot ->
                snapshot.getValue(AuthData::class.java)
            } ?: flowOf(null)).map { authResponse ->
                Retrofit.Builder().apply {
                    baseUrl("https://api.myanimelist.net/v2/")
                    addConverterFactory(GsonConverterFactory.create())
                    client(
                        OkHttpClient.Builder().apply {
                            addInterceptor { chain ->
                                val request = chain.request().newBuilder()
                                if (authResponse == null) {
                                    request
                                        .addHeader(
                                            "X-MAL-CLIENT-ID",
                                            "98ed5bd620cb5e1182e416e994b5b62f"
                                        )
                                } else {
                                    request
                                        .addHeader(
                                            "Authorization",
                                            "${authResponse.tokenType} ${authResponse.accessToken}"
                                        )
                                }
                                chain.proceed(request.build())
                            }

                            addInterceptor { chain ->
                                val originalRequest = chain.request()
                                val response = chain.proceed(originalRequest)
                                // Check if the response indicates that the access token is expired
                                if (response.code() != 401) return@addInterceptor response

                                // Call the refresh token API to obtain a new access token
                                val newAccessToken = TODO("Refresh token API call")
                                // Create a new request with the updated access token
                                val newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer $newAccessToken")
                                    .build()
                                // Retry the request with the new access token
                                chain.proceed(newRequest)
                            }
                        }
                            .build()
                    )
                }
                    .build()
            }
        }
}