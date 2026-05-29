package tcg.frontend.infraestructura.entities.expansion

import kotlinx.serialization.Serializable

@Serializable
data class GetExpansionResponse(
    val id_generacion: Int,
    val name: String,
    val year: Int,
    val id: Int
)