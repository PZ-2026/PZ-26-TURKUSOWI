package com.turkusowi.animalsheltermenager.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ForgotPasswordPage(
    onSendEmailClick: () -> Unit, // Ta nazwa musi się zgadzać z AppNavGraph
    onNavigateBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }

    // Symulacja bazy danych - domyślnie na TRUE
    val userExistsInDatabase = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resetowanie hasła",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSent) {
            Text(
                text = "Podaj swój email, aby otrzymać link do resetowania hasła.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

                    if (email.isBlank()) {
                        errorMessage = "Wpisz adres email!"
                    } else if (!emailRegex.matches(email)) {
                        errorMessage = "To nie jest poprawny format email!"
                    } else if (!userExistsInDatabase) {
                        errorMessage = "Nie znaleźliśmy konta przypisanego do tego maila."
                    } else {
                        errorMessage = ""
                        isSent = true // Pokazuje ekran sukcesu
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wyślij link")
            }
        } else {
            // Widok po "wysłaniu" maila
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Link wysłany na: $email", fontWeight = FontWeight.Medium)
            Text(text = "Sprawdź skrzynkę odbiorczą.", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSendEmailClick, // Przycisk powrotu po sukcesie
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok, rozumiem")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Powrót do logowania")
        }
    }
}