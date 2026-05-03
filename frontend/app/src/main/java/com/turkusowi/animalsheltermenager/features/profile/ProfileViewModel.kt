package com.turkusowi.animalsheltermenager.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.SessionManager
import com.turkusowi.animalsheltermenager.core.data.WalkReservation
import com.turkusowi.animalsheltermenager.features.animals.Animal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val fullName: String = "",
    val roleLabel: String = "",
    val initials: String = "",
    val walks: List<WalkReservation> = emptyList(),
    val favoriteAnimals: List<Animal> = emptyList(),
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val repository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            val currentUser = sessionManager.currentUser.value
            if (currentUser == null) {
                _uiState.value = ProfileUiState(isLoading = false, errorMessage = "Brak aktywnej sesji.")
                return@launch
            }

            runCatching {
                val walks = repository.getReservations(volunteerId = currentUser.id).take(3)
                val animals = repository.getAnimals().take(4)
                ProfileUiState(
                    isLoading = false,
                    fullName = currentUser.fullName,
                    roleLabel = if (currentUser.isGuest) "Gosc" else currentUser.role,
                    initials = "${currentUser.firstName.firstOrNull() ?: 'G'}${currentUser.lastName.firstOrNull() ?: ' '}".trim(),
                    walks = walks,
                    favoriteAnimals = animals
                )
            }.onSuccess { _uiState.value = it }
                .onFailure { error ->
                    _uiState.value = ProfileUiState(isLoading = false, errorMessage = error.message ?: "Nie udalo sie pobrac profilu.")
                }
        }
    }
}
