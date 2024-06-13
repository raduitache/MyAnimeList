package com.raduitache.myanimelist.services.crypto

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode

object CryptoService {
    private val argon2Kt = Argon2Kt()
    private val argon2Mode = Argon2Mode.ARGON2_I
    private const val SALT = "sdkjfdsnhfjgksdhfjksdh"
    private const val tCostInIterations = 5
    private const val mCostInKibibyte = 65536

    fun hash(value: String): String {
        return argon2Kt.hash(
            mode = argon2Mode,
            password = value.toByteArray(),
            salt = SALT.toByteArray(),
            tCostInIterations = tCostInIterations,
            mCostInKibibyte = mCostInKibibyte
        ).encodedOutputAsString()
    }

    fun verify(encodedValue: String, testValue: String): Boolean {
        return argon2Kt.verify(
            mode = argon2Mode,
            encoded = encodedValue,
            password = testValue.toByteArray()
        )
    }
}