package com.turkusowi.animalsheltermenager.features.admin.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.turkusowi.animalsheltermenager.features.admin.EmployeeEditViewModel
import com.turkusowi.animalsheltermenager.features.admin.EmployeeRole
import com.turkusowi.animalsheltermenager.features.admin.EmployeeStatus
import com.turkusowi.animalsheltermenager.features.admin.EmploymentType

@Composable
fun EmployeeEditPage(
    viewModel: EmployeeEditViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(context, "Dane zapisane.", Toast.LENGTH_SHORT).show()
            viewModel.consumeSaveSuccess()
            onBackClick()
        }
    }

    if (state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.employeeNotFound) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.errorMessage ?: "Nie znaleziono pracownika.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onBackClick) {
                Text("Wróć")
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SecondaryBackButton(
            text = "× Zamknij",
            onClick = onBackClick
        )

        Text(
            text = "Edycja Pracownika",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "PROFIL OSOBOWY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = state.firstName,
            onValueChange = viewModel::onFirstNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Imię") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = viewModel::onLastNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nazwisko") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Adres e-mail") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ZATRUDNIENIE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        SimpleDropdownField(
            label = "Stanowisko",
            selectedText = state.role.label,
            options = EmployeeRole.entries,
            optionLabel = { it.label },
            onOptionSelected = viewModel::onRoleChange
        )

        Spacer(modifier = Modifier.height(10.dp))

        SimpleDropdownField(
            label = "Status",
            selectedText = state.status.label,
            options = EmployeeStatus.entries,
            optionLabel = { it.label },
            onOptionSelected = viewModel::onStatusChange
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            OutlinedTextField(
                value = state.salaryPln,
                onValueChange = viewModel::onSalaryChange,
                modifier = Modifier.weight(1f),
                label = { Text("Pensja [PLN]") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(0.08f))

            Column(modifier = Modifier.weight(1f)) {
                SimpleDropdownField(
                    label = "Typ umowy",
                    selectedText = state.employmentType.label,
                    options = EmploymentType.entries,
                    optionLabel = { it.label },
                    onOptionSelected = viewModel::onEmploymentTypeChange
                )
            }
        }

        state.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = viewModel::saveChanges,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("ZAPISZ DANE")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Anuluj i wróć")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun <T> SimpleDropdownField(
    label: String,
    selectedText: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = { Text(label) },
            singleLine = true,
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}