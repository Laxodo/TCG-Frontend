package tcg.frontend.infraestructura.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class GetUserByIdResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val money: Float,
    val opened_boosters: Int,
    val exchanges: Int,
    val is_admin: Boolean
)