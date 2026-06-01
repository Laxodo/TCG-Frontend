package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.frontend.dominio.Generation
import tcg.frontend.dominio.IGenerationRepository
import tcg.frontend.infraestructura.entities.generation.GetGenerationResponse

class GenerationRepository(private val url: String, private val _client: HttpClient): IGenerationRepository {
    override suspend fun getGeneration(): Result<List<Generation>> {
        return runCatching {
            val request = this._client.get("$url/generation/")

            val item = request.body<List<GetGenerationResponse>>()

            if (request.status.value !in 200..< 300) {
                throw Exception("${request.status.value}-${request.status.description}")
            }

            item.map { it->
                Generation(
                    id = it.id,
                    name = it.name,
                    year = it.year
                )
            }
        }
    }
}