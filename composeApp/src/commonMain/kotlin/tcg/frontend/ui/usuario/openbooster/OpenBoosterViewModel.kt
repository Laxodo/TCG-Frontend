package tcg.frontend.ui.usuario.openbooster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterCommand
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterUseCase
import tcg.frontend.dominio.Card
import tcg.frontend.ui.usuario.UserMainViewModel

data class OpenBoosterState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class OpenBoosterViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val openBoosterUseCase: OpenBoosterUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val items: StateFlow<List<Card>> = _items.asStateFlow()

    private val _state = MutableStateFlow(OpenBoosterState())
    val state = _state.asStateFlow()

    fun openBoosted(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            openBoosterUseCase.invoke(OpenBoosterCommand(id))
                .onSuccess { cards ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value = cards as MutableList<Card>
                    userMainViewModel.refreshUser()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

}