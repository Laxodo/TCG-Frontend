package tcg.frontend.ui.usuario

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.UserManager
import tcg.frontend.aplicacion.users.getuser.GetUserCommand
import tcg.frontend.aplicacion.users.getuser.GetUserUseCase
import tcg.frontend.dominio.User
import tcg.frontend.ui.MainViewModel

data class ItemOption(
    val icon: ImageVector,
    val action:()->Unit,
    val name:String
)

class UserMainViewModel(
    private val mainViewModel: MainViewModel,
    private val getUserUseCase: GetUserUseCase,
    private val userManager: UserManager
): ViewModel() {
    private val _options= MutableStateFlow<List<ItemOption>>(emptyList())
    val options = _options

    val user = userManager.user

    init {
        refreshUser()
    }

    fun refreshUser() {
        viewModelScope.launch {
            getUserUseCase.invoke(GetUserCommand(mainViewModel.currentUserState.value?.id ?: -999))
                .onSuccess {
                    userManager.setUser(it)
                }
                .onFailure {
                    mainViewModel.logout()
                }
        }
    }
    fun setOptions(options:List<ItemOption>){
        _options.value = options.toList()
    }
}