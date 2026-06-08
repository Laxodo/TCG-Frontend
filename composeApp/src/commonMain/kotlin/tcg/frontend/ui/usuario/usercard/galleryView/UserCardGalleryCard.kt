package tcg.frontend.ui.usuario.usercard.galleryView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO

@Composable
fun UserCardGalleryCard(
    userCardGalleryViewModel: UserCardGalleryViewModel,
    item: UserCardDTO,
    onView: (UserCardDTO) -> Unit
) {
    val items by userCardGalleryViewModel.selectedItems.collectAsState()
    val active = items.contains(item)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = if (active) BorderStroke(1.dp, Color.Green) else BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        onClick = {
            onView(item)
            },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.card.frontcard,
                    contentDescription = "Image",
                )
                if (item.userCard.sold) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Vendido",
                        tint = Color(0xFF85BB65),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            Text(
                text = item.card.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.userCard.psa?.toString() ?: "Sin gradear",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.card.rarity,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.card.cardNumber.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.userCard.price.toString() + "€",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}