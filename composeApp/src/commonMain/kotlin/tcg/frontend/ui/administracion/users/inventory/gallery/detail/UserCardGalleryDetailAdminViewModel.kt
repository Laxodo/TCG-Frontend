package tcg.frontend.ui.administracion.users.inventory.gallery.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO

data class UserCardGalleryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
data class UserCardDetailState(
    val price: String = "",
    val psa: String? = null,
    val psaErr: String? = null,
    val priceErr: String? = null
)
class UserCardGalleryDetailAdminViewModel(
    item: UserCardDTO?
): ViewModel() {
    private val _usercard = MutableStateFlow(item)
    val userCard = _usercard.asStateFlow()

    private val _state = MutableStateFlow(UserCardGalleryState())
    val state: StateFlow<UserCardGalleryState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow(UserCardDetailState(
        price = item?.userCard?.price?.toString() ?: "",
        priceErr = null
    ))
    val uiState: StateFlow<UserCardDetailState> = _uiState.asStateFlow()

    fun setSelectedUserCard(item: UserCardDTO){
        _usercard.value = item
    }

}