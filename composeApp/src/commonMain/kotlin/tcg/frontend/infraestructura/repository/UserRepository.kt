package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.infraestructura.entities.LoginResponse

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
}