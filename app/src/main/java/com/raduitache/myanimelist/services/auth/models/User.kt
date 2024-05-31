package com.raduitache.myanimelist.services.auth.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.processing.Generated

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val email: String,
    val password: String,
    val username: String,
)
