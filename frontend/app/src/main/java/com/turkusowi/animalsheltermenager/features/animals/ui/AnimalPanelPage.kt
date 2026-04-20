package com.turkusowi.animalsheltermenager.features.animals.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkusowi.animalsheltermenager.features.animals.Animal

@Composable
fun AnimalPanelPage(
    animal: Animal,
    onBackClick: () -> Unit = {}
) {
    // ZMIANA: Zmienna do pamiętania czasu ostatniego kliknięcia
    var lastClickTime by remember { mutableLongStateOf(0L) }

    val statusBgColor = when (animal.status) {
        "Dostępny", "Dostępna" -> Color(0xFFE8F5E9)
        "Na spacerze" -> Color(0xFFE3F2FD)
        "Zarezerwowany", "Zarezerwowana" -> Color(0xFFFFEBEE)
        else -> Color(0xFFF5F5F5)
    }

    val statusTextColor = when (animal.status) {
        "Dostępny", "Dostępna" -> Color(0xFF4CAF50)
        "Na spacerze" -> Color(0xFF2196F3)
        "Zarezerwowany", "Zarezerwowana" -> Color(0xFFE53935)
        else -> Color(0xFF757575)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "← Podopieczny",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .clickable {
                    // ZMIANA: Zabezpieczenie przed tzw. "double clickiem"
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 1000L) { // Blokada na 1000 milisekund (1 sekunda)
                        lastClickTime = currentTime
                        onBackClick()
                    }
                },
            color = Color.Black
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp).fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFFDF7E7),
                    modifier = Modifier.size(120.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = when (animal.breed.contains("Kot")) {
                                true -> "🐱"
                                else -> "🐶"
                            },
                            fontSize = 60.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = animal.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "${animal.breed} • ${animal.age}", color = Color.Black)

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = statusBgColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = animal.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = statusTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoCard(label = "Waga", value = animal.weight, modifier = Modifier.weight(1f))
            InfoCard(label = "Płeć", value = animal.gender, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* TODO: AnimalPanelPage -> button action */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ZAPLANUJ SPACER", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, fontSize = 12.sp, color = Color.Black)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}