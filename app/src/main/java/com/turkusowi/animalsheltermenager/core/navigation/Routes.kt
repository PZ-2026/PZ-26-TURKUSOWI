package com.turkusowi.animalsheltermenager.core.navigation

object Routes {
    // Grafy główne
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"

    // Ekrany autoryzacji
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Główne ekrany z BottomBar (Zakładki)
    const val HOME = "home"
    const val ANIMALS = "animals"
    const val SCHEDULE = "schedule"
    const val PROFILE = "profile"

    // Ekrany szczegółowe (bez BottomBaru)
    const val ANIMAL_DETAILS = "animal_details/{animalId}"

    // Funkcja pomocnicza do nawigacji z argumentami
    //Chwilowa zmiana typu z Int na String w celu sprawdzenia czy wyświetlanie działa
    fun createAnimalDetailsRoute(animalId: String): String {
        return "animal_details/$animalId"
    }
}