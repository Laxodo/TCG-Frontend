package tcg.frontend.dominio

import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationCommand
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest

interface IExpansionRepository {
    suspend fun getExpansionByGeneration(generationId: ListExpansionGenerationCommand): Result<List<Expansion>>
    suspend fun createExpansion(request: CreateExpansionRequest): Result<Unit>
}