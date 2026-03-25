package com.turkusowi.animalsheltermenager.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    // Obserwujemy aktualny stan stosu nawigacji
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Definiujemy, na których ekranach ma być widoczny dolny pasek
    val bottomBarRoutes = listOf(
        Routes.HOME, Routes.ANIMALS, Routes.SCHEDULE, Routes.PROFILE
    )

    val showBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController, currentRoute = currentDestination?.route)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Animals,
        BottomNavItem.Schedule,
        BottomNavItem.Profile
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Zabezpiecza przed wielokrotnym klikaniem w ten sam przycisk
                        launchSingleTop = true

                        // Zapisuje stan ekranu (np. przewinięcie listy), z którego wychodzimy
                        restoreState = true

                        // Czyści stos aż do ekranu startowego grafu,
                        // by nie tworzyć nieskończonej historii podczas klikania zakładek
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}