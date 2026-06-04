package tcg.frontend.ui.administracion.card

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.card.create.CreateCardUseCase
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionCommand
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionUseCase
import tcg.frontend.dominio.Card
import tcg.frontend.infraestructura.entities.card.CreateCardRequest

data class CardState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CardViewModel(
    private val listCardExpansionUseCase: ListCardExpansionUseCase,
    private val createCardUseCase: CreateCardUseCase
): ViewModel() {
    private val _items = MutableStateFlow<List<Card>>(emptyList())
    val items = _items.asStateFlow()

    private val _expansionId = MutableStateFlow<Int?>(null)

    private val _state = MutableStateFlow(CardState())
    val state = _state.asStateFlow()

    fun setExpansion(expansionId: Int) {
        _expansionId.value = expansionId
        refresh()
    }

    fun refresh() {
        val expansionId = _expansionId.value ?: return

        viewModelScope.launch {
            listCardExpansionUseCase(
                ListCardExpansionCommand(expansionId)
            ).onSuccess { Card ->
                _items.value = Card
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun createCard(request: CreateCardRequest) {
        viewModelScope.launch {
            createCardUseCase(request).onSuccess {
                refresh()
                _state.update { it.copy(isLoading = false) }
            }.onFailure {
                _state.update { it.copy(errorMessage = it.errorMessage) }
            }
        }
    }
}