package com.turkusowi.animalsheltermenager.features.animals.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkusowi.animalsheltermenager.features.animals.Animal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListPage(
    animals: List<Animal>,
    onAnimalClick: (Animal) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredAnimals = animals.filter {
        searchText.isBlank() ||
                it.name.contains(searchText, ignoreCase = true) ||
                it.breed.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFFFBEB)).padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Podopieczni", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Lacznie: ${animals.size}", color = Color.Gray, fontSize = 12.sp)
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            placeholder = { Text("Szukaj po imieniu lub rasie...", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF8C00),
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFFFF8C00),
                focusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)), shape = RoundedCornerShape(12.dp)) {
                Text("Wszystkie", color = Color.White)
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f)) {
            items(filteredAnimals) { animal ->
                AnimalGridItem(animal = animal, onClick = { onAnimalClick(animal) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalGridItem(animal: Animal, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                Text(text = if (animal.animalType.contains("Kot", true)) "\uD83D\uDC31" else "\uD83D\uDC36", fontSize = 50.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = animal.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "${animal.breed} • ${animal.age}", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            val statusColor = when {
                animal.status.contains("ADOPCJI", true) -> Color(0xFFE8F5E9)
                animal.status.contains("ADOPTOWANY", true) -> Color(0xFFFBE9E7)
                animal.status.contains("SPACER", true) -> Color(0xFFE3F2FD)
                else -> Color.LightGray
            }
            val statusTextColor = when {
                animal.status.contains("ADOPCJI", true) -> Color(0xFF4CAF50)
                animal.status.contains("ADOPTOWANY", true) -> Color(0xFFD32F2F)
                animal.status.contains("SPACER", true) -> Color(0xFF1976D2)
                else -> Color.Black
            }

            Surface(color = statusColor, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = animal.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = statusTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
