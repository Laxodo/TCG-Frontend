package tcg.frontend.ui.usuario.market.exchangeoffers.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import tcg.frontend.dominio.Offer
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.market.exchangeoffers.ExchangeOffersViewModel
import tcg.frontend.ui.usuario.market.selloffers.SellOffersViewModel

@Composable
fun ExchangeOffersView(
    exchangeOffersViewModel: ExchangeOffersViewModel,
    onExchange: (Offer) -> Unit,
    onClose: () -> Unit
){
    val item = exchangeOffersViewModel.selected.collectAsState()
    val scroll =  rememberScrollState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.value?.imagecardOffer,
                contentDescription = "Image"
            )
            Text(
                text = item.value?.expansionName ?: "MISSINGNO",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.value?.psa?.toString() ?: "Sin gradear",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.value?.price?.toString() ?: "0€",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalButton(onClick = { item.value?.let { onExchange(it) } }) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null)
                }
                FilledTonalButton(onClick = { onClose() }) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}