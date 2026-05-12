package tcg.frontend.ui.usuario.usercard.collectionView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionCommand
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionUseCase
import tcg.frontend.aplicacion.usercard.listCollection.UserCardCollectionDTO

data class UserCardCollectionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class UserCardCollectionViewModel(
    private val listUserCardCollectionUseCase: ListUserCardCollectionUseCase,
    private val idExpansion: Int,
    private val idUser: Int
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<UserCardCollectionDTO>>(mutableListOf())
    val items: StateFlow<List<UserCardCollectionDTO>> = _items.asStateFlow()

    private val _state = MutableStateFlow(UserCardCollectionState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listUserCardCollectionUseCase.invoke(ListUserCardCollectionCommand(idUser, idExpansion))
                .onSuccess { usercards ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value.addAll(usercards)
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}