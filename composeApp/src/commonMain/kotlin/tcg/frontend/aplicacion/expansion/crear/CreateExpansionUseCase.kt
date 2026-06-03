package tcg.frontend.aplicacion.expansion.crear

import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest

class CreateExpansionUseCase(private val repository: IExpansionRepository) {
    suspend operator fun invoke(request: CreateExpansionRequest): Result<Unit> {
        return repository.createExpansion(request)
    }
}