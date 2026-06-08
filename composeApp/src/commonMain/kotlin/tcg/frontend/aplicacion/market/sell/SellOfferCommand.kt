package tcg.frontend.aplicacion.market.sell

import kotlinx.serialization.Serializable

@Serializable
data class SellOfferCommand(
    val id: Int,
    val price: Float
)