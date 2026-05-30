package tcg.frontend.ui.usuario.usercard.galleryView.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellUseCase
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO
import tcg.frontend.ui.usuario.UserMainViewModel

data class UserCardGalleryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class UserCardGalleryDetailViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val quickSellUseCase: QuickSellUseCase,
    item: UserCardDTO?
): ViewModel() {
    private val _usercard = MutableStateFlow(item)
    val userCard = _usercard.asStateFlow()

    private val _state = MutableStateFlow(UserCardGalleryState())
    val state: StateFlow<UserCardGalleryState> = _state.asStateFlow()

    fun setSelectedUserCard(item: UserCardDTO){
        _usercard.value = item
    }

    fun quickSell() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            quickSellUseCase.invoke(QuickSellCommand(listOf(_usercard.value?.userCard?.id ?: -999)))
                .onSuccess { moneyGained ->
                    _state.update { it.copy(isLoading = false) }
                    userMainViewModel.refreshUser()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

}