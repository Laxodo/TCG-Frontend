package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.frontend.aplicacion.expansion.listcards.ListCardsExpansionCommands
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.infraestructura.entities.expansion.GetExpansionCardsListResponse
import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse

class ExpansionRepository(private val url: String, private val _client: HttpClient): IExpansionRepository {
    override suspend fun getExpansion(): Result<List<Expansion>> {
        return runCatching {
            val request = this._client.get("$url/expansions/")

            val item = request.body<GetExpansionResponse>()

            if (request.status.value !in 200 ..< 300){
                throw Exception("${request.status.value}-${request.status.description}")
            }

            item.expansions.map { it ->
                Expansion(
                    id = it.id,
                    idGeneration = it.id_generacion,
                    name = it.name,
                    price = it.price,
                    year = it.year
                )
            }
        }
    }

    override suspend fun getCardsExpansion(listCardsExpansionCommand: ListCardsExpansionCommands): Result<List<Card>> {
        return runCatching {
            val request = _client.get("$url/expansions/${listCardsExpansionCommand.id}/cards")

            if (request.status.value !in 200 ..< 300){
                throw Exception("${request.status.value}-${request.status.description}")
            }

            val item = request.body<GetExpansionCardsListResponse>()
            item.cards.map { it ->
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