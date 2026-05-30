package tcg.frontend.ui.administracion.users.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tcg.frontend.ui.administracion.users.UserViewModel

@Composable
fun UserForm(
    userViewModel: UserViewModel,
    userFormViewModel: UserFormViewModel,
    onClose: () -> Unit,
    onConfirm: (UserFormState) -> Unit
){
    val state by userFormViewModel.uiState.collectAsState()
    val formValid by userFormViewModel.isFormValid.collectAsState()
    val enable = userViewModel.editMode

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = if (enable) {
                        "Editar perfil"
                    } else {
                        "Ver perfil"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.name,
                enabled = enable,
                onValueChange = { userFormViewModel.onNameChange(it) },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nameError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = state.username,
                enabled = enable,
                onValueChange = { userFormViewModel.onUsernameChange(it) },
                label = { Text("Nombre de usuario") },
                leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
                isError = state.usernameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.usernameError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = state.email,
                enabled = enable,
                onValueChange = { userFormViewModel.onEmailChange(it) },
                label = { Text("Correo") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.emailError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = state.money,
                enabled = enable,
                onValueChange = { userFormViewModel.onMoneyChange(it) },
                label = { Text("Dinero") },
                leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null) },
                isError = state.moneyError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.moneyError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = state.openBoosted,
                enabled = enable,
                onValueChange = { userFormViewModel.onOpenBoostedChange(it) },
                label = { Text("Sobres abiertos") },
                leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                isError = state.openBoostedError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.openBoostedError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = state.exchanges,
                enabled = enable,
                onValueChange = { userFormViewModel.onExchangesChange(it) },
                label = { Text("Intercambios") },
                leadingIcon = { Icon(Icons.Default.SwapHoriz, contentDescription = null) },
                isError = state.exchangesError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.exchangesError?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.isAdmin,
                        enabled = enable,
                        onCheckedChange = { userFormViewModel.onAdmin(it) }
                    )
                    Text("Administrador", style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(enable) {
                    FilledTonalButton(onClick = { userFormViewModel.clear() }) {
                        Icon(Icons.Default.Autorenew, contentDescription = null)
                    }

                    Button(
                        onClick = {
                            userFormViewModel.submit(
                                onSuccess = {
                                    onConfirm(it)
                                },
                                onFailure = {}
                            )
                        },
                        enabled = formValid || enable
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                    }
                }

                FilledTonalButton(onClick = { onClose() }) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}