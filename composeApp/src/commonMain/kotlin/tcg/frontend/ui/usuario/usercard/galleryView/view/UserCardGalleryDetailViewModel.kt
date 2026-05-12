package tcg.frontend.ui.usuario.usercard.galleryView.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO

class UserCardGalleryDetailViewModel(
    item: UserCardDTO?
): ViewModel() {
    private val _usercard = MutableStateFlow(item)
    val userCard = _usercard.asStateFlow()

    fun setSelectedUserCard(item: UserCardDTO){
        _usercard.value = item
    }
}