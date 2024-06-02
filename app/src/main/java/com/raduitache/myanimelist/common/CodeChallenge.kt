package com.raduitache.myanimelist.common

import android.util.Base64
import java.util.UUID

object CodeChallenge {
    operator fun invoke(): String =
        Base64.encodeToString(UUID.randomUUID().toString().toByteArray(), Base64.URL_SAFE)
}