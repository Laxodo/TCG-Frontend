package tcg.frontend.ui.administracion.expansion.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpansionForm(
    expansionFormViewModel: ExpansionFormViewModel,
    onClose: () -> Unit,
    onConfirm: (ExpansionFormState) -> Unit
) {
    val state by expansionFormViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { expansionFormViewModel.onNameChange(it) },
            label = { Text("Nombre") }
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.year,
            onValueChange = { expansionFormViewModel.onYearChange(it) },
            label = { Text("Año") }
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.price,
            onValueChange = { expansionFormViewModel.onPriceChange(it) },
            label = { Text("Precio") }
        )
        Spacer(Modifier.height(24.dp))
        Row {
            Button(
                onClick = {
                    if (
                        expansionFormViewModel.validate()
                    ) {
                        onConfirm(state)
                    }
                }
            ) {
                Icon(
                    Icons.Default.Save,
                    null
                )
            }
            Spacer(Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onClose
            ) {
                Icon(
                    Icons.Default.Close,
                    null
                )
            }
        }
    }
}