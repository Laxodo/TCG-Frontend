package tcg.frontend.ui.administracion.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.users.delete.DeleteUserCommand
import tcg.frontend.aplicacion.users.delete.DeleteUserUseCase
import tcg.frontend.aplicacion.users.listar.ListUsersUseCase
import tcg.frontend.aplicacion.users.update.UpdateUserCommand
import tcg.frontend.aplicacion.users.update.UpdateUserUseCase
import tcg.frontend.dominio.User
import tcg.frontend.ui.administracion.users.form.UserFormState
import kotlin.collections.mutableListOf

data class UserState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class UserViewModel(
    private val listUsersUseCase: ListUsersUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
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

    fun updateUser(state: UserFormState) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            updateUserUseCase.invoke(UpdateUserCommand(
                id = _selected.value?.id ?: -999,
                name = if (state.name == _selected.value?.name) null else state.name,
                username = if (state.username == _selected.value?.username) null else state.username,
                email = if (state.email == _selected.value?.email) null else state.email,
                money = if (state.money.toFloatOrNull() == _selected.value?.money) null else state.money.toFloatOrNull(),
                openBoosted = if (state.openBoosted.toIntOrNull() == _selected.value?.openBoosted) null else state.openBoosted.toIntOrNull(),
                exchanges = if (state.exchanges.toIntOrNull() == _selected.value?.exchanges) null else state.exchanges.toIntOrNull(),
                isAdmin = if (state.isAdmin == _selected.value?.isAdmin) null else state.isAdmin
            ))
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