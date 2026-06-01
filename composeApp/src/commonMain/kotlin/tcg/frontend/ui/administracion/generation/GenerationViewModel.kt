package tcg.frontend.ui.administracion.generation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.generation.listar.ListGenerationUseCase
import tcg.frontend.dominio.Generation

data class GenerationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class GenerationViewModel(
    private val listGenerationUseCase: ListGenerationUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Generation>>(mutableListOf())
    val items: StateFlow<List<Generation>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<Generation?>(null)
    val selected = _selected.asStateFlow()

    private val _state = MutableStateFlow(GenerationState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedGeneration(item: Generation?) {
        _selected.value = item
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listGenerationUseCase.invoke()
                .onSuccess { generations ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value.addAll(generations)
                }.onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}