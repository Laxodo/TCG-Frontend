package tcg.frontend.infraestructura.entities.card

import kotlinx.serialization.Serializable

@Serializable
data class GetCardsResponse(
    val cards: List<GetCardResponse>
)
