package tcg.frontend.dominio

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: Long,
    val username: String,
    val isAdmin: Boolean
)