package tcg.frontend.dominio

import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest
import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse

interface IExpansionRepository {
    suspend fun getExpansionByGeneration(generationId: Int): Result<List<Expansion>>
    suspend fun createExpansion(request: CreateExpansionRequest): Result<Unit>
}