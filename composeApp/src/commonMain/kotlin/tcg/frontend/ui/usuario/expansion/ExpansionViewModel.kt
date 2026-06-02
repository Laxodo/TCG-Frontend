package tcg.frontend.ui.usuario.expansion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.expansion.list.ListExpansionUseCase
import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionCommands
import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionUseCase
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.Expansion

data class ExpansionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExpansionViewModel(
    private val listExpansionUseCase: ListExpansionUseCase,
    private val listCardsExpansionUseCase: ListCardsExpansionUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Expansion>>(mutableListOf())
    val items: StateFlow<List<Expansion>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<Expansion?>(null)
    val selected = _selected.asStateFlow()

    private val _cards = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val cards = _cards.asStateFlow()

    private val _state = MutableStateFlow(ExpansionState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedExpansion(item: Expansion?) {
        _selected.value = item
    }

    fun getExpansionCards() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listCardsExpansionUseCase.invoke(ListCardsExpansionCommands(_selected.value?.id!!))
                .onSuccess { cards ->
                    _state.update { it.copy(isLoading = false) }
                    _cards.value.clear()
                    _cards.value = cards as MutableList
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listExpansionUseCase.invoke()
                .onSuccess { expansions ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value.addAll(expansions)
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}