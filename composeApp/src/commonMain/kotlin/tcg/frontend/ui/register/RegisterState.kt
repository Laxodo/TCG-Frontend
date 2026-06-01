package tcg.frontend.ui.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirm_password: String = "",
    val nameErr: String? = null,
    val usenameErr: String? = null,
    val passwordErr: String? = null,
    val emailErr: String? = null,
    val confirm_passwordErr: String? = null,

    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val isValid: Boolean = false,

    val errorMessage: String? = null
)