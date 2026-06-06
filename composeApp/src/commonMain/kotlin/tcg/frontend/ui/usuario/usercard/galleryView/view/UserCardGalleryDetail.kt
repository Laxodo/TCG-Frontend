package tcg.frontend.ui.usuario.usercard.galleryView.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import tcg.frontend.ui.usuario.wiki.card.CardViewModel

@Preview
@Composable
fun UserCardGalleryDetail(
    userCardGalleryViewViewModel: UserCardGalleryDetailViewModel,
    cardViewModel: CardViewModel,
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
            Card(userCardGalleryViewViewModel, cardViewModel, onQuickSell, onSell, onGrade, onExchange, onClose)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    userCardGalleryViewViewModel: UserCardGalleryDetailViewModel,
    cardViewModel: CardViewModel,
    onQuickSell: () -> Unit,
    onSell: () -> Unit,
    onGrade: () -> Unit,
    onExchange: () -> Unit,
    onClose: () -> Unit
){
    val selectedCard by userCardGalleryViewViewModel.userCard.collectAsState()
    val state by userCardGalleryViewViewModel.uiState.collectAsState()
    val cards by cardViewModel.items.collectAsState()
    val exchangeCard by userCardGalleryViewViewModel.exchangeCard.collectAsState()
    var cardImageState by remember { mutableStateOf(true) }
    var showSellDialog by remember { mutableStateOf(false) }
    var showExchangeDialog by remember { mutableStateOf(false) }

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
            FilledTonalButton(onClick = {
                showSellDialog = true
            }) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null)
                Text("Subir al mercado")
            }
            if (showSellDialog){
                BasicAlertDialog(
                    onDismissRequest = { showSellDialog = false }
                ){
                    Surface(
                        modifier = Modifier.widthIn(max = 300.dp).wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Escoge un precio",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = state.price,
                                onValueChange = { userCardGalleryViewViewModel.onChangePrice(it) },
                                label = { Text("Precio") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.MonetizationOn,
                                        contentDescription = null
                                    )
                                },
                                isError = state.priceErr != null,
                                modifier = Modifier.fillMaxWidth()
                            )
                            state.priceErr?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            FlowRow() {
                                TextButton(
                                    onClick = { showSellDialog = false }
                                ) {
                                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                                }
                                TextButton(
                                    onClick = onSell
                                ) {
                                    Text("Vender", color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
            FilledTonalButton(onClick = { onQuickSell() }) {
                Icon(Icons.Default.FlashOn, contentDescription = null)
                Text("Venta rapida")
            }
            FilledTonalButton(onClick = { showExchangeDialog = true }) {
                Icon(Icons.Default.ChangeCircle, contentDescription = null)
                Text("Intercambiar")
            }
            if (showExchangeDialog){
                BasicAlertDialog(
                    onDismissRequest = { showExchangeDialog = false }
                ){
                    Surface(
                        modifier = Modifier.widthIn(max = 900.dp).wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Elige una carta y un PSA",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier.fillMaxHeight(0.5f)
                            ){
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(
                                        minSize = 128.dp
                                    )
                                ) {
                                    items(cards.size) { item ->
                                        Card(
                                            border = if (exchangeCard == cards[item]) BorderStroke(1.dp, Color.Green) else BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                            onClick = { userCardGalleryViewViewModel.setExchangeCard(cards[item]) },
                                        ){
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                AsyncImage(
                                                    model = cards[item].frontcard,
                                                    contentDescription = "Image",
                                                )
                                                Text(cards[item].name)
                                                Text(cards[item].rarity)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = state.psa?.toString() ?: "",
                                onValueChange = { userCardGalleryViewViewModel.onPSAChange(it) },
                                label = { Text("PSA") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.MonetizationOn,
                                        contentDescription = null
                                    )
                                },
                                isError = state.psaErr != null,
                                modifier = Modifier.fillMaxWidth()
                            )
                            state.psaErr?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            FlowRow() {
                                TextButton(
                                    onClick = { showExchangeDialog = false }
                                ) {
                                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                                }
                                TextButton(
                                    onClick = onExchange
                                ) {
                                    Text("Intercambiar", color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
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