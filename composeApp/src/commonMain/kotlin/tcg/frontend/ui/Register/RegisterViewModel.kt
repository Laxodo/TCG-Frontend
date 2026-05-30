package tcg.frontend.ui.Register

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.application.register.RegisterUserCase
import tcg.frontend.application.register.RegisterCommand

class RegisterViewModel(
    private val registerUserCase: RegisterUserCase
): ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()
    var isFormValid = MutableStateFlow(false)

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, usenameErr = if (username.isBlank()) "El username no puede estar vacío." else null)
        validateAll()
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailErr = if (email.contains("@")) null else "Email no válido")
        validateAll()
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordErr = if (password.isBlank()) "The password cannot be empty" else null)
        validateAll()
    }

    fun onConfirmPassword(confirm_password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirm_password =confirm_password,
                confirm_passwordErr = when {
                    confirm_password != currentState.password -> "Las 2 contraseñas no coinciden"
                    else -> null
                }
            )
        }
        validateAll()
    }

    fun validateAll() {
        val s = _uiState.value
        isFormValid.value =
            s.username.isNotBlank() &&
            s.email.isNotBlank() &&
            s.password.isNotBlank() &&
            s.confirm_password.isNotBlank() &&
            s.usenameErr == null &&
            s.emailErr == null &&
            s.passwordErr == null &&
            s.confirm_passwordErr == null
        _uiState.value = uiState.value.copy(isValid = isFormValid.value)
    }

    fun register() {
        viewModelScope.launch {
            try {
                _uiState.value = uiState.value.copy(isLoading = true)
                val registerCommand = RegisterCommand(
                    name = uiState.value.name,
                    username = uiState.value.username,
                    email = uiState.value.email,
                    password = uiState.value.password,
                    confirm_password = uiState.value.confirm_password
                )
                registerUserCase.invoke(registerCommand).onSuccess {
                    _uiState.value = _uiState.value.copy(isRegisterSuccess = true)
                    println("The user can now login")
                    _uiState.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false, isRegisterSuccess = false) }
                    val error = it.message
                    _uiState.update { it.copy(errorMessage = error) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error al registrarse: ${e.message}")
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}