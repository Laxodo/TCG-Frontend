package tcg.frontend.aplicacion.usercard.listar

import kotlinx.serialization.Serializable

@Serializable
data class ListUserCardCommand(
    val idUser: Int,
    val idExpansion: Int,
    val limit: Int,
    val offset: Int
)