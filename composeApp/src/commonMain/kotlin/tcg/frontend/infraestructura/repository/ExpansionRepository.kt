package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.infraestructura.entities.expansion.GetExpansionResponse

class ExpansionRepository(private val url: String, private val _client: HttpClient): IExpansionRepository {
    override suspend fun getExpansion(): Result<List<Expansion>> {
        return runCatching {
            val request = this._client.get("$url/expansions/")

            val item = request.body<List<GetExpansionResponse>>()

            if (request.status.value !in 200 ..< 300){
                throw Exception("${request.status.value}-${request.status.description}")
            }

            item.map { it ->
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