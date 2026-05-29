package tcg.frontend.dominio

import tcg.frontend.aplicacion.market.openboosted.OpenBoosterCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand

interface IMarketRepository {
    suspend fun quickSell(quickSellCommand: QuickSellCommand): Result<Float>
    suspend fun openBooster(openBoosterCommand: OpenBoosterCommand): Result<List<Card>>
}