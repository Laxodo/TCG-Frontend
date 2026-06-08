package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class OpenBoosterListResponse(
    val booster: List<OpenBoosterResponse>
)

@Serializable
data class OpenBoosterResponse(
    val id: Int,
    val id_expansion: Int,
    val name: String,
    val rarity: String,
    val price: Float,
    val card_number: Int,
    val frontcard: String,
    val backcard: String
)