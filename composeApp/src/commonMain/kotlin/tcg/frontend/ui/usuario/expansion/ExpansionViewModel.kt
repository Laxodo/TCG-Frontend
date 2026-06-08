package tcg.frontend.ui.usuario.expansion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.expansion.list.ListExpansionUseCase
import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionCommand
import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionUseCase
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.Generation

data class ExpansionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExpansionViewModel(
    private val listGenerationExpansionUseCase: ListGenerationExpansionUseCase,
    private val listExpansionUseCase: ListExpansionUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Expansion>>(mutableListOf())
    val items: StateFlow<List<Expansion>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<Expansion?>(null)
    val selected = _selected.asStateFlow()

    private val _state = MutableStateFlow(ExpansionState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedExpansion(item: Expansion?) {
        _selected.value = item
    }

    fun getGenerationExpansions(id: Int){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listGenerationExpansionUseCase.invoke(ListGenerationExpansionCommand(id))
                .onSuccess { expansions ->
                    _items.value = expansions as MutableList
                    _state.update { it.copy(isLoading = false) }
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