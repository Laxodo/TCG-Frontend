package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.frontend.aplicacion.generation.listexpansions.ListGenerationExpansionCommand
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.Generation
import tcg.frontend.dominio.IGenerationRepository
import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse
import tcg.frontend.infraestructura.entities.generation.GetGenerationsListResponse

class GenerationRepository(private val url: String, private val _client: HttpClient): IGenerationRepository {
    override suspend fun getGeneration(): Result<List<Generation>> {
        return runCatching {
            val request = _client.get("$url/generations")

            if (request.status.value !in 200 ..< 300){
                throw Exception("${request.status.value}-${request.status.description}")
            }

            val item = request.body<GetGenerationsListResponse>()
            item.generations.map {
                Generation(
                    id = it.id,
                    name = it.name,
                    year = it.year
                )
            }

        }
    }

    override suspend fun getGeneratrionsExpansion(listGenerationExpansionCommand: ListGenerationExpansionCommand): Result<List<Expansion>> {
        return runCatching {
            val request = _client.get("$url/generations/${listGenerationExpansionCommand.id}/expansions")

            if (request.status.value !in 200 ..< 300){
                throw Exception("${request.status.value}-${request.status.description}")
            }

            val items = request.body<GetExpansionResponse>()
            items.expansions.map { it ->
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
}
















