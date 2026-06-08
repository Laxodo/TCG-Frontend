package tcg.frontend.aplicacion.card.createBatch

import tcg.frontend.dominio.ICardRepository
import tcg.frontend.infraestructura.entities.card.CreateCardRequest

class CreateCardsBatchUseCase(private val repository: ICardRepository) {
    suspend operator fun invoke(request: List<CreateCardRequest>): Result<Unit> {
        return repository.createCardsBatch(request)
    }
}