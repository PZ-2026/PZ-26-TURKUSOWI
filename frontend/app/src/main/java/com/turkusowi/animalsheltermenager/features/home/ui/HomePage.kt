package com.turkusowi.animalsheltermenager.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkusowi.animalsheltermenager.features.animals.Animal
import com.turkusowi.animalsheltermenager.features.home.HomeUiState

@Composable
fun HomePage(
    state: HomeUiState,
    onProfileClick: () -> Unit = {},
    onAnimalClick: (Animal) -> Unit = {},
    onReserveClick: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFFFFBEB)
    val orangeMain = Color(0xFFFF8C00)
    val darkButton = Color(0xFF212121)
    val cardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF9800), Color(0xFFF44336))
    )

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Surface(
                modifier = Modifier.size(44.dp).clickable { onProfileClick() },
                shape = CircleShape,
                color = Color(0xFFFFCC80)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(state.greetingName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), elevation = CardDefaults.cardElevation(6.dp)) {
            Box(modifier = Modifier.background(cardGradient).padding(24.dp)) {
                Column {
                    Text("Czesc, ${state.greetingName}!", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Masz obecnie zaplanowane ${state.todayWalks} spacery z podopiecznymi.",
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onReserveClick,
                        colors = ButtonDefaults.buttonColors(containerColor = darkButton),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text("Przejdz do terminow", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HomeStatCard(state.animalCount.toString(), "Zwierzat", Modifier.weight(1f), orangeMain)
            HomeStatCard(state.availableCount.toString(), "Dostepnych", Modifier.weight(1f), orangeMain)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Nasi podopieczni", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D))
            state.errorMessage?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(state.featuredAnimals.size) { index ->
                val animal = state.featuredAnimals[index]
                PetMiniCard(animal = animal, onClick = { onAnimalClick(animal) })
            }
        }
    }
}

@Composable
fun HomeStatCard(value: String, label: String, modifier: Modifier, accentColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = accentColor)
            Text(label, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PetMiniCard(animal: Animal, onClick: () -> Unit) {
    val statusColor = when {
        animal.status.contains("ADOPCJI", true) || animal.status.contains("Dostep", true) -> Color(0xFFE8F5E9) to Color(0xFF4CAF50)
        animal.status.contains("SPACER", true) -> Color(0xFFE3F2FD) to Color(0xFF2196F3)
        animal.status.contains("ADOPTOWANY", true) -> Color(0xFFFFEBEE) to Color(0xFFE53935)
        else -> Color(0xFFF5F5F5) to Color(0xFF757575)
    }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(110.dp).background(Color(0xFFFDF7E7), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (animal.animalType.contains("Kot", true)) "\uD83D\uDC31" else "\uD83D\uDC36", fontSize = 44.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
            Text("${animal.breed} • ${animal.age}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Surface(color = statusColor.first, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = animal.status,
                    color = statusColor.second,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
