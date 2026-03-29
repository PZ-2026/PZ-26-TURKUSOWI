package com.turkusowi.animalsheltermenager.core.navigation

object Routes {
    // GRAFY
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"

    // AUTH
    const val LOGIN = "login"
    const val REGISTER = "register"

    // MAIN TABS
    const val HOME = "home"
    const val ANIMALS = "animals"
    const val SCHEDULE = "schedule"
    const val PROFILE = "profile"

    // ADMIN
    const val EMPLOYEE_MANAGEMENT = "employee_management"
    const val EMPLOYEE_EDIT = "employee_edit"
    const val EMPLOYEE_ID_ARG = "employeeId"
    const val EMPLOYEE_EDIT_ROUTE = "$EMPLOYEE_EDIT/{$EMPLOYEE_ID_ARG}"

    // DETAILS
    const val ANIMAL_DETAILS = "animal_details/{animalId}"

    fun createAnimalDetailsRoute(animalId: Int): String {
        return "animal_details/$animalId"
    }

    fun createEmployeeEditRoute(employeeId: String): String {
        return "$EMPLOYEE_EDIT/$employeeId"
    }
}