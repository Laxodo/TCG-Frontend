package tcg.frontend.ui.usuario.expansion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationCommand
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationUseCase
import tcg.frontend.dominio.Expansion

data class ExpansionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExpansionViewModel(
    private val listExpansionGenerationUseCase: ListExpansionGenerationUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Expansion>>(mutableListOf())
    val items: StateFlow<List<Expansion>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<Expansion?>(null)
    val selected = _selected.asStateFlow()

    private var generationId: Int? = null

    private val _state = MutableStateFlow(ExpansionState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun loadGeneration(id: Int) {
        generationId = id
        refresh()
    }

    fun setSelectedExpansion(item: Expansion?) {
        _selected.value = item
    }

    fun refresh() {
        val id = generationId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listExpansionGenerationUseCase(
                ListExpansionGenerationCommand(
                    idGeneration = id
                )
            ).onSuccess { expansions ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value.addAll(expansions)
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            errorMessage = error.message,
                            isLoading = false
                        )
                    }
                }
        }
    }
}