package com.turkusowi.animalsheltermenager.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.turkusowi.animalsheltermenager.features.auth.AuthUiState

@Composable
fun WelcomeLoginPage(
    state: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onEnterAsGuest: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

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
            label = { Text("Haslo") },
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
                Text("Zapamietaj mnie", style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onNavigateToForgotPassword) {
                Text("Nie pamietasz hasla?", style = MaterialTheme.typography.bodySmall)
            }
        }

        state.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        } ?: Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text(if (state.isLoading) "Logowanie..." else "Zaloguj sie")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onEnterAsGuest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Wejdz jako Gosc")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Nie masz konta? Zarejestruj sie")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Testy:\nadmin@schronisko.pl / admin123\nuser@schronisko.pl / user123",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
