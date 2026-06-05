package tcg.frontend.ui.administracion.users.logs.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import tcg.frontend.dominio.LogActivity
import tcg.frontend.ui.administracion.users.logs.detail.tools.Action

@Composable
fun ActivityCard(item: LogActivity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = if (item.action == Action.GET.type) {
            BorderStroke(2.dp, Color.Green)
        } else{
            BorderStroke(2.dp, Color.Red)
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.card?.frontcard,
                contentDescription = "Image",
            )
            Text(
                text = "Precio: ${item.price}€",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (item.psa != null) "PSA: ${item.psa}" else "Sin gradear",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}