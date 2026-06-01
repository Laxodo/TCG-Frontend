package tcg.frontend.ui.administracion.generation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tcg.frontend.dominio.Generation

@Composable
fun Generation(
    generationViewModel: GenerationViewModel,
    onViewForm: (Generation) -> Unit,
    onBack: () -> Unit
) {
    val items by generationViewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val state by generationViewModel.state.collectAsState()
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.name.contains(searchText, ignoreCase = true)
        } else {
            true
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                if (state.errorMessage != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Undo,
                                contentDescription = "Back",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
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
                                    contentDescription = "Search..."
                                )
                            },
                            modifier = Modifier.weight(1f).padding(8.dp)
                        )
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(
                            minSize = 512.dp
                        )
                    ) {
                        items(filteredItems.size) { item ->
                            GenerationForm(
                                filteredItems[item],
                                {
                                    onViewForm(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}