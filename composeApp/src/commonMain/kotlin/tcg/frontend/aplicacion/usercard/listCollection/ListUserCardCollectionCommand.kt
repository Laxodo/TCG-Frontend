package tcg.frontend.aplicacion.usercard.listCollection

import kotlinx.serialization.Serializable

@Serializable
data class ListUserCardCollectionCommand(
    val idUser: Int,
    val idExpansion: Int
)