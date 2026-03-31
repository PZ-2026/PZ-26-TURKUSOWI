package com.turkusowi.animalsheltermenager.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.turkusowi.animalsheltermenager.features.auth.ui.WelcomeLoginPage
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalPanelPage
import com.turkusowi.animalsheltermenager.features.animals.Animal
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalListPage
import com.turkusowi.animalsheltermenager.features.auth.ui.RegisterPage
import com.turkusowi.animalsheltermenager.features.auth.ui.VerificationPage
import com.turkusowi.animalsheltermenager.features.auth.ui.ForgotPasswordPage

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
                        // Przejście do głównej aplikacji po zalogowaniu
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onEnterAsGuest = {
                        // Przejście jako gość - w przyszłości można tu przekazać parametr
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Routes.FORGOT_PASSWORD)
                    }
                )
            }

            composable(Routes.REGISTER) {
                RegisterPage(
                    onRegisterSuccess = {
                        navController.navigate(Routes.VERIFICATION)
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.VERIFICATION) {
                VerificationPage(
                    onVerifySuccess = {
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onResendCode = {
                        // logika ponownego wysłania
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordPage(
                    onSendEmailClick = {
                        navController.popBackStack()
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
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
                com.turkusowi.animalsheltermenager.features.profile.ui.ProfilePage()
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
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nie znaleziono: $animalName")
                    }
                }
            }
        }
    }
}

// Komponent tymczasowy na potrzeby szkieletu
@Composable
fun PlaceholderScreen(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Button(onClick = onClick) {
            Text(text)
        }
    }
}