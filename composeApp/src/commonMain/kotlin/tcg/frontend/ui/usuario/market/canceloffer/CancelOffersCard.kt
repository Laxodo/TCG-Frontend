package tcg.frontend.ui.usuario.market.canceloffer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HideSource
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import tcg.frontend.dominio.Offer
import tcg.frontend.ui.usuario.market.tools.ExchangeType

@Composable
fun CancelOffersCard(
    item: Offer,
    onCancel: (Offer) -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        when (item.exchangeType){
            ExchangeType.SELL.type -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = item.imagecardOffer,
                            contentDescription = "Image",
                        )
                        Text(
                            text = item.expansionName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = item.price.toString() + "€",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = item.psa?.toString() ?: "Sin gradear",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            OutlinedIconButton(onClick = {onCancel(item)}) {
                                Icon(Icons.Default.Delete, contentDescription = "Ver")
                            }
                        }
                    }
                }
            }

            ExchangeType.EXCHANGES.type -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Tuyo",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            AsyncImage(
                                model = item.imagecardOffer,
                                contentDescription = "Image",
                            )
                            Text(
                                text = item.expansionName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.psa?.toString() ?: "Sin gradear",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Icon(Icons.Default.SwapHoriz, contentDescription = "Ver")

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "A recibir",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            AsyncImage(
                                model = item.imageCardDemanded,
                                contentDescription = "Image",
                            )
                            Text(
                                text = item.expansionName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.psa?.toString() ?: "Sin gradear",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        OutlinedIconButton(onClick = {onCancel(item)}) {
                            Icon(Icons.Default.Delete, contentDescription = "Ver")
                        }
                    }
                }
            }
        }
    }
}