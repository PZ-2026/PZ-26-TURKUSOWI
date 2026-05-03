package com.turkusowi.animalsheltermenager.features.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.SessionManager
import com.turkusowi.animalsheltermenager.core.data.WalkReservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val isLoading: Boolean = true,
    val reservations: List<WalkReservation> = emptyList(),
    val errorMessage: String? = null
)

class ScheduleViewModel(
    private val repository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            val userId = sessionManager.currentUser.value?.id
            if (userId == null) {
                _uiState.value = ScheduleUiState(isLoading = false, errorMessage = "Najpierw zaloguj sie, aby zobaczyc terminy.")
                return@launch
            }

            runCatching {
                repository.getReservations(volunteerId = userId)
            }.onSuccess { reservations ->
                _uiState.value = ScheduleUiState(isLoading = false, reservations = reservations)
            }.onFailure { error ->
                _uiState.value = ScheduleUiState(isLoading = false, errorMessage = error.message ?: "Nie udalo sie pobrac rezerwacji.")
            }
        }
    }

    fun cancelReservation(reservationId: Int, onFinished: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching {
                repository.cancelReservation(reservationId)
            }.onSuccess {
                refresh()
                onFinished()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(errorMessage = error.message ?: "Nie udalo sie anulowac rezerwacji.")
            }
        }
    }
}
