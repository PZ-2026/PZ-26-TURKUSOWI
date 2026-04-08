package com.turkusowi.animalsheltermenager.features.admin

enum class EmployeeRole(val label: String) {
    ADMIN("Administrator"),
    SENIOR_SPECIALIST("Starszy Specjalista"),
    SPECIALIST("Specjalista"),
    COORDINATOR("Koordynator"),
    CAREGIVER("Opiekun")
}

enum class EmployeeStatus(val label: String) {
    ACTIVE("Aktywny"),
    ON_LEAVE("Na urlopie"),
    BLOCKED("Zablokowany")
}

enum class EmploymentType(val label: String) {
    FULL_TIME("Umowa o pracę"),
    CONTRACT("Umowa zlecenie"),
    B2B("B2B")
}

data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: EmployeeRole,
    val status: EmployeeStatus,
    val salaryPln: String,
    val employmentType: EmploymentType
) {
    val fullName: String
        get() = "$firstName $lastName"

    val initials: String
        get() = buildString {
            firstName.firstOrNull()?.let { append(it.uppercaseChar()) }
            lastName.firstOrNull()?.let { append(it.uppercaseChar()) }
        }
}

data class AdminDashboardSummary(
    val managedAccounts: Int,
    val newReports: Int
)