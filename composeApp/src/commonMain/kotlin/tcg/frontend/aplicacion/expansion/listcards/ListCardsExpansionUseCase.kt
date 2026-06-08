package tcg.frontend.aplicacion.expansion.listcards

import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IExpansionRepository

class ListCardsExpansionUseCase(private val repository: IExpansionRepository) {
    suspend fun invoke(command: ListCardsExpansionCommands): Result<List<Card>>{
        return repository.getCardsExpansion(command)
    }
}