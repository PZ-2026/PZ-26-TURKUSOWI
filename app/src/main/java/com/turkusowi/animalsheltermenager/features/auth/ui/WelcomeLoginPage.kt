package com.turkusowi.animalsheltermenager.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeLoginPage(
    onLoginSuccess: () -> Unit,
    onEnterAsGuest: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Animal Shelter Manager",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Zapamiętaj mnie", style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onNavigateToForgotPassword) {
                Text("Nie pamiętasz hasła?", style = MaterialTheme.typography.bodySmall)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

                // Kolejność walidacji
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Uzupełnij email i hasło!"
                } else if (!emailRegex.matches(email)) {
                    errorMessage = "Podaj poprawny format email!"
                } else if (email == "admin@schronisko.pl" && password == "admin123") {
                    errorMessage = ""
                    onLoginSuccess()
                } else if (email == "user@schronisko.pl" && password == "user123") {
                    errorMessage = ""
                    onLoginSuccess()
                } else {
                    errorMessage = "Błędne dane logowania!"
                }
            },
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Testy:\nadmin@schronisko.pl / admin123\nuser@schronisko.pl / user123",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}