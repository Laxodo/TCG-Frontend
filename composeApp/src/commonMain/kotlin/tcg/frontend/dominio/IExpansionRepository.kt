package tcg.frontend.dominio

import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse

interface IExpansionRepository {
    suspend fun getExpansion(): Result<List<Expansion>>
}