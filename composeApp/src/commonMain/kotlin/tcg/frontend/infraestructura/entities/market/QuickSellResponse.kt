package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class QuickSellResponse(
    val total_earn: Float
)