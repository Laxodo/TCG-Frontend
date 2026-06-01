package tcg.frontend.infraestructura.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse (
    val id: String,
    val name: String,
    val email: String,
    val username: String
)