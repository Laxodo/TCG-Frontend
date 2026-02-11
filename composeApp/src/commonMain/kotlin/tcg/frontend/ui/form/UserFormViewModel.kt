package tcg.frontend.ui.form

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
import tcg.frontend.application.user.dto.UserDTO

class UserFormViewModel(
    private val item: UserDTO?,
    onSuccess: (UserFormState) -> Unit
): ViewModel() {
    private val _uiState = MutableStateFlow(UserFormState(
        name = item?.name ?: "",
        username = item?.username ?: "",
        email = item?.email ?: "",
        password = ""
    ))
    val uiState: StateFlow<UserFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if(item == null)
            state.usernameError == null &&
            state.emailError == null &&
            state.passwordError == null &&

            !state.name.isBlank() &&
            !state.email.isBlank() &&
            state.password.isNotBlank()
        else {
            state.usernameError == null
            state.emailError == null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onUsernameChange(v: String) {
        _uiState.value = _uiState.value.copy(username = v, usernameError = validateUsername(v))
    }

    fun onEmailChange(v: String) {
        _uiState.value = _uiState.value.copy(email = v, emailError = validateEmail(v))
    }

    fun onPasswordChange(v: String) {
        _uiState.value = _uiState.value.copy(password = v, passwordError = validatePassword(v))
    }

    fun clear() {
        _uiState.value = UserFormState()
    }

    private fun validateUsername(username: String): String? {
        if (username.isBlank()) return "El nombre es obligatorio"
        if (username.length < 2) return "El nombre es muy corto"
        return null
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "El correo es obligatorio"
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (!emailRegex.matches(email)) "Correo inválido" else null
    }

    private fun validatePassword(pw: String): String? {
        if (pw.isBlank()) return "La contraseña es obligatoria"
        if (pw.length < 8) return "La contraseña debe tener al menos 8 caracteres"
        val hasDigit = pw.any { it.isDigit() }
        val hasUpper = pw.any { it.isUpperCase() }
        if (!hasDigit || !hasUpper) return "La contraseña debe incluir mayúscula y número"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val usernameErr = validateUsername(s.username)
        val pwErr = if(item==null) validatePassword(s.password) else null
        val emailErr = validateEmail(s.email)
        val newState = s.copy(
            usernameError = usernameErr,
            passwordError = pwErr,
            emailError = emailErr,

            submitted = true
        )
        _uiState.value = newState
        return listOf(usernameErr, pwErr).all { it == null }
    }

    fun submit(
        onSuccess: (UserFormState) -> Unit,
        onFailure: ((UserFormState) -> Unit)? = null
    ) {
        //se ejecuta en una corrutina, evitando que se bloque la interfaz gráficas
        viewModelScope.launch {
            val ok = validateAll()
            if (ok) {
                onSuccess(_uiState.value)
            } else {
                onFailure?.invoke(_uiState.value)
            }
        }
    }
}