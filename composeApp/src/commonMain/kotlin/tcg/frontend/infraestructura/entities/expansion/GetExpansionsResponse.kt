package tcg.frontend.infraestructura.entities.expansion

import kotlinx.serialization.Serializable

@Serializable
data class GetExpansionsResponse (
    val expansions: List<GetExpansionResponse>
)