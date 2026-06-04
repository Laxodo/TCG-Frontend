package tcg.frontend.ui.administracion.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tcg.frontend.infraestructura.entities.card.CreateCardRequest

@Composable
fun CardCreate(
    cardViewModel: CardViewModel,
    expansionId: Int,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var rarity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var frontcard by remember { mutableStateOf("") }
    var backcard by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Carta", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = rarity,
            onValueChange = { rarity = it },
            label = { Text("Rareza") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Número") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = frontcard,
            onValueChange = { frontcard = it },
            label = { Text("Imagen frontal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = backcard,
            onValueChange = { backcard = it },
            label = { Text("Imagen trasera") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    cardViewModel.createCard(
                        CreateCardRequest(
                            id_expansion = expansionId,
                            name = name,
                            rarity = rarity,
                            price = price.toFloatOrNull() ?: 0f,
                            card_number = cardNumber.toIntOrNull() ?: 0,
                            frontcard = frontcard,
                            backcard = backcard
                        )
                    )
                    onBack()
                }
            ) {
                Text("Guardar")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = onBack
            ) {
                Text("Cancelar")
            }
        }
    }
}