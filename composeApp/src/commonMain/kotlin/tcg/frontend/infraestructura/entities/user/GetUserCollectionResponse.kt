package tcg.frontend.infraestructura.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class GetUserCollectionResponse (
    val id_card: Int,
    val card_number: Int,
    val card_name: String,
    val quantity: Int,
    val frontcard: String
)