package com.turkusowi.animalsheltermenager.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkusowi.animalsheltermenager.features.home.ui.PetMiniCard
import com.turkusowi.animalsheltermenager.features.profile.ProfileUiState

@Composable
fun ProfilePage(
    state: ProfileUiState,
    onCancelWalk: (Int) -> Unit = {}
) {
    val backgroundColor = Color(0xFFFFFBEB)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(modifier = Modifier.size(120.dp), shape = CircleShape, color = Color(0xFFFFCC80)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(state.initials.ifBlank { "GS" }, fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(state.fullName.ifBlank { "Gosc schroniska" }, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8C00))
                Text(state.roleLabel, fontSize = 14.sp, color = Color.Gray)
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text("Moje spacery", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (state.walks.isEmpty()) {
                    Text(state.errorMessage ?: "Brak zaplanowanych spacerow.", color = Color.Gray)
                } else {
                    state.walks.forEach { walk ->
                        WalkCard(
                            name = walk.animalName,
                            location = walk.location,
                            time = "${walk.date}, ${walk.startTime}-${walk.endTime}",
                            onCancelClick = { onCancelWalk(walk.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Polecani podopieczni", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                state.favoriteAnimals.chunked(2).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { animal ->
                            Box(modifier = Modifier.weight(1f)) {
                                PetMiniCard(animal = animal, onClick = {})
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun WalkCard(name: String, location: String, time: String, onCancelClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(location, fontSize = 12.sp, color = Color.Gray)
                Text(time, fontSize = 12.sp, color = Color(0xFFFF8C00), fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onCancelClick) {
                Text("Anuluj")
            }
        }
    }
}
