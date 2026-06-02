package tcg.frontend.aplicacion.market.offers.buy

import tcg.frontend.dominio.IMarketRepository

class BuyOfferUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: BuyOfferCommand): Result<Boolean>{
        return repository.buyOffer(command)
    }
}