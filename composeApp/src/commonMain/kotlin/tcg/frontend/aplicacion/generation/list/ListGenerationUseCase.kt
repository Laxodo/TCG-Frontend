package tcg.frontend.aplicacion.generation.list

import tcg.frontend.dominio.Generation
import tcg.frontend.dominio.IGenerationRepository

class ListGenerationUseCase(private val repository: IGenerationRepository) {
    suspend fun invoke(): Result<List<Generation>> {
        return repository.getGeneration()
    }
}