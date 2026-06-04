package tcg.frontend.infraestructura.entities.card

import kotlinx.serialization.Serializable

@Serializable
data class CreateCardRequest(
    val id_expansion: Int,
    val name: String,
    val rarity: String,
    val price: Float,
    val card_number: Int,
    val frontcard: String,
    val backcard: String
)