package tcg.frontend.aplicacion.expansion.crear

import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest

class CreateExpansionUseCase(private val repository: IExpansionRepository) {
    suspend fun invoke(command: CreateExpansionCommand): Result<CreateExpansionRequest> {
        repository.createExpansion(command)
    }
}