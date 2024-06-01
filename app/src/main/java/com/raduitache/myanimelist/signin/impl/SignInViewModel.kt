package com.raduitache.myanimelist.signin.impl

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.raduitache.myanimelist.common.CodeChallenge
import com.raduitache.myanimelist.responses.AuthData
import com.raduitache.myanimelist.responses.AuthResponse
import com.raduitache.myanimelist.responses.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    internal val savedStateHandle: SavedStateHandle,
    private val databaseFlow: Flow<@JvmSuppressWildcards DatabaseReference?>,
) : ViewModel() {
    private val codeFlow: Flow<String?> = savedStateHandle.getStateFlow("code", null)
    private val completionState: MutableStateFlow<Result<Unit>?> = MutableStateFlow(null)
    private val codeChallenge: String? = savedStateHandle.get<String>("state")

    val uiData: StateFlow<SignInUiData> = combine(codeFlow, completionState, ::SignInUiData)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SignInUiData(code = null))

    init {
        savedStateHandle.get<String>("state")?.let {
            Log.w("SignInViewModel", "Returned state: $it")
        }
    }

    fun signIn(activity: Activity) {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val codeChallenge = CodeChallenge()
        Log.d("SignInViewModel", "initial challenge: $codeChallenge")
        customTabsIntent.launchUrl(
            activity,
            Uri.parse(
                "https://myanimelist.net/v1/oauth2/authorize" +
                        "?response_type=code" +
                        "&client_id=98ed5bd620cb5e1182e416e994b5b62f" +
                        "&state=$codeChallenge" +
                        "&redirect_uri=" + Uri.encode("app://myanimelist-69e80.firebaseapp.com") +
                        "&code_challenge=$codeChallenge" +
                        "&code_challenge_method=plain"
            ),
        )
    }

    fun exchangeCodesAndLogIn() {
        val code = uiData.value.code
        if (code == null) {
            completionState.value = Result.failure(IllegalStateException("No code provided"))
            return
        }

        val httpClient = OkHttpClient()
        val requestBody = RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "client_id=98ed5bd620cb5e1182e416e994b5b62f" +
                    "&client_secret=82c79106d56636edb358e5045db6f2985e243d3509c7f41e507dda9180024b07" +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + Uri.encode("app://myanimelist-69e80.firebaseapp.com") +
                    "&code=$code" +
                    "&code_verifier=$codeChallenge"
        )
        val request = Request.Builder()
            .url("https://myanimelist.net/v1/oauth2/token")
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completionState.value = Result.failure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val authResponse = response.body()?.string()?.let {
                    Gson().fromJson(it, AuthResponse::class.java)
                }
                if (authResponse == null) {
                    completionState.value =
                        Result.failure(IOException("Failed to parse auth response"))
                    return
                }
                viewModelScope.launch {
                    getUserIdAndSignInToken(authResponse.toAuthData())
                }
            }
        })
    }

    private fun getUserIdAndSignInToken(authData: AuthData) {
        val httpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.myanimelist.net/v2/users/@me")
            .addHeader("Authorization", "Bearer ${authData.accessToken}")
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completionState.value = Result.failure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val user = response.body()?.string()?.let {
                    Gson().fromJson(it, User::class.java)
                }
                if (user == null) {
                    completionState.value =
                        Result.failure(IOException("Failed to parse user response"))
                    return
                }

                val userTokenRequest = Request.Builder()
                    .url("https://auth-server-zcbtcmryzq-ey.a.run.app/${user.id}")
                    .build()

                httpClient.newCall(userTokenRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        completionState.value = Result.failure(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val token = response.body()?.string()
                        if (token == null) {
                            completionState.value =
                                Result.failure(IOException("Failed to parse user token"))
                            return
                        }
                        Firebase.auth.signInWithCustomToken(token).addOnSuccessListener {
                            viewModelScope.launch {
                                databaseFlow.firstOrNull()
                                    ?.child("authData")?.setValue(authData)
                                    ?.addOnFailureListener {
                                        completionState.value = Result.failure(it)
                                    }
                                    ?.addOnSuccessListener {
                                        completionState.value = Result.success(Unit)
                                    }
                            }
                        }
                            .addOnFailureListener {
                                completionState.value = Result.failure(it)
                            }
                    }
                })
            }
        })
    }
}