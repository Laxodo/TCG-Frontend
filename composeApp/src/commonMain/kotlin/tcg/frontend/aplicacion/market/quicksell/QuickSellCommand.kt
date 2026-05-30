package tcg.frontend.aplicacion.market.quicksell

import kotlinx.serialization.Serializable

@Serializable
data class QuickSellCommand(
    val listId: List<Int>
)