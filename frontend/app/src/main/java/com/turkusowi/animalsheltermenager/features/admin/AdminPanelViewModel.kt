package com.turkusowi.animalsheltermenager.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminPanelUiState(
    val adminName: String = "",
    val adminInitials: String = "",
    val adminRole: String = "",
    val managedAccounts: Int = 0,
    val newReports: Int = 0
)

class AdminPanelViewModel(
    private val repository: AdminRepository = InMemoryAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPanelUiState())
    val uiState: StateFlow<AdminPanelUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.employeesFlow.collect {
                val admin = repository.getLoggedAdmin()
                val summary = repository.getDashboardSummary()

                _uiState.value = AdminPanelUiState(
                    adminName = admin.fullName,
                    adminInitials = admin.initials,
                    adminRole = admin.role.label,
                    managedAccounts = summary.managedAccounts,
                    newReports = summary.newReports
                )
            }
        }
    }
}