package tcg.frontend.ui.usuario.market.selloffers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import tcg.frontend.dominio.Offer
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryCard

@Composable
fun SellOffers(
    sellOffersViewModel: SellOffersViewModel,
    onView: (Offer) -> Unit,
    onBack: () -> Unit
){
    val items by sellOffersViewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("")}
    val state by sellOffersViewModel.state.collectAsState()
    val filteredItems = items.filter { true }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                FlowRow(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(ButtonDefaults.IconSize)
                        )
                    }
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text("Buscar...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                if (filteredItems.isNotEmpty()){
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(
                            minSize = 512.dp
                        )
                    ) {
                        items(filteredItems.size) { item ->
                            SellOffersCard(
                                filteredItems[item],
                                {
                                    onView(it)
                                }
                            )
                        }
                    }
                }else{
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        text = "No hay ofertas de cartas.",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    )
                }
            }
        }
    }
}