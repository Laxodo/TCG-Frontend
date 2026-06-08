package tcg.frontend.aplicacion.generation.listexpansions

import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IGenerationRepository

class ListGenerationExpansionUseCase(private val repository: IGenerationRepository) {
    suspend fun invoke(command: ListGenerationExpansionCommand): Result<List<Expansion>>{
        return repository.getGeneratrionsExpansion(command)
    }
}