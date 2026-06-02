package tcg.frontend.aplicacion.generation.crear

import tcg.frontend.dominio.IGenerationRepository
import tcg.frontend.infraestructura.entities.generation.CreateGenerationRequest

class CreateGenerationUseCase(private val repository: IGenerationRepository) {
    suspend operator fun invoke(request: CreateGenerationRequest): Result<Unit> {
        return repository.createGeneration(request)
    }
}