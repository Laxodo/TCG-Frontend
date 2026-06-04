package tcg.frontend.aplicacion.expansion.listCardExpansion

import tcg.frontend.dominio.Card
import tcg.frontend.dominio.ICardRepository

class ListCardExpansionUseCase(private val repository: ICardRepository) {
    suspend operator fun invoke(command: ListCardExpansionCommand): Result<List<Card>> {
        return repository.getCardsByExpansion(command)
    }
}