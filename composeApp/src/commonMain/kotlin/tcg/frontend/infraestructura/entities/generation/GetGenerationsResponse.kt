package tcg.frontend.infraestructura.entities.generation

import kotlinx.serialization.Serializable

@Serializable
data class GetGenerationsResponse(
    val generations: List<GetGenerationResponse>
)