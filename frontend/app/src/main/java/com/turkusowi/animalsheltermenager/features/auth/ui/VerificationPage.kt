package com.turkusowi.animalsheltermenager.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun VerificationPage(
    onVerifySuccess: () -> Unit,
    onResendCode: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Weryfikacja",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Wprowadź 4-cyfrowy kod autoryzacyjny, który wysłaliśmy na Twój email.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = code,
            onValueChange = {
                // Pozwala wpisać tylko cyfry i max 4 znaki
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    code = it
                }
            },
            label = { Text("Kod weryfikacyjny") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (code.isBlank()) {
                    errorMessage = "Wpisz kod!"
                } else if (code.length < 4) {
                    errorMessage = "Kod musi składać się z 4 cyfr!"
                } else {
                    errorMessage = ""
                    onVerifySuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Potwierdź")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nie dostałeś maila?", style = MaterialTheme.typography.bodyMedium)

        TextButton(onClick = onResendCode) {
            Text("Wyślij ponownie")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Powrót do logowania")
        }
    }
}