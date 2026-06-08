package tcg.frontend.aplicacion.market.sell

import tcg.frontend.dominio.IMarketRepository

class SellOfferUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: SellOfferCommand): Result<Boolean>{
        return repository.sellCard(command)
    }
}