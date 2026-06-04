package tcg.frontend.aplicacion.expansion.listCardExpansion

import kotlinx.serialization.Serializable

@Serializable
data class ListCardExpansionCommand(
    val idExpansion: Int
)