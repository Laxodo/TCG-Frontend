package tcg.frontend.aplicacion.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginCommand(
    val username: String,
    val password: String
)