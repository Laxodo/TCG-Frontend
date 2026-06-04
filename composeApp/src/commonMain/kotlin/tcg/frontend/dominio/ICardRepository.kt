package tcg.frontend.dominio

import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionCommand

interface ICardRepository {
    suspend fun getCardsByExpansion(command: ListCardExpansionCommand): Result<List<Card>>
    suspend fun createCard(request: CreateCardRequest): Result<Unit>
}