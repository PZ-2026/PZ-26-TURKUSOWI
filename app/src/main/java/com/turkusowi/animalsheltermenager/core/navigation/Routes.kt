package com.turkusowi.animalsheltermenager.core.navigation

object Routes {
    // Grafy główne
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"

    // Ekrany autoryzacji
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val VERIFICATION = "verification"
    const val FORGOT_PASSWORD = "forgot_password"

    // Główne ekrany z BottomBar (Zakładki)
    const val HOME = "home"
    const val ANIMALS = "animals"
    const val SCHEDULE = "schedule"
    const val PROFILE = "profile"

    // Ekrany szczegółowe (bez BottomBaru)
    const val ANIMAL_DETAILS = "animal_details/{animalId}"

    // Funkcja pomocnicza do nawigacji z argumentami
    fun createAnimalDetailsRoute(animalId: Int): String {
        return "animal_details/$animalId"
    }
}