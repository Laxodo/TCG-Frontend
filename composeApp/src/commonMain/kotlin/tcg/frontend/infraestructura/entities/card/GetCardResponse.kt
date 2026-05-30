package tcg.frontend.infraestructura.entities.card

import kotlinx.serialization.Serializable

@Serializable
data class GetCardResponse(
    val id: Int,
    val id_expansion: Int,
    val name: String,
    val rarity: String,
    val price: Float,
    val card_number: Int,
    val frontcard: String,
    val backcard: String
)