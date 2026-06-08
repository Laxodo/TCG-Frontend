package tcg.frontend.aplicacion.market.offers.list

import tcg.frontend.dominio.IMarketRepository
import tcg.frontend.dominio.Offer

class ListOffersUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: ListOffersCommand): Result<List<Offer>>{
        return repository.getSellOffers(command)
    }
}