package com.turkusowi.animalsheltermenager.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.SessionManager
import com.turkusowi.animalsheltermenager.features.animals.Animal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val greetingName: String = "Opiekunie",
    val animalCount: Int = 0,
    val availableCount: Int = 0,
    val todayWalks: Int = 0,
    val featuredAnimals: List<Animal> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                val currentUser = sessionManager.currentUser.value
                val animals = repository.getAnimals()
                val reservations = currentUser?.let { repository.getReservations(volunteerId = it.id) }.orEmpty()

                HomeUiState(
                    isLoading = false,
                    greetingName = currentUser?.firstName ?: "Opiekunie",
                    animalCount = animals.size,
                    availableCount = animals.count { it.status.contains("ADOPCJI", true) || it.status.contains("Dostep", true) },
                    todayWalks = reservations.size,
                    featuredAnimals = animals.take(6)
                )
            }.onSuccess {
                _uiState.value = it
            }.onFailure { error ->
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Nie udalo sie pobrac danych strony glownej."
                )
            }
        }
    }
}
