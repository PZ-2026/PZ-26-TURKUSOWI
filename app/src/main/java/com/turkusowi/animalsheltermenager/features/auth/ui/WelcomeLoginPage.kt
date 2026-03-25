package com.turkusowi.animalsheltermenager.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeLoginPage(
    onLoginSuccess: () -> Unit,
    onEnterAsGuest: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Tymczasowe stany dla pól tekstowych
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tu w przyszłości wrzucisz Image() z logo aplikacji
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Pets,
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Animal Shelter Menager",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginSuccess, // Symulacja udanego logowania
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zaloguj się")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onEnterAsGuest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Wejdź jako Gość")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Nie masz konta? Zarejestruj się")
        }
    }
}