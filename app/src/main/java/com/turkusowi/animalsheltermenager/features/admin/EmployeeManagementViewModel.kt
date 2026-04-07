package com.turkusowi.animalsheltermenager.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class EmployeeManagementUiState(
    val searchQuery: String = "",
    val onlyActive: Boolean = false,
    val employees: List<Employee> = emptyList()
)

class EmployeeManagementViewModel(
    private val repository: AdminRepository = InMemoryAdminRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val onlyActiveFlow = MutableStateFlow(false)

    val uiState: StateFlow<EmployeeManagementUiState> = combine(
        repository.employeesFlow,
        searchQueryFlow,
        onlyActiveFlow
    ) { employees, searchQuery, onlyActive ->

        val filteredEmployees = employees.filter { employee ->
            val matchesQuery = searchQuery.isBlank() ||
                    employee.fullName.contains(searchQuery, ignoreCase = true) ||
                    employee.email.contains(searchQuery, ignoreCase = true)

            val matchesStatus = !onlyActive || employee.status == EmployeeStatus.ACTIVE

            matchesQuery && matchesStatus
        }

        EmployeeManagementUiState(
            searchQuery = searchQuery,
            onlyActive = onlyActive,
            employees = filteredEmployees
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EmployeeManagementUiState()
    )

    fun onSearchQueryChange(value: String) {
        searchQueryFlow.value = value
    }

    fun onOnlyActiveChange(value: Boolean) {
        onlyActiveFlow.value = value
    }
}