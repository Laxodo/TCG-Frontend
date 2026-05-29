package tcg.frontend.ui.usuario

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.aplicacion.usuarios.getuser.GetUserCommand
import tcg.frontend.aplicacion.usuarios.getuser.GetUserUseCase
import tcg.frontend.dominio.User
import tcg.frontend.ui.MainViewModel

data class ItemOption(
    val icon: ImageVector,
    val action:()->Unit,
    val name:String
)

class UserMainViewModel(
    private val mainViewModel: MainViewModel,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {
    private val _options= MutableStateFlow<List<ItemOption>>(emptyList())
    val options = _options

    private val _user= MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        refreshUser()
    }

    fun refreshUser() {
        viewModelScope.launch {
            getUserUseCase.invoke(GetUserCommand(mainViewModel.currentUserState.value?.id ?: -999))
                .onSuccess {
                    _user.value = it
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