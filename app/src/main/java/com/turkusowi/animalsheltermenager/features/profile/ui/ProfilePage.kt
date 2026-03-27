package com.turkusowi.animalsheltermenager.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfilePage() {
    val backgroundColor = Color(0xFFFFFBEB) // Kremowy beż
    val orangeMain = Color(0xFFFF8C00)

    // POPRAWKA: Tło backgroundColor przypisane do LazyColumn na całą szerokość
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 48.dp) // POPRAWKA: Mniejszy padding, żeby karty dojeżdżały do footera
    ) {
        // --- NAGŁÓWEK (Białe tło na samej górze) ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Góra nadal biała
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = Color(0xFFFFCC80)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("DS", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Dariusz Szymanek", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Wolontariusz • Staż: 6 m-cy", fontSize = 14.sp, color = Color.Gray)
            }
        }

        // --- SEKCJA MOJE SPACERY (Kremowe tło zaczyna się tutaj) ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Moje spacery", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Historia →", color = Color(0xFFE57373), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Karta spaceru 1
                WalkCard("Maks (Labrador)", "Sektor B, Boks 12", "Jutro, 10:30 - 11:30", "🐶")
                Spacer(modifier = Modifier.height(12.dp))
                // Karta spaceru 2
                WalkCard("Azor (Owczarek)", "Sektor A, Boks 04", "25 Marca, 14:00 - 15:00", "🐕")

                Spacer(modifier = Modifier.height(32.dp)) // Większy odstęp przed ulubieńcami

                // --- SEKCJA ULUBIEŃCY ❤️ (Układ 2x2 z kartami jak na Home) ---
                Text("Ulubieńcy ❤️", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // Rząd 1
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Używamy PetMiniCardWithHeart (jak na Home)
                    PetMiniCardWithHeart("Luna", "🐱", Modifier.weight(1f))
                    PetMiniCardWithHeart("Chrupek", "🐹", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Rząd 2
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    PetMiniCardWithHeart("Puszek", "🐱", Modifier.weight(1f))
                    PetMiniCardWithHeart("Burek", "🐶", Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun WalkCard(name: String, location: String, time: String, emoji: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Box(contentAlignment = Alignment.Center) { Text(emoji, fontSize = 24.sp) }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(location, fontSize = 12.sp, color = Color.Gray)
                Text(time, fontSize = 12.sp, color = Color(0xFFFF8C00), fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Anuluj", fontSize = 11.sp)
            }
        }
    }
}

// --- ZMODYFIKOWANA KARTA ZWIERZAKA Z CZERWONYM SERDUSZKIEM (Styl z Home) ---
@Composable
fun PetMiniCardWithHeart(name: String, emoji: String, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                // POPRAWKA: Czerwone, wypełnione serduszko ❤️ na białym kółku
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(28.dp),
                    shape = CircleShape,
                    color = Color.White // Białe tło kółka
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("❤️", fontSize = 14.sp) // Czerwone serduszko
                    }
                }

                Text(emoji, fontSize = 44.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Dostępny", fontSize = 12.sp, color = Color.Gray) // Uproszczony opis

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFE8F5E9), // Zielone tło statusu "Dostępny"
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Dostępny",
                    color = Color(0xFF4CAF50),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}