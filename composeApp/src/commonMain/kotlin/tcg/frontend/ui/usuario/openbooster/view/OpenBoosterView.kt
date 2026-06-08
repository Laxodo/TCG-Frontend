package tcg.frontend.ui.usuario.openbooster.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.expansion.ExpansionViewModel
import tcg.frontend.ui.usuario.openbooster.OpenBoosterViewModel

@Composable
fun OpenBoosterView(
    userMainViewModel: UserMainViewModel,
    expansionViewModel: ExpansionViewModel,
    openBoosterViewModel: OpenBoosterViewModel,
    onOpenBooster: () -> Unit,
    onBack: () -> Unit
){
    val items by openBoosterViewModel.items.collectAsState()
    val state by openBoosterViewModel.state.collectAsState()
    val user by userMainViewModel.user.collectAsState()
    val expansion by expansionViewModel.selected.collectAsState()
    val enoughtMoney = ((user?.money ?: 0f) >= (expansion?.price ?: 0f))
    val filteredItems = items.filter { true }
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading){
            CircularProgressIndicator()
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.errorMessage != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                } else {
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
                                imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(ButtonDefaults.IconSize)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxHeight(0.95f)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(
                                minSize = 512.dp
                            )
                        ) {
                            items(filteredItems.size) { item ->
                                OpenBoosterViewCard(
                                    openBoosterViewModel,
                                    filteredItems[item]
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {onOpenBooster()},
                        enabled = enoughtMoney
                    ){
                        Text(
                            text = "Abrir otro sobre",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
    }
}