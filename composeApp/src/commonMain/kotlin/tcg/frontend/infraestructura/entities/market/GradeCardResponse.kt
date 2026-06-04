package tcg.frontend.infraestructura.entities.market

import kotlinx.serialization.Serializable

@Serializable
data class GradeCardResponse(
    val grade: Int
)