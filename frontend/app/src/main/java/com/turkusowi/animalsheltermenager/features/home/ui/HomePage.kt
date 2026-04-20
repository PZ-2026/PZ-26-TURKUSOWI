package com.turkusowi.animalsheltermenager.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomePage(
    onProfileClick: () -> Unit = {},
    onAnimalClick: (String) -> Unit = {} // ZMIANA: Dodano akcję kliknięcia w zwierzaka
) {
    // DOKŁADNE KOLORY Z FIGMY
    val backgroundColor = Color(0xFFFFFBEB)
    val orangeMain = Color(0xFFFF8C00)
    val darkButton = Color(0xFF212121)
    val cardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF9800), Color(0xFFF44336))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ) {
        // 1. Status Bar / Avatar Row
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onProfileClick() },
                shape = CircleShape,
                color = Color(0xFFFFCC80)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("DS", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Pomarańczowa Karta Gradientowa
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(cardGradient)
                    .padding(24.dp)
            ) {
                Column {
                    Text("Cześć, Dariusz! 👋", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Masz dzisiaj zaplanowane 3 spacery z podopiecznymi.",
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Akcja rezerwacji */ },
                        colors = ButtonDefaults.buttonColors(containerColor = darkButton),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text("Zarezerwuj nowy spacer", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Statystyki (Dwa kafelki obok siebie)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeStatCard("48", "Zwierząt", Modifier.weight(1f), orangeMain)
            HomeStatCard("15", "Dostępnych", Modifier.weight(1f), orangeMain)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 4. Sekcja "Nasi podopieczni"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Nasi podopieczni", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D))
            TextButton(onClick = { /* Zobacz wszystkie */ }) {
                Text("Wszystkie", color = orangeMain, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 5. SIATKA ZWIERZAKÓW ---
        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ZMIANA: Przekazujemy onClick z odpowiednim imieniem zwierzaka
            item { PetMiniCard("Maks", "Labrador • 4 lata", "🐶", "Dostępny") { onAnimalClick("Maks") } }
            item { PetMiniCard("Luna", "Syberyjski • 2 lata", "🐱", "Dostępna") { onAnimalClick("Luna") } }
            item { PetMiniCard("Burek", "Mieszaniec • 6 lat", "🐕", "Na spacerze") { onAnimalClick("Burek") } }
            item { PetMiniCard("Puszek", "Perski • 1 rok", "🐱", "Zarezerwowany") { onAnimalClick("Puszek") } }
            item { PetMiniCard("Azor", "Owczarek • 3 lata", "🐶", "Dostępny") { onAnimalClick("Azor") } }
            item { PetMiniCard("Chrupek", "Chomik • 0.5 roku", "🐹", "Dostępny") { onAnimalClick("Chrupek") } }
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
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = accentColor)
            Text(label, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetMiniCard(name: String, desc: String, emoji: String, status: String, onClick: () -> Unit) {
    val statusColor = when (status) {
        "Dostępny", "Dostępna" -> Color(0xFFE8F5E9) to Color(0xFF4CAF50)
        "Na spacerze" -> Color(0xFFE3F2FD) to Color(0xFF2196F3)
        "Zarezerwowany", "Zarezerwowana" -> Color(0xFFFFEBEE) to Color(0xFFE53935)
        else -> Color(0xFFF5F5F5) to Color(0xFF757575)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFFFDF7E7), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(28.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.8f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🤍", fontSize = 12.sp)
                    }
                }

                Text(emoji, fontSize = 44.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ZMIANA: Dodano wymuszony czarny kolor dla imienia (color = Color.Black)
            Text(name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)

            Text(desc, fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = statusColor.first,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = status,
                    color = statusColor.second,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}