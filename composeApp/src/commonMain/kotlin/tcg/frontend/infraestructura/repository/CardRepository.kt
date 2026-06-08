package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionCommand
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.ICardRepository
import tcg.frontend.infraestructura.entities.card.CreateCardRequest
import tcg.frontend.infraestructura.entities.card.GetCardResponse
import tcg.frontend.infraestructura.entities.card.GetCardsResponse

class CardRepository(private val url: String, private val _client: HttpClient): ICardRepository {
    override suspend fun getCardsByExpansion(expansionId: ListCardExpansionCommand): Result<List<Card>> {
        return runCatching {
            val response = _client.get("$url/expansions/${expansionId.idExpansion}/cards")


            if (response.status.value !in 200..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }

            val body = response.body<GetCardsResponse>()

            body.cards.map {
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

    override suspend fun createCard(request: CreateCardRequest): Result<Unit> {
        return runCatching {
            val response = _client.post("$url/cards/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }
        }
    }

    override suspend fun createCardsBatch(request: List<CreateCardRequest>): Result<Unit> {
        return runCatching {
            val response = _client.post("$url/cards/batch") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }
        }
    }
}