package tcg.frontend.application.user.command

data class RegisterCommand(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)