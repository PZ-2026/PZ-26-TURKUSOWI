package com.turkusowi.animalsheltermenager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.turkusowi.animalsheltermenager.core.navigation.MainScaffold
import com.turkusowi.animalsheltermenager.core.theme.AnimalShelterMenagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // To sprawia, że apka rysuje się pod paskiem powiadomień i nawigacji systemowej
        enableEdgeToEdge()

        setContent {
            AnimalShelterMenagerTheme {
                // Odpalamy nasz główny szkielet
                MainScaffold()
            }
        }
    }
}