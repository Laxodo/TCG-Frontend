package tcg.frontend.ui.administracion.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.delete.DeleteUserCommand
import tcg.frontend.aplicacion.delete.DeleteUserUseCase
import tcg.frontend.aplicacion.usuarios.listar.ListUsersUseCase
import tcg.frontend.dominio.User
import kotlin.collections.mutableListOf

data class UserState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class UserViewModel(
    private val listUsersUseCase: ListUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {


    private val _items = MutableStateFlow<MutableList<User>>(mutableListOf())
    val items: StateFlow<List<User>> = _items.asStateFlow()
    private val _selected = MutableStateFlow<User?>(null)
    val selected = _selected.asStateFlow()

    var editMode: Boolean = false

    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedUser(item: User?) {
        _selected.value = item
    }

    fun deleteUser(item: User){
        val command = DeleteUserCommand(
            id = item.id
        )
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            deleteUserUseCase.invoke(command)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
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
            listUsersUseCase.invoke()
                .onSuccess { users ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value.addAll(users)
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}