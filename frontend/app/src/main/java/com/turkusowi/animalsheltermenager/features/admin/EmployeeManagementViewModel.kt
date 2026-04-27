package com.turkusowi.animalsheltermenager.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmployeeManagementUiState(
    val searchQuery: String = "",
    val onlyActive: Boolean = false,
    val employees: List<Employee> = emptyList(),
    val isLoading: Boolean = true
)

class EmployeeManagementViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeManagementUiState())
    val uiState: StateFlow<EmployeeManagementUiState> = _uiState.asStateFlow()

    private var allEmployees: List<Employee> = emptyList()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val onlyActive = _uiState.value.onlyActive
            _uiState.value = _uiState.value.copy(isLoading = true)
            allEmployees = repository.getEmployees(onlyActive = onlyActive)
            applyFilters()
        }
    }

    fun onSearchQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
        applyFilters()
    }

    fun onOnlyActiveChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(onlyActive = value)
        refresh()
    }

    private fun applyFilters() {
        val searchQuery = _uiState.value.searchQuery
        val filteredEmployees = allEmployees.filter { employee ->
            searchQuery.isBlank() ||
                    employee.fullName.contains(searchQuery, ignoreCase = true) ||
                    employee.email.contains(searchQuery, ignoreCase = true)
        }

        _uiState.value = _uiState.value.copy(
            employees = filteredEmployees,
            isLoading = false
        )
    }
}
