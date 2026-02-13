package tcg.frontend.ui.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameErr: String? = null,
    val passwordErr: String? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isValid:Boolean = false,
    val errorMessage: String? = null,
)