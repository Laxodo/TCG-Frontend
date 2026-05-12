package tcg.frontend.ui.usuario.usercard.collectionView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHorizontalCircle
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
import androidx.compose.ui.unit.dp

@Composable
fun UserCardCollection(
    userCardCollectionViewModel: UserCardCollectionViewModel,
    onBack: () -> Unit,
    onChangeView: () -> Unit
){
    val items by userCardCollectionViewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("")}
    val state by userCardCollectionViewModel.state.collectAsState()
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.cardName.contains(searchText, ignoreCase = true)
        }else{
            true
        }
    }
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
                    .padding(16.dp)
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
                        OutlinedButton(
                            onClick = onChangeView,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHorizontalCircle,
                                contentDescription = "Change View",
                                modifier = Modifier
                                    .size(ButtonDefaults.IconSize)
                            )
                        }
                        OutlinedButton(
                            onClick = { userCardCollectionViewModel.refresh() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChangeCircle,
                                contentDescription = "Recargar",
                                modifier = Modifier
                                    .size(ButtonDefaults.IconSize)
                            )
                        }
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(
                            minSize = 512.dp
                        )
                    ) {
                        items(filteredItems.size) { item ->
                            UserCardCollectionCard(
                                filteredItems[item]
                            )
                        }
                    }
                }
            }
        }
    }
}