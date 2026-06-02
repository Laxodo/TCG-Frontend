package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import tcg.frontend.dominio.Generation
import tcg.frontend.dominio.IGenerationRepository
import tcg.frontend.infraestructura.entities.generation.CreateGenerationRequest
import tcg.frontend.infraestructura.entities.generation.GetGenerationsResponse

class GenerationRepository(private val url: String, private val _client: HttpClient): IGenerationRepository {
    override suspend fun getGenerations(): Result<List<Generation>> {
        return runCatching {
            val response = _client.get("$url/generation/")

            if(response.status.value !in 200 ..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }

            val body = response.body<GetGenerationsResponse>()

            body.generations.map {
                Generation(
                    id = it.id,
                    name = it.name,
                    year = it.year
                )
            }
        }
    }

    override suspend fun createGeneration(request: CreateGenerationRequest): Result<Unit> {
        return runCatching {
            val response = _client.post("$url/generation/") {
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }
        }
    }
}