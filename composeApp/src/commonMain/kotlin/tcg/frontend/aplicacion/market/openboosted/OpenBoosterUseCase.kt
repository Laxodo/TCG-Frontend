package tcg.frontend.aplicacion.market.openboosted

import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IMarketRepository

class OpenBoosterUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: OpenBoosterCommand): Result<List<Card>>{
        return repository.openBooster(command)
    }
}