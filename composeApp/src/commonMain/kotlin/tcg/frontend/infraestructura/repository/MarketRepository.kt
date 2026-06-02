package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferCommand
import tcg.frontend.aplicacion.market.offers.cancel.CancelOfferCommand
import tcg.frontend.aplicacion.market.offers.exchange.ExchangeCardCommand
import tcg.frontend.aplicacion.market.offers.list.ListOffersCommand
import tcg.frontend.aplicacion.market.offers.sell.SellOfferCommand
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IMarketRepository
import tcg.frontend.dominio.Offer
import tcg.frontend.infraestructura.entities.market.ExchangeCardRequest
import tcg.frontend.infraestructura.entities.market.GetOffersResponse
import tcg.frontend.infraestructura.entities.market.OpenBoosterListResponse
import tcg.frontend.infraestructura.entities.market.OpenBoosterResponse
import tcg.frontend.infraestructura.entities.market.PostQuickSell
import tcg.frontend.infraestructura.entities.market.QuickSellResponse
import tcg.frontend.infraestructura.entities.market.SellCardResponse

class MarketRepository(private val url: String, private val _client: HttpClient): IMarketRepository {
    override suspend fun quickSell(quickSellCommand: QuickSellCommand): Result<Float> {
        return runCatching {
            val request = _client.post("$url/market/cards/quick-sell"){
                setBody(PostQuickSell(quickSellCommand.listId))
            }

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            val item = request.body<QuickSellResponse>()
            item.total_earn
        }
    }

    override suspend fun openBooster(openBoosterCommand: OpenBoosterCommand): Result<List<Card>> {
        return runCatching {
            val request = _client.post("$url/market/boosters/${openBoosterCommand.id_booster}/open")

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            val item = request.body<OpenBoosterListResponse>()
            item.booster.map { it ->
                Card(
                    id = it.id,
                    idExpansion = it.id_expansion,
                    name = it.name,
                    rarity = it.rarity,
                    price = it.price,
                    cardNumber = it.card_number,
                    frontcard = it.frontcard,
                    backcard = it.backcard
                )
            }
        }
    }

    override suspend fun getSellOffers(listOffersCommand: ListOffersCommand): Result<List<Offer>> {
        return runCatching {
            var dynamicURL = "$url/market/offers?"
            if (listOffersCommand.id != null) dynamicURL += "id_user=${listOffersCommand.id}&"
            if (listOffersCommand.type != null) dynamicURL += "type=${listOffersCommand.type}"

            val request = _client.get(dynamicURL)
            if (listOffersCommand.id != null)

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            val items = request.body<GetOffersResponse>()

            items.offers.map { it ->
                Offer(
                    id = it.id,
                    idCard = it.id_card,
                    idUserCard = it.id_user_card,
                    exchangeType = it.exchange_type,
                    price = it.price,
                    psa = it.psa,
                    imagecardOffer = it.image_card_offer,
                    imageCardDemanded = it.image_card_demanded,
                    expansionName = it.expansion_name
                )
            }
        }
    }

    override suspend fun buyOffer(buyOfferCommand: BuyOfferCommand): Result<Boolean> {
        return runCatching {
            val request = _client.post("$url/market/offers/${buyOfferCommand.id}/buy")

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            true
        }
    }

    override suspend fun sellCard(sellOfferCommand: SellOfferCommand): Result<Boolean> {
        return runCatching {
            val request = _client.post("$url/market/cards/${sellOfferCommand.id}/sell"){
                setBody(SellCardResponse(sellOfferCommand.price))
            }

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            true
        }
    }

    override suspend fun exchangeCard(exchangeCardCommand: ExchangeCardCommand): Result<Unit> {
        return runCatching {
            val request = _client.post("$url/market/cards/${exchangeCardCommand.id}/exchange"){
                setBody(ExchangeCardRequest(id_card = exchangeCardCommand.idCard, psa = exchangeCardCommand.psa))
            }

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")
        }
    }

    override suspend fun cancelOffer(cancelOfferCommand: CancelOfferCommand): Result<Unit> {
        return runCatching {
            val request = _client.delete("$url/market/offers/${cancelOfferCommand.id}")

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")
        }
    }
}