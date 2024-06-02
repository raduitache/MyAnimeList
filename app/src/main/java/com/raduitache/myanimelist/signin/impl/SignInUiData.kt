package com.raduitache.myanimelist.signin.impl

data class SignInUiData(val code: String?, val completionState: Result<Unit>? = null)
