package tcg.frontend.aplicacion.expansion.crear

import kotlinx.serialization.Serializable

@Serializable
data class CreateExpansionCommand(
    val id: Int,
    val name: String,
    val year: Int,
    val idGeneration: Int
)