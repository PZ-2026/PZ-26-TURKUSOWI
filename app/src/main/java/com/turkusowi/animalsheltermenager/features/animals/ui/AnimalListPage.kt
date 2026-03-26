package com.turkusowi.animalsheltermenager.features.animals.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.tooling.preview.Preview
import com.turkusowi.animalsheltermenager.features.animals.Animal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListPage(
    animals: List<Animal>,
    onAnimalClick: (Animal) -> Unit /* TODO  */
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB)) // Jasne tło
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "← Podopieczny", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "16:32", color = Color.Gray, fontSize = 12.sp) // Czas ze zdjęcia
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            placeholder = { Text("Szukaj po imieniu lub rasie...", color = Color.Gray) },
            leadingIcon = { Text("🔍") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF8C00),
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFFFF8C00),
                focusedLabelColor = Color(0xFFFF8C00),
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Button(
                onClick = { /* TODO: Filtrowanie */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Wszystkie", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(animals) { animal ->
                AnimalGridItem(animal = animal, onClick = { onAnimalClick(animal) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalGridItem(animal: Animal, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // "Zdjecie" (Emoji)
            Box(
                modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (animal.breed.contains("Kot")) {
                        true -> "🐱"
                        else -> "🐶"
                    },
                    fontSize = 50.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = animal.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "${animal.breed} • ${animal.age}", color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))

            val statusColor = when (animal.status) {
                "Dostępny" -> Color(0xFFE8F5E9)
                "Zarezerwowany" -> Color(0xFFFBE9E7)
                "Na spacerze" -> Color(0xFFE3F2FD)
                else -> Color.LightGray
            }
            val statusTextColor = when (animal.status) {
                "Dostępny" -> Color(0xFF4CAF50)
                "Zarezerwowany" -> Color(0xFFD32F2F)
                "Na spacerze" -> Color(0xFF1976D2)
                else -> Color.Black
            }

            Surface(
                color = statusColor,
                shape = RoundedCornerShape(8.dp)
            ) {
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
@Preview(showBackground = true)
@Composable
fun AnimalListPreview() {
    val dummyAnimals = listOf(
        Animal("Maks", "Labrador", "4 lata", "24.5 kg", "Samiec", "Dostępny"),
        Animal("Luna", "Kot Syberyjski", "2 lata", "4 kg", "Samica", "Dostępny"),
        Animal("Burek", "Mieszaniec", "6 lat", "12 kg", "Samiec", "Na spacerze")
    )

    AnimalListPage(animals = dummyAnimals, onAnimalClick = {})
}