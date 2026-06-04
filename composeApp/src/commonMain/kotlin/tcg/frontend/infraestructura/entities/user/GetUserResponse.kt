package tcg.frontend.infraestructura.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class GetUserListResponse(
    val users: List<GetUserResponse>
)

@Serializable
data class GetUserResponse(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val money: Float,
    val opened_boosters: Int,
    val exchanges: Int,
    val is_admin: Boolean
)