package tcg.frontend.infraestructura.entities.generation

import kotlinx.serialization.Serializable

@Serializable
data class GetGenerationsListResponse(
    val generations: List<GetGenerationsResponse>
)

@Serializable
data class GetGenerationsResponse(
    val id: Int,
    val name: String,
    val year: Int
)