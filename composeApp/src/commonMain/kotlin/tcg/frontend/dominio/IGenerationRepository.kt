package tcg.frontend.dominio

import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionCommand

interface IGenerationRepository {
    suspend fun getGeneration(): Result<List<Generation>>
    suspend fun getGeneratrionsExpansion(listGenerationExpansionCommand: ListGenerationExpansionCommand): Result<List<Expansion>>
}