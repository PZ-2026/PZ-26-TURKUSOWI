package com.turkusowi.animalsheltermenager.features.admin

interface AdminRepository {
    suspend fun getLoggedAdmin(): Employee
    suspend fun getDashboardSummary(): AdminDashboardSummary
    suspend fun getEmployees(onlyActive: Boolean = false): List<Employee>
    suspend fun getEmployeeById(employeeId: String): Employee?
    suspend fun updateEmployee(updatedEmployee: Employee): Result<Unit>
}
