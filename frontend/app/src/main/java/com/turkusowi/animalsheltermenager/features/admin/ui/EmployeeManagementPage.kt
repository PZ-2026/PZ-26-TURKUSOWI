package com.turkusowi.animalsheltermenager.features.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.turkusowi.animalsheltermenager.features.admin.EmployeeManagementViewModel

@Composable
fun EmployeeManagementPage(
    viewModel: EmployeeManagementViewModel,
    onEmployeeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SecondaryBackButton(
            text = "← Wróć",
            onClick = onBackClick
        )

        Text(
            text = "Zarządzanie kontami",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Szukaj pracownika") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterChip(
            selected = state.onlyActive,
            onClick = { viewModel.onOnlyActiveChange(!state.onlyActive) },
            label = { Text("Tylko aktywni") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Znaleziono: ${state.employees.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.employees.isEmpty()) {
            Text(
                text = "Brak pracowników spełniających kryteria.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = state.employees,
                    key = { it.id }
                ) { employee ->
                    EmployeeRowCard(
                        employee = employee,
                        onClick = { onEmployeeClick(employee.id) }
                    )
                }
            }
        }
    }
}