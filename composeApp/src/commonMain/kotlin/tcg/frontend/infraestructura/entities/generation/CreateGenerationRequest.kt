package tcg.frontend.infraestructura.entities.generation

import kotlinx.serialization.Serializable

@Serializable
data class CreateGenerationRequest(
    val name: String,
    val year: Int
)