package com.turkusowi.animalsheltermenager.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(Routes.HOME, "Główna", Icons.Default.Home)
    object Animals : BottomNavItem(Routes.ANIMALS, "Zwierzęta", Icons.Default.Pets)
    object Schedule : BottomNavItem(Routes.SCHEDULE, "Terminy", Icons.Default.DateRange)
    object Profile : BottomNavItem(Routes.PROFILE, "Profil", Icons.Default.Person)
}