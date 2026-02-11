package tcg.frontend.ui.form

data class UserFormState (
    val id: Int = 1,
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val money: Float = 0f,
    val address: String = "",
    val exchanges: Int = 0,
    // Errors
    val usernameError: String? = null,
    val passwordError: String? = null,
    val emailError: String? = null,
    // Control de envio
    val submitted: Boolean = false
)