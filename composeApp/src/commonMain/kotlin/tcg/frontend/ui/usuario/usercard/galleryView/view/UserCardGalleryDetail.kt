package tcg.frontend.ui.usuario.usercard.galleryView.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Preview
@Composable
fun UserCardGalleryDetail(
    userCardGalleryViewViewModel: UserCardGalleryDetailViewModel,
    onQuickSell: () -> Unit,
    onSell: () -> Unit,
    onGrade: () -> Unit,
    onExchange: () -> Unit,
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
            Card(userCardGalleryViewViewModel, onQuickSell, onSell, onGrade, onExchange, onClose)
        }
    }
}

@Composable
fun Card(
    userCardGalleryViewViewModel: UserCardGalleryDetailViewModel,
    onQuickSell: () -> Unit,
    onSell: () -> Unit,
    onGrade: () -> Unit,
    onExchange: () -> Unit,
    onClose: () -> Unit
){
    val selectedCard = userCardGalleryViewViewModel.userCard.value
    var cardImageState by remember { mutableStateOf(true) }

    AsyncImage(
        model = if (cardImageState) selectedCard?.card?.frontcard else selectedCard?.card?.backcard,
        contentDescription = "Image",
        modifier = Modifier
            .size(375.dp)
            .clickable{
                cardImageState = !cardImageState
            }
    )

    Text(
        text = selectedCard?.card?.name ?: "MISSIGNO",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    if (selectedCard?.userCard?.sold ?: false) {
        Icon(
            imageVector = Icons.Default.MonetizationOn,
            contentDescription = null,
            tint = Color(0xFF85BB65),
            modifier = Modifier.size(60.dp)
        )
    }

    Text(
        text = selectedCard?.card?.rarity ?: "Common",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = selectedCard?.userCard?.psa?.toString() ?: "Sin gradear",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    if (selectedCard?.userCard?.sold ?: false) {
        Text(
            text = selectedCard?.userCard?.price?.toString() + "€",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    } else {
        Text(
            text = selectedCard?.card?.price.toString() + "€",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Text(
        text = "Nº " + selectedCard?.card?.cardNumber.toString(),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.fillMaxWidth(0.5f)
    )

    if (selectedCard?.userCard?.sold == false) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilledTonalButton(onClick = { onQuickSell() }) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null)
                Text("Subir al mercado")
            }
            FilledTonalButton(onClick = { onSell() }) {
                Icon(Icons.Default.FlashOn, contentDescription = null)
                Text("Venta rapida")
            }
            FilledTonalButton(onClick = { onExchange() }) {
                Icon(Icons.Default.ChangeCircle, contentDescription = null)
                Text("Intercambiar")
            }
            FilledTonalButton(onClick = { onGrade() }) {
                Icon(Icons.Default.Verified, contentDescription = null)
                Text("Gradear")
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilledTonalButton(onClick = { onClose() }) {
            Icon(Icons.Default.Close, contentDescription = null)
        }
    }
}