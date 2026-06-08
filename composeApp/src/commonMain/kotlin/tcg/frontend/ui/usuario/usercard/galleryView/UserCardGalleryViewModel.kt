package tcg.frontend.ui.usuario.usercard.galleryView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellUseCase
import tcg.frontend.aplicacion.usercard.listar.ListUserCardCommand
import tcg.frontend.aplicacion.usercard.listar.ListUserCardUseCase
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO
import tcg.frontend.ui.usuario.UserMainViewModel
import kotlin.collections.MutableList

data class UserCardState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class UserCardGalleryViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val listUserCardUseCase: ListUserCardUseCase,
    private val quickSellUseCase: QuickSellUseCase,
    private val idExpansion: Int,
    private val idUser: Int
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<UserCardDTO>>(mutableListOf())
    val items: StateFlow<List<UserCardDTO>> = _items.asStateFlow()

    private val _selectedItems = MutableStateFlow<MutableList<UserCardDTO>>(mutableListOf())
    val selectedItems: StateFlow<List<UserCardDTO>> = _selectedItems.asStateFlow()

    private val _state = MutableStateFlow(UserCardState())
    val state: StateFlow<UserCardState> = _state.asStateFlow()

    private val _sellMode = MutableStateFlow(false)
    val sellMode: StateFlow<Boolean> = _sellMode.asStateFlow()


    init {
        refresh()
    }

    fun setSelectedUserCard(item: UserCardDTO){
        if(_selectedItems.value.contains(item)){
            _selectedItems.value = _selectedItems.value.filterNot { it.userCard.id == item.userCard.id }.toMutableList()
        }else{
            _selectedItems.value = (_selectedItems.value + item) as MutableList<UserCardDTO>
        }
    }

    fun setSelectedUsersCards(items: List<UserCardDTO>){
        if(_selectedItems.value.containsAll(items)){
            _selectedItems.value = mutableListOf()
        }else{
            _selectedItems.value = (_selectedItems.value + items) as MutableList<UserCardDTO>
        }
    }

    fun changeSellMode(){
        _sellMode.value = !_sellMode.value
        if (!_sellMode.value){
            _selectedItems.value = mutableListOf()
        }
    }

    fun quickSell() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            quickSellUseCase.invoke(QuickSellCommand(_selectedItems.value.map { value -> value.userCard.id }))
                .onSuccess { moneyGained ->
                    _state.update { it.copy(isLoading = false) }
                    _selectedItems.value.clear()
                    userMainViewModel.refreshUser()
                    refresh()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

        fun refresh(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
                listUserCardUseCase.invoke(ListUserCardCommand(idUser, idExpansion, -1, 0)) // Temporaly without pagination until i research how to fire the trigger
                .onSuccess { usercards ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value = usercards as MutableList<UserCardDTO>
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}