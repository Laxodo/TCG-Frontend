package tcg.frontend.dominio

import tcg.frontend.infraestructura.entities.GetExpansionResponse

interface IExpansionRepository {
    suspend fun getExpansion(): Result<List<Expansion>>
}