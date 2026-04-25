package com.turkusowi.animalsheltermenager.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkusowi.animalsheltermenager.features.auth.data.AuthRepository
import com.turkusowi.animalsheltermenager.features.auth.data.LoginRequest
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        authState = AuthState.Success(body.token)
                    } else {
                        authState = AuthState.Error("Błąd serwera: pusty body")
                    }
                } else {
                    val code = response.code()
                    val message = when (code) {
                        401 -> "Błędne dane logowania (401)"
                        403 -> "Brak dostępu (403)"
                        404 -> "Nie znaleziono endpointu (404)"
                        else -> "Błąd serwera: $code"
                    }
                    authState = AuthState.Error(message)
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Błąd połączenia: ${e.message}")
            }
        }
    }

    fun resetState() {
        authState = AuthState.Idle
    }
}
