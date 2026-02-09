package tcg.frontend.ui.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tcg.frontend.application.user.dto.UserDTO

class UserFormViewModel(
    private val item: UserDTO?,
    onSuccess: (UserDTO) -> Unit
): ViewModel() {
    private val _uiState = MutableStateFlow(UserFormState(
        nombre = item?.name ?: "",
        username = item?.username ?: "",
        email = item?.email ?: "",
        password = ""
    ))
    val uiState: StateFlow<UserFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if(item == null)
            state.nombreError == null &&
            state.emailError == null &&
            state.password == null &&

            !state.nombre.isBlack() &&
            !state.email.isBlack() &&
            state.password.isNotBlank()
        else {
            state.nombreError == null
            state.emailError == null
        }
    }
}