package tcg.frontend.ui.administracion.card

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.card.create.CreateCardUseCase
import tcg.frontend.aplicacion.card.createBatch.CreateCardsBatchUseCase
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionCommand
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionUseCase
import tcg.frontend.dominio.Card
import tcg.frontend.infraestructura.entities.card.CreateCardRequest
import tcg.frontend.ui.register.RegisterState

data class CardState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CardViewModel(
    private val listCardExpansionUseCase: ListCardExpansionUseCase,
    private val createCardUseCase: CreateCardUseCase,
    private val createCardsBatchUseCase: CreateCardsBatchUseCase
): ViewModel() {
    private val _items = MutableStateFlow<List<Card>>(emptyList())
    val items = _items.asStateFlow()

    private val _expansionId = MutableStateFlow<Int?>(null)
    val expansionId = _expansionId.asStateFlow()

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
                ListCardExpansionCommand(
                    idExpansion = expansionId
                )
            ).onSuccess { cards ->
                _items.value = cards
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun createCard(request: CreateCardRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            createCardUseCase(request)
                .onSuccess {
                    refresh()
                    _state.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun createBatch(request: List<CreateCardRequest>) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            createCardsBatchUseCase(
                request
            ).onSuccess {
                refresh()
                _state.update { it.copy(isLoading = false) }
            }.onFailure {
                _state.update { it.copy(errorMessage = it.errorMessage) }
            }
        }
    }
}