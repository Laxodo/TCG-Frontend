package tcg.frontend.ui.usuario.wiki.generation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.generation.list.ListGenerationUseCase
import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionCommand
import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionUseCase
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.Generation

data class GenerationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class GenerationViewModel(
    private val listGenerationUseCase: ListGenerationUseCase
): ViewModel() {
    private val _items = MutableStateFlow<List<Generation>>(listOf())
    val items = _items.asStateFlow()

    private val _item = MutableStateFlow<Generation?>(null)
    val item = _item.asStateFlow()

    private val _state = MutableStateFlow(GenerationState())
    val state: StateFlow<GenerationState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedGeneration(g: Generation){
        _item.value = g
    }

    fun refresh(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listGenerationUseCase.invoke()
                .onSuccess { generations ->
                    _items.value = generations as MutableList
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}