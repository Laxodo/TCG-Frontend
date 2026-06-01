package tcg.frontend.infraestructura.entities.generation

import kotlinx.serialization.Serializable

@Serializable
data class GetGenerationResponse(
    val id: Int,
    val name: String,
    val year: Int
)