package tcg.frontend.aplicacion.market.offers.exchange

import tcg.frontend.dominio.IMarketRepository

class ExchangeCardUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: ExchangeCardCommand): Result<Unit>{
        return repository.exchangeCard(command)
    }
}