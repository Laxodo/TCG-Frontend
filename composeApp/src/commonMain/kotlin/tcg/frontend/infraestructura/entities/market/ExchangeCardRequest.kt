package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeCardRequest(
    val id_card: Int,
    val psa: Int?
)