package com.turkusowi.animalsheltermenager.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.turkusowi.animalsheltermenager.core.data.AppRepository
import com.turkusowi.animalsheltermenager.core.data.SessionManager
import com.turkusowi.animalsheltermenager.features.admin.AdminPanelViewModel
import com.turkusowi.animalsheltermenager.features.admin.EmployeeEditViewModel
import com.turkusowi.animalsheltermenager.features.admin.EmployeeManagementViewModel
import com.turkusowi.animalsheltermenager.features.admin.InMemoryAdminRepository
import com.turkusowi.animalsheltermenager.features.admin.ui.AdminPanelPage
import com.turkusowi.animalsheltermenager.features.admin.ui.EmployeeEditPage
import com.turkusowi.animalsheltermenager.features.admin.ui.EmployeeManagementPage
import com.turkusowi.animalsheltermenager.features.animals.Animal
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalListPage
import com.turkusowi.animalsheltermenager.features.animals.ui.AnimalPanelPage
import com.turkusowi.animalsheltermenager.features.auth.AuthViewModel
import com.turkusowi.animalsheltermenager.features.auth.ui.ForgotPasswordPage
import com.turkusowi.animalsheltermenager.features.auth.ui.RegisterPage
import com.turkusowi.animalsheltermenager.features.auth.ui.WelcomeLoginPage
import com.turkusowi.animalsheltermenager.features.home.HomeViewModel
import com.turkusowi.animalsheltermenager.features.home.ui.HomePage
import com.turkusowi.animalsheltermenager.features.profile.ProfileViewModel
import com.turkusowi.animalsheltermenager.features.profile.ui.ProfilePage
import com.turkusowi.animalsheltermenager.features.schedule.ScheduleViewModel
import com.turkusowi.animalsheltermenager.features.schedule.ui.SchedulePage
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val repository = remember { AppRepository() }
    val sessionManager = remember { SessionManager() }
    val authViewModel = remember { AuthViewModel(repository, sessionManager) }
    val homeViewModel = remember { HomeViewModel(repository, sessionManager) }
    val scheduleViewModel = remember { ScheduleViewModel(repository, sessionManager) }
    val profileViewModel = remember { ProfileViewModel(repository, sessionManager) }
    val adminRepository = remember { InMemoryAdminRepository(repository, sessionManager) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val authState by authViewModel.uiState.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()
    val scheduleState by scheduleViewModel.uiState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    val currentUser by sessionManager.currentUser.collectAsState()

    NavHost(navController = navController, startDestination = Routes.AUTH_GRAPH, modifier = modifier) {
        navigation(startDestination = Routes.LOGIN, route = Routes.AUTH_GRAPH) {
            composable(Routes.LOGIN) {
                WelcomeLoginPage(
                    state = authState,
                    onLoginClick = { email, password ->
                        authViewModel.login(email, password) {
                            homeViewModel.refresh()
                            scheduleViewModel.refresh()
                            profileViewModel.refresh()
                            navController.navigate(Routes.MAIN_GRAPH) {
                                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                            }
                        }
                    },
                    onEnterAsGuest = {
                        authViewModel.continueAsGuest {
                            homeViewModel.refresh()
                            scheduleViewModel.refresh()
                            profileViewModel.refresh()
                            navController.navigate(Routes.MAIN_GRAPH) {
                                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                    onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterPage(
                    state = authState,
                    onRegisterClick = { firstName, lastName, email, password ->
                        authViewModel.register(firstName, lastName, email, password) {
                            homeViewModel.refresh()
                            scheduleViewModel.refresh()
                            profileViewModel.refresh()
                            navController.navigate(Routes.MAIN_GRAPH) {
                                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                            }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordPage(
                    state = authState,
                    onSendEmailClick = authViewModel::forgotPassword,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        navigation(startDestination = Routes.HOME, route = Routes.MAIN_GRAPH) {
            composable(Routes.HOME) {
                LaunchedEffect(currentUser?.id) { homeViewModel.refresh() }
                HomePage(
                    state = homeState,
                    onProfileClick = {
                        navController.navigate(Routes.PROFILE) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        }
                    },
                    onAnimalClick = { animal -> navController.navigate(Routes.createAnimalDetailsRoute(animal.id.toString())) },
                    onReserveClick = { navController.navigate(Routes.SCHEDULE) }
                )
            }

            composable(Routes.ANIMALS) {
                val animalsState = produceState<List<Animal>?>(initialValue = null, key1 = currentUser?.id) {
                    value = runCatching { repository.getAnimals() }.getOrDefault(emptyList())
                }

                if (animalsState.value == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AnimalListPage(
                        animals = animalsState.value.orEmpty(),
                        onAnimalClick = { animal -> navController.navigate(Routes.createAnimalDetailsRoute(animal.id.toString())) }
                    )
                }
            }

            composable(Routes.SCHEDULE) {
                LaunchedEffect(currentUser?.id) { scheduleViewModel.refresh() }
                SchedulePage(
                    state = scheduleState,
                    onCancelReservation = { reservationId ->
                        scheduleViewModel.cancelReservation(reservationId) {
                            profileViewModel.refresh()
                            homeViewModel.refresh()
                            Toast.makeText(context, "Rezerwacja anulowana.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            composable(Routes.PROFILE) {
                if (currentUser?.role.equals("ADMIN", ignoreCase = true)) {
                    val adminViewModel = remember { AdminPanelViewModel(adminRepository) }
                    AdminPanelPage(
                        viewModel = adminViewModel,
                        onManageAccountsClick = { navController.navigate(Routes.EMPLOYEE_MANAGEMENT) },
                        onLogoutClick = {
                            sessionManager.logout()
                            navController.navigate(Routes.AUTH_GRAPH) {
                                popUpTo(Routes.MAIN_GRAPH) { inclusive = true }
                            }
                        }
                    )
                } else {
                    LaunchedEffect(currentUser?.id) { profileViewModel.refresh() }
                    ProfilePage(
                        state = profileState,
                        onCancelWalk = { reservationId ->
                            scheduleViewModel.cancelReservation(reservationId) {
                                profileViewModel.refresh()
                            }
                        }
                    )
                }
            }

            composable(Routes.EMPLOYEE_MANAGEMENT) {
                val employeeManagementViewModel = remember { EmployeeManagementViewModel(adminRepository) }
                EmployeeManagementPage(
                    viewModel = employeeManagementViewModel,
                    onEmployeeClick = { employeeId -> navController.navigate(Routes.createEmployeeEditRoute(employeeId)) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.EMPLOYEE_EDIT_ROUTE,
                arguments = listOf(navArgument(Routes.EMPLOYEE_ID_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                val employeeId = backStackEntry.arguments?.getString(Routes.EMPLOYEE_ID_ARG).orEmpty()
                val employeeEditViewModel = remember(employeeId) { EmployeeEditViewModel(employeeId = employeeId, repository = adminRepository) }
                EmployeeEditPage(viewModel = employeeEditViewModel, onBackClick = { navController.popBackStack() })
            }

            composable(Routes.ANIMAL_DETAILS, arguments = listOf(navArgument("animalId") { type = NavType.StringType })) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")?.toIntOrNull()
                val animalState = produceState<Animal?>(initialValue = null, key1 = animalId) {
                    value = animalId?.let { runCatching { repository.getAnimal(it) }.getOrNull() }
                }

                if (animalState.value == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nie znaleziono zwierzaka")
                    }
                } else {
                    AnimalPanelPage(
                        animal = animalState.value!!,
                        onBackClick = { navController.popBackStack() },
                        onScheduleClick = {
                            val userId = currentUser?.id
                            if (userId == null || currentUser?.role.equals("ADMIN", true)) {
                                Toast.makeText(context, "Spacer moze zaplanowac zalogowany wolontariusz.", Toast.LENGTH_SHORT).show()
                            } else {
                                val selectedAnimal = animalState.value!!
                                scope.launch {
                                    runCatching {
                                        repository.createWalkReservation(userId, selectedAnimal.id)
                                    }.onSuccess {
                                        scheduleViewModel.refresh()
                                        profileViewModel.refresh()
                                        homeViewModel.refresh()
                                        Toast.makeText(context, "Spacer zostal zaplanowany na jutro 10:00.", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Routes.SCHEDULE)
                                    }.onFailure { error ->
                                        Toast.makeText(context, error.message ?: "Nie udalo sie zapisac spaceru.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String, onClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onClick) {
            Text(text)
        }
    }
}
