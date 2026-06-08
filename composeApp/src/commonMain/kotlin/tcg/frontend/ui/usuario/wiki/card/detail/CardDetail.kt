package tcg.frontend.ui.usuario.wiki.card.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import tcg.frontend.ui.usuario.wiki.card.CardViewModel

@Composable
fun CardDetail(
    cardViewModel: CardViewModel,
    onClose: () -> Unit
) {
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
            Card(cardViewModel, onClose)
        }
    }
}

@Composable
fun Card(
    cardViewModel: CardViewModel,
    onClose: () -> Unit
){
    val selectedCard by cardViewModel.selected.collectAsState()
    var cardImageState by remember { mutableStateOf(true) }

    AsyncImage(
        model = if (cardImageState) selectedCard?.frontcard else selectedCard?.backcard,
        contentDescription = "Image",
        modifier = Modifier
            .size(375.dp)
            .clickable{
                cardImageState = !cardImageState
            }
    )

    Text(
        text = selectedCard?.name ?: "MISSIGNO",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = selectedCard?.rarity ?: "Common",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = selectedCard?.price.toString() + "€",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = "Nº " + selectedCard?.cardNumber.toString(),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.fillMaxWidth(0.5f)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilledTonalButton(onClick = { onClose() }) {
            Icon(Icons.Default.Close, contentDescription = null)
        }
    }
}