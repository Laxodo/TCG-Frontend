package tcg.frontend.aplicacion.market.offers.cancel

import tcg.frontend.dominio.IMarketRepository

class CancelOfferUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: CancelOfferCommand): Result<Unit>{
        return repository.cancelOffer(command)
    }
}