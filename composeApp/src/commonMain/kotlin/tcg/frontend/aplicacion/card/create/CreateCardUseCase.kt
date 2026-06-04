package tcg.frontend.aplicacion.card.create

import tcg.frontend.dominio.ICardRepository
import tcg.frontend.infraestructura.entities.card.CreateCardRequest

class CreateCardUseCase(private val repository: ICardRepository) {
    suspend operator fun invoke(request: CreateCardRequest): Result<Unit> {
        return repository.createCard(request)
    }
}