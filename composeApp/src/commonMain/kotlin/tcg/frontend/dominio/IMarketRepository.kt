package tcg.frontend.dominio

import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand

interface IMarketRepository {
    suspend fun quickSell(quickSellCommand: QuickSellCommand): Result<Float>
}