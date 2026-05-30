package tcg.frontend.application.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterCommand(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String
)