package com.raduitache.myanimelist.auth.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduitache.myanimelist.services.auth.AuthService
import com.raduitache.myanimelist.services.auth.db.UserDao
import com.raduitache.myanimelist.services.auth.models.User
import com.raduitache.myanimelist.services.db.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authService: AuthService, private val appDatabase: AppDatabase) : ViewModel() {
    private val _authViewState = MutableStateFlow(AuthViewState())
    val authViewState = _authViewState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                startLoading()
                if (authService.login(email, password)) {
                    return@launch ggGoNext()
                }
                throw Exception("Login failed")
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            try {
                startLoading()
                if (authService.signup(email = email, password = password, username = username)) {
                    return@launch ggGoNext()
                }
                throw Exception("Signup failed")

            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun ggGoNext() {
        _authViewState.update {
            it.copy(shouldNavigateNext = true, isLoading = false)
        }
    }

    private fun startLoading() {
        _authViewState.update {
            it.copy(isLoading = true, hadError = null)
        }
    }

    private fun handleException(e: Exception) {
        _authViewState.update {
            it.copy(hadError = e, isLoading = false)
        }
    }

    fun toggleSigningUp() {
        _authViewState.update {
            it.copy(isSigningUp = it.isSigningUp.not())
        }
    }

    fun toggleUserList() {
        viewModelScope.launch {
            appDatabase.userDao().getAll().also { list ->
                _authViewState.update {
                    it.copy(users = list)
                }
            }
        }
    }

}


data class AuthViewState(
    val isSigningUp: Boolean = false,
    val isLoading: Boolean = false,
    val hadError: Exception? = null,
    val shouldNavigateNext: Boolean = false,
    val users: List<User>? = null
)