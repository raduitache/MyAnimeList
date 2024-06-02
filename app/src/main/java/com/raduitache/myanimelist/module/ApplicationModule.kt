package com.raduitache.myanimelist.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideCurrentUser(): Flow<FirebaseUser?> = callbackFlow {
        val stateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        Firebase.auth.addAuthStateListener(stateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(stateListener)
        }
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(currentUser: Flow<@JvmSuppressWildcards FirebaseUser?>): Flow<DatabaseReference?> =
        currentUser
            .map { user ->
                if (user == null) return@map null
                Firebase.database("https://myanimelist-69e80-default-rtdb.europe-west1.firebasedatabase.app/")
                    .reference
                    .child("users")
                    .child(user.uid)
            }
}