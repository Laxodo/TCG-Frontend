package tcg.frontend.dominio

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: Int,
    val username: String,
    val isAdmin: Boolean
)