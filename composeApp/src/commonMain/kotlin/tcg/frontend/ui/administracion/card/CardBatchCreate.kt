package tcg.frontend.ui.administracion.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import tcg.frontend.infraestructura.entities.card.CreateCardRequest
import tcg.frontend.ui.administracion.media.MediaViewModel

@Composable
fun CardBatchCreate(
    cardViewModel: CardViewModel,
    expansionId: Int,
    onPickImage: suspend () -> Pair<String, ByteArray>?,
    onBack: () -> Unit
) {
    val mediaViewModel: MediaViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    var rarity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var backCardPath by remember { mutableStateOf("") }
    val cards = remember { mutableStateListOf(BatchCardRow()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear lote de cartas", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = rarity,
            onValueChange = { rarity = it },
            label = { Text("Rareza en común") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio en común") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    onPickImage()?.let { file ->
                        val result = mediaViewModel.uploadImage(file.first, file.second)

                        result.onSuccess { path ->
                            backCardPath = path
                        }.onFailure {  }
                    }
                }
            }
        ) {
            Text(if (backCardPath.isBlank()) "Subir backcard común" else "Imagen subida")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(cards) { index, card ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = card.name,
                            onValueChange = { cards[index] = card.copy(name = it) },
                            label = { Text("Nombre") }
                        )

                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = card.cardNumber,
                            onValueChange = { cards[index] = card.copy(cardNumber = it) },
                            label = { Text("Número") }
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    onPickImage()?.let { file ->
                                        val result = mediaViewModel.uploadImage(file.first, file.second)
                                        result.onSuccess { path ->
                                            cards[index] = card.copy(frontCard = path)
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(if (card.frontCard.isBlank()) "Subir Frontcard" else "Imagen subida" )
                        }
                    }
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = {
                    cards.add(
                        BatchCardRow()
                    )
                }
            ) {
                Text("Añadir")
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    val request =
                        cards.map {
                            CreateCardRequest(
                                id_expansion = expansionId,
                                name = it.name,
                                rarity = rarity,
                                price = price.toFloatOrNull() ?: 0f,
                                card_number = it.cardNumber.toIntOrNull() ?: 0,
                                frontcard = it.frontCard,
                                backcard = backCardPath
                            )
                        }
                    cardViewModel.createBatch(request)
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