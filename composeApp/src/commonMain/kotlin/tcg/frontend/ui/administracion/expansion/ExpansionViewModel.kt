package tcg.frontend.ui.administracion.expansion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.expansion.crear.CreateExpansionUseCase
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationCommand
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationUseCase
import tcg.frontend.dominio.Expansion
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest

data class ExpansionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExpansionViewModel (
    private val listExpansionGenerationUseCase: ListExpansionGenerationUseCase,
    private val createExpansionUseCase: CreateExpansionUseCase
): ViewModel() {
    private val _items = MutableStateFlow<List<Expansion>>(emptyList())
    val items = _items.asStateFlow()

    private val _selected = MutableStateFlow<Expansion?>(null)
    val selected = _selected.asStateFlow()

    private val _generationId = MutableStateFlow<Int?>(null)
    val generationId = _generationId.asStateFlow()

    private val _state = MutableStateFlow(ExpansionState())
    val state = _state.asStateFlow()

    fun setSelectedExpansion(item: Expansion?) {
        _selected.value = item
    }

    fun setGeneration(generationId: Int) {
        _generationId.value = generationId
        refresh()
    }

    fun refresh() {
        val generationId = _generationId.value ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listExpansionGenerationUseCase(
                ListExpansionGenerationCommand(
                    idGeneration = generationId
                )
            ).onSuccess { expansions ->
                _items.value = expansions
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _state.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    fun createExpansion(request: CreateExpansionRequest) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            createExpansionUseCase(
                request
            ).onSuccess {
                refresh()
                _state.update { it.copy(isLoading = false) }
            }.onFailure {
                _state.update {
                    it.copy(errorMessage = it.errorMessage)
                }
            }
        }
    }
}