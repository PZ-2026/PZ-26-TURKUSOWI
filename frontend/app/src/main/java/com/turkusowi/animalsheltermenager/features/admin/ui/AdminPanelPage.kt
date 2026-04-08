package com.turkusowi.animalsheltermenager.features.admin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.turkusowi.animalsheltermenager.features.admin.AdminPanelViewModel

@Composable
fun AdminPanelPage(
    viewModel: AdminPanelViewModel,
    onManageAccountsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                InitialsAvatar(initials = state.adminInitials)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = state.adminName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = state.adminRole,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DashboardStatCard(
                        value = state.managedAccounts,
                        label = "Konta",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.weight(0.08f))

                    DashboardStatCard(
                        value = state.newReports,
                        label = "Nowe raporty",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Panel administracyjny",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminActionRow(
                title = "Zarządzanie kontami",
                onClick = onManageAccountsClick
            )

            AdminActionRow(
                title = "Raporty systemowe",
                onClick = {
                    Toast.makeText(
                        context,
                        "Widok raportów jeszcze nie jest gotowy.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            AdminActionRow(
                title = "Konfiguracja schroniska",
                onClick = {
                    Toast.makeText(
                        context,
                        "Konfiguracja schroniska będzie dodana później.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            AdminActionRow(
                title = "Wyloguj się",
                onClick = onLogoutClick,
                destructive = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}