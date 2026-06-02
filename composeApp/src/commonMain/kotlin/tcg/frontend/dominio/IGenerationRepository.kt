package tcg.frontend.dominio

import tcg.frontend.infraestructura.entities.generation.CreateGenerationRequest

interface IGenerationRepository {
    suspend fun getGenerations(): Result<List<Generation>>
    suspend fun createGeneration(request: CreateGenerationRequest): Result<Unit>
}