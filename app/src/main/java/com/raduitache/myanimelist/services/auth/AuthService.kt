package com.raduitache.myanimelist.services.auth

import com.raduitache.myanimelist.services.auth.db.UserDao
import com.raduitache.myanimelist.services.auth.models.User
import com.raduitache.myanimelist.services.crypto.CryptoService
import kotlinx.coroutines.delay

class AuthService(private val userDao: UserDao) {

    suspend fun login(email: String, password: String): Boolean {
        val currentUser = userDao.findByEmail(email) ?: throw Exception("User not found")
        delay(1500)
        return CryptoService.verify(currentUser.password, password)
    }

    suspend fun signup(email: String, password: String, username: String): Boolean {
        userDao.findByEmail(email)?.let {
            throw Exception("User already exists")
        }
        return try {
            userDao.insert(
                User(
                    email = email,
                    password = CryptoService.hash(password),
                    username = username
                )
            )
            true
        } catch (_: Exception) {
            false
        }
    }

}