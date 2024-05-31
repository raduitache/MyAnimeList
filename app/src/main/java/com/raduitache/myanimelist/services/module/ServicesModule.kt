package com.raduitache.myanimelist.services.module

import android.content.Context
import androidx.room.Room
import com.raduitache.myanimelist.MainApplication
import com.raduitache.myanimelist.services.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder().baseUrl("https://api.myanimelist.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("X-MAL-CLIENT-ID", "740f79def96ef89384d831c339a48731")
                        .build()
                    chain.proceed(request)
                }
                    .build()
            )
            .build()

    @Provides
    fun provideAppContext(): Context = MainApplication.appContext

    @Provides
    fun provideDb(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration().build()
}