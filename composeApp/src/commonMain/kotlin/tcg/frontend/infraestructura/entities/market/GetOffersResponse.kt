package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class GetOffersResponse(
    val offers: List<OffersResponse>
)

@Serializable
data class OffersResponse(
    val id: Int,
    val id_card: Int?,
    val id_user_card: Int,
    val exchange_type: String,
    val image_card_offer: String,
    val image_card_demanded: String?,
    val expansion_name: String,
    val price: Float?,
    val psa: Int?
)