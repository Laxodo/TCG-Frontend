package tcg.frontend.aplicacion.market.quicksell

import tcg.frontend.dominio.IMarketRepository

class QuickSellUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: QuickSellCommand): Result<Float>{
        return repository.quickSell(command)
    }
}