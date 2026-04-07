package com.turkusowi.animalsheltermenager.features.admin

import kotlinx.coroutines.flow.StateFlow

interface AdminRepository {
    val employeesFlow: StateFlow<List<Employee>>

    fun getLoggedAdmin(): Employee
    fun getDashboardSummary(): AdminDashboardSummary
    fun getEmployeeById(employeeId: String): Employee?
    suspend fun updateEmployee(updatedEmployee: Employee): Result<Unit>
}