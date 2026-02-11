package tcg.frontend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tcg.frontend.application.user.RegisterUseCase
import tcg.frontend.application.user.command.RegisterCommand
import tcg.frontend.application.user.dto.UserDTO
import tcg.frontend.modelo.IUserRepository
import tcg.frontend.ui.form.UserFormState

class RegisterViewModel(
    private val UserRepository: IUserRepository
) : ViewModel() {
    private val registerUseCase: RegisterUseCase

    private val _items = MutableStateFlow<MutableList<UserDTO>>(mutableListOf())
    val items: StateFlow<List<UserDTO>> = _items.asStateFlow()
    private val _selected = MutableStateFlow<UserDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        registerUseCase = RegisterUseCase(UserRepository)
        viewModelScope.launch {
            var items = mutableListOf<UserDTO>()
            _items.value.clear()
            _items.value.addAll(items)
        }
    }

    fun add(formstate: UserFormState) {
        val command = RegisterCommand(
            formstate.name,
            formstate.username,
            formstate.password,
            formstate.email
        )
        viewModelScope.launch {
            try {
                val user = RegisterUseCase.invoke(command)
                _items.value = (_items.value + user) as MutableList<UserDTO>
            } catch (e: Exception) {
                throw e
            }
        }
    }

    /*fun update(formState: UserFormState) {
        val command = UpdateUserCommand()
    }

    fun save(item: UserFormState) {
        if(_selected.value == null)
            this.add(item)
        else {
            this.update(item)
        }
    }
    */
}