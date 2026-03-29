package com.turkusowi.animalsheltermenager.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmployeeEditUiState(
    val employeeId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: EmployeeRole = EmployeeRole.SPECIALIST,
    val status: EmployeeStatus = EmployeeStatus.ACTIVE,
    val salaryPln: String = "",
    val employmentType: EmploymentType = EmploymentType.FULL_TIME,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val employeeNotFound: Boolean = false
)

class EmployeeEditViewModel(
    private val employeeId: String,
    private val repository: AdminRepository = InMemoryAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeEditUiState())
    val uiState: StateFlow<EmployeeEditUiState> = _uiState.asStateFlow()

    init {
        loadEmployee()
    }

    private fun loadEmployee() {
        if (employeeId.isBlank()) {
            _uiState.value = EmployeeEditUiState(
                isLoading = false,
                employeeNotFound = true,
                errorMessage = "Brakuje identyfikatora pracownika."
            )
            return
        }

        val employee = repository.getEmployeeById(employeeId)
        if (employee == null) {
            _uiState.value = EmployeeEditUiState(
                isLoading = false,
                employeeNotFound = true,
                errorMessage = "Nie znaleziono pracownika."
            )
            return
        }

        _uiState.value = employee.toUiState()
    }

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value, errorMessage = null)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value, errorMessage = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onRoleChange(value: EmployeeRole) {
        _uiState.value = _uiState.value.copy(role = value, errorMessage = null)
    }

    fun onStatusChange(value: EmployeeStatus) {
        _uiState.value = _uiState.value.copy(status = value, errorMessage = null)
    }

    fun onSalaryChange(value: String) {
        _uiState.value = _uiState.value.copy(
            salaryPln = value.filter { it.isDigit() },
            errorMessage = null
        )
    }

    fun onEmploymentTypeChange(value: EmploymentType) {
        _uiState.value = _uiState.value.copy(employmentType = value, errorMessage = null)
    }

    fun saveChanges() {
        val currentState = _uiState.value

        when {
            currentState.firstName.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Imię jest wymagane.")
                return
            }

            currentState.lastName.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Nazwisko jest wymagane.")
                return
            }

            currentState.email.isBlank() || !currentState.email.contains("@") -> {
                _uiState.value = currentState.copy(errorMessage = "Podaj poprawny adres e-mail.")
                return
            }

            currentState.salaryPln.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Podaj wynagrodzenie.")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isSaving = true, errorMessage = null)

            val updatedEmployee = Employee(
                id = currentState.employeeId,
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim(),
                email = currentState.email.trim(),
                role = currentState.role,
                status = currentState.status,
                salaryPln = currentState.salaryPln,
                employmentType = currentState.employmentType
            )

            repository.updateEmployee(updatedEmployee)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = true
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Nie udało się zapisać zmian."
                    )
                }
        }
    }

    fun consumeSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }

    private fun Employee.toUiState(): EmployeeEditUiState {
        return EmployeeEditUiState(
            employeeId = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            role = role,
            status = status,
            salaryPln = salaryPln,
            employmentType = employmentType,
            isLoading = false
        )
    }
}