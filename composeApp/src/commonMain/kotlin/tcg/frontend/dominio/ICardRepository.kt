package tcg.frontend.dominio

import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionCommand
import tcg.frontend.infraestructura.entities.card.CreateCardRequest

interface ICardRepository {
    suspend fun getCardsByExpansion(command: ListCardExpansionCommand): Result<List<Card>>
    suspend fun createCard(request: CreateCardRequest): Result<Unit>
    suspend fun createCardsBatch(request: List<CreateCardRequest>): Result<Unit>
}