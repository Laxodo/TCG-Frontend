package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationCommand
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest
import tcg.frontend.infraestructura.entities.expansion.GetExpansionsResponse

class ExpansionRepository(private val url: String, private val _client: HttpClient): IExpansionRepository {
    override suspend fun getExpansionByGeneration(generationId: ListExpansionGenerationCommand): Result<List<Expansion>> {
        return runCatching {
            val response = _client.get("$url/generation/${generationId.idGeneration}/expansions")

            if (response.status.value !in 200 .. 299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }

            val body = response.body<GetExpansionsResponse>()

            body.expansions.map { expansion ->
                Expansion(
                    id = expansion.id,
                    idGeneration = expansion.id_generacion,
                    name = expansion.name,
                    price = expansion.price,
                    year = expansion.year
                )
            }
        }
    }

    override suspend fun createExpansion(request: CreateExpansionRequest): Result<Unit> {
        return runCatching {
            val response = _client.post("$url/expansions/") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            if (response.status.value !in 200..299) {
                throw Exception(
                    "${response.status.value} - ${response.status.description}"
                )
            }
        }
    }
}