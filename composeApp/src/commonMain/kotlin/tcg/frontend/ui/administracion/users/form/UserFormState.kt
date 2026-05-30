package tcg.frontend.ui.administracion.users.form

data class UserFormState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val money: String = "",
    val openBoosted: String = "",
    val exchanges: String = "",
    val isAdmin: Boolean = false,

    val nameError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val moneyError: String? =null,
    val openBoostedError: String? =null,
    val exchangesError: String? =null,

    val submitted: Boolean = false
)