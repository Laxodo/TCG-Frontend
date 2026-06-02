package tcg.frontend.infraestructura.entities.expansion

import kotlinx.serialization.Serializable

@Serializable
data class CreateExpansionRequest(
    val id_generacion: Int,
    val name: String,
    val price: Double,
    val year: Int
)