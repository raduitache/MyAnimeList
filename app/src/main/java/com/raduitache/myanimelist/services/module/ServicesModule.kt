package com.raduitache.myanimelist.services.module

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import com.google.gson.Gson
import com.raduitache.myanimelist.responses.AuthData
import com.raduitache.myanimelist.responses.AuthResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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
                                if (response.code != 401) return@addInterceptor response

                                // Call the refresh token API to obtain a new access token
                                val newAuthData = runBlocking(Dispatchers.IO) {
                                    val client = OkHttpClient()
                                    val requestBody = RequestBody.create(
                                        "application/x-www-form-urlencoded".toMediaTypeOrNull(),
                                        "client_id=98ed5bd620cb5e1182e416e994b5b62f" +
                                                "&client_secret=82c79106d56636edb358e5045db6f2985e243d3509c7f41e507dda9180024b07" +
                                                "&grant_type=refresh_token" +
                                                "&redirect_uri=" + Uri.encode("app://myanimelist-69e80.firebaseapp.com") +
                                                "&refresh_token=${authResponse?.refreshToken}"
                                    )
                                    val request = Request.Builder()
                                        .url("https://myanimelist.net/v1/oauth2/token")
                                        .post(requestBody)
                                        .build()
                                    val authData = client.newCall(request).execute().body?.string()?.let {
                                        Gson().fromJson(it, AuthData::class.java)
                                    } ?: AuthData()

                                    database?.child("authData")?.setValue(authData)

                                    return@runBlocking authData
                                }
                                // Create a new request with the updated access token
                                val newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "${newAuthData.tokenType} ${newAuthData.accessToken}")
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