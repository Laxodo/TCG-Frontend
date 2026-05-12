package tcg.frontend.infraestructura.entities

import kotlinx.serialization.Serializable

@Serializable
data class GetUserCardResponse (
    val card: GetCardResponse,
    val user_cards: List<UserCardResponse>
)

@Serializable
data class UserCardResponse(
    val id: Int,
    val id_user: Int,
    val id_card: Int,
    val price: Float,
    val psa: Int?,
    val sold: Boolean
)