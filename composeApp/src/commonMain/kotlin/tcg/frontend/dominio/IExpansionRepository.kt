package tcg.frontend.dominio

import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionCommands
import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse

interface IExpansionRepository {
    suspend fun getExpansion(): Result<List<Expansion>>
    suspend fun getCardsExpansion(listCardsExpansionCommand: ListCardsExpansionCommands): Result<List<Card>>
}