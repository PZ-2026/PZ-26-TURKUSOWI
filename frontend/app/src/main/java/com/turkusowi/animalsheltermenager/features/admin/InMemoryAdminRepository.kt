package com.turkusowi.animalsheltermenager.features.admin

import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.SessionManager

class InMemoryAdminRepository(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : AdminRepository {

    override suspend fun getLoggedAdmin(): Employee {
        val sessionUser = sessionManager.currentUser.value
            ?: error("Brak zalogowanego administratora.")

        return appRepository.getEmployee(sessionUser.id)
    }

    override suspend fun getDashboardSummary(): AdminDashboardSummary {
        return appRepository.getAdminDashboardSummary()
    }

    override suspend fun getEmployees(onlyActive: Boolean): List<Employee> {
        return appRepository.getEmployees(activeOnly = onlyActive)
    }

    override suspend fun getEmployeeById(employeeId: String): Employee? {
        return runCatching { appRepository.getEmployee(employeeId.toInt()) }.getOrNull()
    }

    override suspend fun updateEmployee(updatedEmployee: Employee): Result<Unit> {
        return runCatching {
            appRepository.updateEmployee(updatedEmployee)
            Unit
        }
    }
}
