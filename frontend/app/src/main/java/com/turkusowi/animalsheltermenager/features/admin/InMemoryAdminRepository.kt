package com.turkusowi.animalsheltermenager.features.admin

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryAdminRepository : AdminRepository {

    private const val LOGGED_ADMIN_ID = "emp_admin_1"

    private val _employeesFlow = MutableStateFlow(
        listOf(
            Employee(
                id = LOGGED_ADMIN_ID,
                firstName = "Dariusz",
                lastName = "Szymanek",
                email = "d.szymanek@schronisko.pl",
                role = EmployeeRole.ADMIN,
                status = EmployeeStatus.ACTIVE,
                salaryPln = "8500",
                employmentType = EmploymentType.FULL_TIME
            ),
            Employee(
                id = "emp_2",
                firstName = "Adri",
                lastName = "Storow",
                email = "adri.storow@schronisko.pl",
                role = EmployeeRole.SPECIALIST,
                status = EmployeeStatus.ACTIVE,
                salaryPln = "5600",
                employmentType = EmploymentType.FULL_TIME
            ),
            Employee(
                id = "emp_3",
                firstName = "Radosław",
                lastName = "Cebula",
                email = "radoslaw.cebula@schronisko.pl",
                role = EmployeeRole.COORDINATOR,
                status = EmployeeStatus.ON_LEAVE,
                salaryPln = "6200",
                employmentType = EmploymentType.FULL_TIME
            ),
            Employee(
                id = "emp_4",
                firstName = "Dariusz",
                lastName = "Bryszwa",
                email = "dariusz.bryszwa@schronisko.pl",
                role = EmployeeRole.SENIOR_SPECIALIST,
                status = EmployeeStatus.ACTIVE,
                salaryPln = "7100",
                employmentType = EmploymentType.B2B
            ),
            Employee(
                id = "emp_5",
                firstName = "Kacper",
                lastName = "Nowak",
                email = "kacper.nowak@schronisko.pl",
                role = EmployeeRole.CAREGIVER,
                status = EmployeeStatus.BLOCKED,
                salaryPln = "4800",
                employmentType = EmploymentType.CONTRACT
            ),
            Employee(
                id = "emp_6",
                firstName = "Wiktor",
                lastName = "Trzebiński",
                email = "wiktor.trzebinski@schronisko.pl",
                role = EmployeeRole.SPECIALIST,
                status = EmployeeStatus.ACTIVE,
                salaryPln = "5900",
                employmentType = EmploymentType.CONTRACT
            )
        )
    )

    override val employeesFlow: StateFlow<List<Employee>> = _employeesFlow.asStateFlow()

    override fun getLoggedAdmin(): Employee {
        return _employeesFlow.value.first { it.id == LOGGED_ADMIN_ID }
    }

    override fun getDashboardSummary(): AdminDashboardSummary {
        return AdminDashboardSummary(
            managedAccounts = 12,
            newReports = 4
        )
    }

    override fun getEmployeeById(employeeId: String): Employee? {
        return _employeesFlow.value.firstOrNull { it.id == employeeId }
    }

    override suspend fun updateEmployee(updatedEmployee: Employee): Result<Unit> {
        delay(300)

        val exists = _employeesFlow.value.any { it.id == updatedEmployee.id }
        if (!exists) {
            return Result.failure(IllegalArgumentException("Nie znaleziono pracownika."))
        }

        _employeesFlow.update { employees ->
            employees.map { employee ->
                if (employee.id == updatedEmployee.id) updatedEmployee else employee
            }
        }

        return Result.success(Unit)
    }
}