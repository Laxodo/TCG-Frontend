package tcg.frontend.aplicacion.generation.listar

import tcg.frontend.dominio.Generation
import tcg.frontend.dominio.IGenerationRepository

class ListGenerationUseCase(private val repository: IGenerationRepository) {
    suspend fun invoke(): Result<List<Generation>> {
        return repository.getGeneration()
    }
}