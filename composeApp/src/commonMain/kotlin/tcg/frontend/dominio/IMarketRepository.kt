package tcg.frontend.dominio

import tcg.frontend.aplicacion.market.grade.GradeCardCommand
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferCommand
import tcg.frontend.aplicacion.market.offers.cancel.CancelOfferCommand
import tcg.frontend.aplicacion.market.offers.exchange.ExchangeCardCommand
import tcg.frontend.aplicacion.market.offers.list.ListOffersCommand
import tcg.frontend.aplicacion.market.sell.SellOfferCommand
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand

interface IMarketRepository {
    suspend fun quickSell(quickSellCommand: QuickSellCommand): Result<Float>
    suspend fun openBooster(openBoosterCommand: OpenBoosterCommand): Result<List<Card>>
    suspend fun getSellOffers(listOffersCommand: ListOffersCommand): Result<List<Offer>>
    suspend fun buyOffer(buyOfferCommand: BuyOfferCommand): Result<Boolean>
    suspend fun sellCard(sellOfferCommand: SellOfferCommand): Result<Boolean>
    suspend fun exchangeCard(exchangeCardCommand: ExchangeCardCommand): Result<Unit>
    suspend fun gradeCard(gradeCardCommand: GradeCardCommand): Result<Int>
    suspend fun cancelOffer(cancelOfferCommand: CancelOfferCommand): Result<Unit>
}