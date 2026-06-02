package tcg.frontend.infraestructura.entities.expansion

import kotlinx.serialization.Serializable

@Serializable
data class GetExpansionCardsListResponse(
    val cards: List<GetExpansionCardsResponse>
)

@Serializable
data class GetExpansionCardsResponse(
    val id_expansion: Int,
    val name: String,
    val rarity: String,
    val price: Float,
    val card_number: Int,
    val frontcard: String,
    val backcard: String,
    val id: Int
)