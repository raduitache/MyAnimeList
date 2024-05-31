package com.raduitache.myanimelist.services.auth.db

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.raduitache.myanimelist.services.auth.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * from user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Insert
    suspend fun insert(vararg users: User)

}
