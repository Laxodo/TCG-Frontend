package tcg.frontend.ui.administracion.users.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tcg.frontend.dominio.User

class UserFormViewModel(
    private val item: User?
): ViewModel() {
    private val _uiState = MutableStateFlow(UserFormState(
        name = item?.name ?: "",
        username = item?.username ?: "",
        email = item?.email ?: "",
        money = item?.money?.toString() ?: "0",
        openBoosted = item?.openBoosted?.toString() ?: "0",
        exchanges = item?.exchanges?.toString() ?: "0",
        isAdmin = item?.isAdmin ?: false
    ))

    val uiState: StateFlow<UserFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if(item == null){
            state.nameError == null &&
                    state.usernameError == null &&
                    state.emailError == null &&
                    state.moneyError == null &&
                    state.openBoostedError == null &&
                    state.exchangesError == null &&
                    state.name.isNotBlank() &&
                    state.username.isNotBlank() &&
                    state.email.isNotBlank()
        }else{
            state.nameError == null &&
                    state.usernameError == null &&
                    state.emailError == null &&
                    state.moneyError == null &&
                    state.openBoostedError == null &&
                    state.exchangesError == null &&
                    state.name.isNotBlank() &&
                    state.username.isNotBlank() &&
                    state.email.isNotBlank()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onNameChange(v: String){
        _uiState.value = _uiState.value.copy(name = v, nameError = validateName(v))
    }

    fun onUsernameChange(v: String){
        _uiState.value = _uiState.value.copy(username = v, usernameError = validateUsername(v))
    }

    fun onEmailChange(v: String){
        _uiState.value = _uiState.value.copy(email = v, emailError = validateEmail(v))
    }

    fun onMoneyChange(v: String){
        _uiState.value = _uiState.value.copy(money = v, moneyError = validateMoney(v))
    }

    fun onOpenBoostedChange(v: String){
        _uiState.value = _uiState.value.copy(openBoosted = v, openBoostedError = validateOpenBoosted(v))
    }

    fun onExchangesChange(v: String){
        _uiState.value = _uiState.value.copy(exchanges = v, exchangesError = validateExchanges(v))
    }

    fun onAdmin(v: Boolean){
        _uiState.value = _uiState.value.copy(isAdmin = v)
    }

    fun clear(){
        _uiState.value = UserFormState(
            name = item?.name ?: "",
            username = item?.username ?: "",
            email = item?.email ?: "",
            money = item?.money?.toString() ?: "0",
            openBoosted = item?.openBoosted?.toString() ?: "0",
            exchanges = item?.exchanges?.toString() ?: "0",
            isAdmin = item?.isAdmin ?: false
        )
    }

    fun validateName(s: String): String?{
        if(s.isBlank()) return "El nombre no puede estar en blanco"
        return null
    }

    fun validateUsername(s: String): String?{
        if(s.isBlank()) return "El nombre de usuario no puede estar en blanco"
        return null
    }

    fun validateEmail(s: String): String?{
        if(s.isBlank()) return "El correo no puede estar en blanco"
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (!emailRegex.matches(s)) return "El correo no tiene un formato valido"
        return null
    }

    fun validateMoney(s: String): String?{
        if(s.isBlank()) return "EL dinero no puede estar vacio"
        if(s.toFloatOrNull() == null) return "El formato no es valido"
        return null
    }

    fun validateOpenBoosted(s: String): String?{
        if(s.isBlank()) return "EL numero de sobres no puede estar vacio"
        if(s.toIntOrNull() == null) return "El formato no es valido"
        if (s.toInt() < 0) return "El numero de sobres abiertos no puede ser menor que 0"
        return null
    }

    fun validateExchanges(s: String): String?{
        if(s.isBlank()) return "El numero de intercambios no puede estar vacio"
        if(s.toIntOrNull() == null) return "El formato no es valido"
        if (s.toInt() < 0) return "El numero de intercambios no puede ser menor que 0"
        return null
    }

    fun validateAll(): Boolean{
        val s = _uiState.value
        val nameError = validateName(s.name)
        val usernameError = validateUsername(s.username)
        val emailError = validateEmail(s.email)
        val moneyError = validateMoney(s.money)
        val openBoostedError = validateOpenBoosted(s.openBoosted)
        val exchangesError = validateExchanges(s.exchanges)

        val newState = s.copy(
            nameError = nameError,
            usernameError = usernameError,
            emailError = emailError,
            moneyError = moneyError,
            openBoostedError = openBoostedError,
            exchangesError = exchangesError
        )
        _uiState.value = newState
        return listOf(nameError, usernameError, emailError, moneyError, openBoostedError, exchangesError).all{it == null}
    }

    fun submit(
        onSuccess: (UserFormState) -> Unit,
        onFailure: ((UserFormState) -> Unit)? = null
    ){
        val ok = validateAll()
        if(ok){
            onSuccess(_uiState.value)
        }else{
            onFailure?.invoke(_uiState.value)
        }
    }
}