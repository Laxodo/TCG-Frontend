package tcg.frontend.infraestructura

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import tcg.frontend.application.Dto.RegisterDto
import tcg.frontend.application.user.command.RegisterCommand
import tcg.frontend.modelo.IUserRepository
import tcg.frontend.modelo.User

class UserRepository(private val url: String, private val _client: HttpClient): IUserRepository {
    override suspend fun register(registerCommand: RegisterCommand): Result<RegisterDto> {
        return runCatching {
            val request = this._client.post("$url/users/register") {
                contentType(ContentType.Application.Json)
                setBody(registerCommand)
            }

            when (request.status) {
                HttpStatusCode.Created -> ""
                HttpStatusCode.BadRequest -> throw Exception("Usuario o contraseña no validos")
                HttpStatusCode.Conflict -> throw Exception("Usuario o contraseña incorrecto")
                else -> throw Exception(request.status.description)
            }

            val item = request.body<RegisterDto>()
            item
        }
    }

    override suspend fun findAll(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun remove(item: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: User): Boolean {
        TODO("Not yet implemented")
    }
}
