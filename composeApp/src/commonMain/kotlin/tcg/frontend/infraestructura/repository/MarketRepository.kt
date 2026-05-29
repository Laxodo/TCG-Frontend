package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IMarketRepository
import tcg.frontend.infraestructura.entities.market.OpenBoosterResponse
import tcg.frontend.infraestructura.entities.market.PostQuickSell
import tcg.frontend.infraestructura.entities.market.QuickSellResponse

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

            val item = request.body<List<OpenBoosterResponse>>()
            item.map { it ->
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
}