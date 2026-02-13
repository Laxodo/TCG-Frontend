package tcg.frontend.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.aplicacion.login.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()
    var isFormValid =  MutableStateFlow(false)
    var id = MutableStateFlow<String?>(null)

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, usernameErr = if (username.isBlank()) "The username cannot be empty." else null)
        validateAll()
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordErr = if (password.isBlank()) "The passwrod cannit be empty." else null)
        validateAll()
    }

    fun validateAll() {
        val s = _uiState.value
        isFormValid.value = s.username.isNotBlank() &&
                s.password.isNotBlank() &&
                s.usernameErr == null &&
                s.passwordErr == null
        _uiState.value=uiState.value.copy( isValid = isFormValid.value)
    }

    fun login(){
        viewModelScope.launch {
            try {
                _uiState.value = uiState.value.copy(isLoading = true)
                val loginCommand =
                    LoginCommand(
                        username = _uiState.value.username,
                        password = _uiState.value.password
                    )
                loginUseCase.invoke(loginCommand).onSuccess{
                    _uiState.value = _uiState.value.copy(isLoginSuccess = true)
                    println("Access token: $it")
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }

                }.onFailure {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = false) }
                    val error = it.message

                    _uiState.update { it.copy(errorMessage = error) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al conectar: ${e.message}"
                    )
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}