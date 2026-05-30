package tcg.frontend.infraestructura

import kotlinx.serialization.Serializable

// This file will need to be moved to
// infraestructura.entitites.user
@Serializable
data class RegisterResponse (
    val id: String,
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val confirm_password: String
)