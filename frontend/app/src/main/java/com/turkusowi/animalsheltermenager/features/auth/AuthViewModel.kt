package com.turkusowi.animalsheltermenager.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.LoginPayload
import com.turkusowi.animalsheltermenager.core.data.RegisterPayload
import com.turkusowi.animalsheltermenager.core.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isPasswordResetSent: Boolean = false,
    val resetMessage: String? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val repository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = AuthUiState(isLoading = true)
                repository.login(LoginPayload(email, password))
            }.onSuccess { user ->
                sessionManager.login(user)
                _uiState.value = AuthUiState()
                onSuccess()
            }.onFailure { error ->
                _uiState.value = AuthUiState(errorMessage = error.message ?: "Nie udalo sie zalogowac.")
            }
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        sessionManager.continueAsGuest()
        onSuccess()
    }

    fun register(firstName: String, lastName: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = AuthUiState(isLoading = true)
                repository.register(RegisterPayload(firstName, lastName, email, password))
            }.onSuccess { user ->
                sessionManager.login(user)
                _uiState.value = AuthUiState()
                onSuccess()
            }.onFailure { error ->
                _uiState.value = AuthUiState(errorMessage = error.message ?: "Nie udalo sie zarejestrowac.")
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = AuthUiState(isLoading = true)
                repository.requestPasswordReset(email)
            }.onSuccess { message ->
                _uiState.value = AuthUiState(isPasswordResetSent = true, resetMessage = message)
            }.onFailure { error ->
                _uiState.value = AuthUiState(errorMessage = error.message ?: "Nie udalo sie wyslac linku.")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
