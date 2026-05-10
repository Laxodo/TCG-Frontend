package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.internal.throwMissingFieldException
import tcg.frontend.aplicacion.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User
import tcg.frontend.infraestructura.entities.GetUserResponse
import tcg.frontend.infraestructura.entities.LoginResponse
import kotlin.runCatching

class UserRepository(private val url: String, private val _client: HttpClient) : IUserRepository {
    override suspend fun login(loginCommand: LoginCommand): Result<LoginResponse> {
        return runCatching {
            val request = this._client.post("$url/users/login") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(FormDataContent(Parameters.build {
                    append("username", loginCommand.username)
                    append("password", loginCommand.password)
                }))
            }

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            val item = request.body<LoginResponse>()
            item
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return runCatching {
            val request = this._client.get("$url/users/")

            val item = request.body<List<GetUserResponse>>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.map { it ->
                User(
                    id = it.id.toInt(),
                    name = it.name,
                    username = it.username,
                    email = it.email,
                    money = it.money,
                    openBoosted = it.opened_boosters,
                    exchanges = it.exchanges,
                    isAdmin = it.is_admin
                )
            }
        }
    }

    override suspend fun deleteUser(deleteCommand: DeleteUserCommand): Result<Boolean> {
        return runCatching {
            val request = this._client.delete("$url/users/${deleteCommand.id}")

            if (request.status.value == 200){
                true
            }else
                throw Exception("${request.status.value}-${request.status.description}")

        }
    }
}