package tcg.frontend.infraestructura.entities.expansion

import kotlinx.serialization.Serializable

@Serializable
data class GetExpansionResponse(
    val expansions: List<ExpansionResponse>
)

@Serializable
data class ExpansionResponse(
    val id_generacion: Int,
    val name: String,
    val price: Float,
    val year: Int,
    val id: Int
)