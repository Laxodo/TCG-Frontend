package tcg.frontend.ui.administracion.generation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.generation.crear.CreateGenerationUseCase
import tcg.frontend.aplicacion.generation.listar.ListGenerationUseCase
import tcg.frontend.dominio.Generation
import tcg.frontend.infraestructura.entities.generation.CreateGenerationRequest

data class GenerationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class GenerationViewModel(
    private val listGenerationUseCase: ListGenerationUseCase,
    private val createGenerationUseCase: CreateGenerationUseCase
): ViewModel() {
    private val _items = MutableStateFlow<List<Generation>>(emptyList())
    val items: StateFlow<List<Generation>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<Generation?>(null)
    val selected: StateFlow<Generation?> = _selected.asStateFlow()

    private val _state = MutableStateFlow(GenerationState())
    val state: StateFlow<GenerationState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedGeneration(item: Generation?) {
        _selected.value = item
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            listGenerationUseCase().onSuccess { generations ->
                _items.value = generations
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _state.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    fun createGeneration(name: String, year: Int) {
        viewModelScope.launch {
            createGenerationUseCase(
                CreateGenerationRequest(
                    name = name,
                    year = year
                )
            ).onSuccess {
                refresh()
            }.onFailure {
                _state.update {
                    it.copy(errorMessage = it.errorMessage)
                }
            }
        }
    }
}