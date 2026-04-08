package com.turkusowi.animalsheltermenager.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.turkusowi.animalsheltermenager.features.admin.EmployeeEditViewModel
import com.turkusowi.animalsheltermenager.features.admin.EmployeeManagementViewModel
import com.turkusowi.animalsheltermenager.features.admin.ui.AdminPanelPage
import com.turkusowi.animalsheltermenager.features.admin.ui.EmployeeEditPage
import com.turkusowi.animalsheltermenager.features.admin.ui.EmployeeManagementPage
import com.turkusowi.animalsheltermenager.features.animals.Animal
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalListPage
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalPanelPage
import com.turkusowi.animalsheltermenager.features.auth.ui.WelcomeLoginPage

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    val dummyAnimals = listOf(
        Animal("Maks", "Labrador", "4 lata", "24.5 kg", "Samiec", "Dostępny"),
        Animal("Luna", "Kot Syberyjski", "2 lata", "4 kg", "Samica", "Dostępny"),
        Animal("Burek", "Mieszaniec", "6 lat", "12 kg", "Samiec", "Na spacerze")
    )

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GRAPH,
        modifier = modifier
    ) {

        // ==========================================
        // --- STREFA AUTORYZACJI ---
        // ==========================================
        navigation(
            startDestination = Routes.LOGIN,
            route = Routes.AUTH_GRAPH
        ) {
            composable(Routes.LOGIN) {
                WelcomeLoginPage(
                    onLoginSuccess = {
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onEnterAsGuest = {
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            composable(Routes.REGISTER) {
                PlaceholderScreen("Ekran Rejestracji") {
                    navController.popBackStack()
                }
            }
        }

        // ==========================================
        // --- STREFA GŁÓWNA APLIKACJI (Z BOTTOM BAR) ---
        // ==========================================
        navigation(
            startDestination = Routes.HOME,
            route = Routes.MAIN_GRAPH
        ) {
            // ZAKŁADKA 1
            composable(Routes.HOME) {
                com.turkusowi.animalsheltermenager.features.home.ui.HomePage()
            }

            // ZAKŁADKA 2
            composable(Routes.ANIMALS) {
                AnimalListPage(
                    animals = dummyAnimals,
                    onAnimalClick = { animal ->
                        navController.navigate(Routes.createAnimalDetailsRoute(animal.name))
                    }
                )
            }

            // ZAKŁADKA 3
            composable(Routes.SCHEDULE) {
                PlaceholderScreen("Terminy i Harmonogram")
            }

            // ZAKŁADKA 4
            composable(Routes.PROFILE) {
                val adminViewModel = remember {
                    com.turkusowi.animalsheltermenager.features.admin.AdminPanelViewModel()
                }

                AdminPanelPage(
                    viewModel = adminViewModel,
                    onManageAccountsClick = {
                        navController.navigate(Routes.EMPLOYEE_MANAGEMENT)
                    },
                    onLogoutClick = {
                        navController.navigate(Routes.AUTH_GRAPH) {
                            popUpTo(Routes.MAIN_GRAPH) { inclusive = true }
                        }
                    }
                )
            }

            // EKRAN ZARZĄDZANIA KONTAMI
            composable(Routes.EMPLOYEE_MANAGEMENT) {
                val employeeManagementViewModel = remember {
                    EmployeeManagementViewModel()
                }

                EmployeeManagementPage(
                    viewModel = employeeManagementViewModel,
                    onEmployeeClick = { employeeId ->
                        navController.navigate(Routes.createEmployeeEditRoute(employeeId))
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // EKRAN EDYCJI PRACOWNIKA
            composable(
                route = Routes.EMPLOYEE_EDIT_ROUTE,
                arguments = listOf(
                    navArgument(Routes.EMPLOYEE_ID_ARG) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val employeeId = backStackEntry.arguments
                    ?.getString(Routes.EMPLOYEE_ID_ARG)
                    .orEmpty()

                val employeeEditViewModel = remember(employeeId) {
                    EmployeeEditViewModel(employeeId = employeeId)
                }

                EmployeeEditPage(
                    viewModel = employeeEditViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // EKRANY SZCZEGÓŁOWE - Tu nie ma Bottombaru
            composable(Routes.ANIMAL_DETAILS) { backStackEntry ->
                val animalName = backStackEntry.arguments?.getString("animalId")
                val selectedAnimal = dummyAnimals.find {
                    it.name.equals(animalName, ignoreCase = true)
                }

                if (selectedAnimal != null) {
                    AnimalPanelPage(animal = selectedAnimal)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nie znaleziono: $animalName")
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text)
        }
    }
}