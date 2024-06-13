package com.raduitache.myanimelist.services.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raduitache.myanimelist.services.auth.db.UserDao
import com.raduitache.myanimelist.services.auth.models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
