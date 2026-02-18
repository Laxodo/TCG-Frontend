package tcg.frontend.ui.form

data class RegisterState (
    // Campos Formulario
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val money: Float = 0f,


    // UI States
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val isValid: Boolean = false,

    //Errores específicos de campo
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,

    //Error global (No internet)
    val errorMessage: String? = null
)