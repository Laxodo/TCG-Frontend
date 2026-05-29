package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class PostQuickSell(
    val card_list_id: List<Int>
)