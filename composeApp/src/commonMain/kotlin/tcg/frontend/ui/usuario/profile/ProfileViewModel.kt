package tcg.frontend.ui.usuario.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.users.update.UpdateUserCommand
import tcg.frontend.aplicacion.users.update.UpdateUserUseCase
import tcg.frontend.ui.administracion.users.form.UserFormState
import tcg.frontend.ui.usuario.UserMainViewModel


data class ProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class ProfileViewModel(
    private val updateUserUseCase: UpdateUserUseCase,
    private val userMainViewModel: UserMainViewModel
): ViewModel() {

    val user =  userMainViewModel.user

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun updateUser(state: UserFormState) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            updateUserUseCase.invoke(UpdateUserCommand(
                id = user.value?.id ?: -999,
                name = if (state.name == user.value?.name) null else state.name,
                username = if (state.username == user.value?.username) null else state.username,
                email = if (state.email == user.value?.email) null else state.email,
                money = if (state.money.toFloatOrNull() == user.value?.money) null else state.money.toFloatOrNull(),
                openBoosted = if (state.openBoosted.toIntOrNull() == user.value?.openBoosted) null else state.openBoosted.toIntOrNull(),
                exchanges = if (state.exchanges.toIntOrNull() == user.value?.exchanges) null else state.exchanges.toIntOrNull(),
                isAdmin = if (state.isAdmin == user.value?.isAdmin) null else state.isAdmin
            ))
                .onSuccess {
                    userMainViewModel.refreshUser()
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}