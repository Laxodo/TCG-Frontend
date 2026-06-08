package tcg.frontend.ui.usuario.wiki.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionCommands
import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionUseCase
import tcg.frontend.dominio.Card

data class CardState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class CardViewModel(
    private val listCardsExpansionUseCase: ListCardsExpansionUseCase
): ViewModel() {

    private val _items = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val items = _items.asStateFlow()

    private val _selected = MutableStateFlow<Card?>(null)
    val selected = _selected.asStateFlow()

    private val _state = MutableStateFlow(CardState())
    val state = _state.asStateFlow()

    fun setSelectedCard(v: Card){
        _selected.value = v
    }

    fun getExpansionCards(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listCardsExpansionUseCase.invoke(ListCardsExpansionCommands(id))
                .onSuccess { cards ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value = cards as MutableList
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}