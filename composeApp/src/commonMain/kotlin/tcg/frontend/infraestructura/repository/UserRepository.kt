package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import tcg.frontend.aplicacion.usuarios.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionCommand
import tcg.frontend.aplicacion.usercard.listCollection.UserCardCollectionDTO
import tcg.frontend.aplicacion.usercard.listar.ListUserCardCommand
import tcg.frontend.aplicacion.usuarios.getuser.GetUserCommand
import tcg.frontend.application.register.RegisterCommand
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User
import tcg.frontend.dominio.UserCard
import tcg.frontend.infraestructura.entities.user.GetUserByIdResponse
import tcg.frontend.infraestructura.entities.user.GetUserCardResponse
import tcg.frontend.infraestructura.entities.user.GetUserCollectionResponse
import tcg.frontend.infraestructura.entities.user.GetUserResponse
import tcg.frontend.infraestructura.entities.user.LoginResponse
import tcg.frontend.infraestructura.entities.user.RegisterResponse
import kotlin.runCatching

class UserRepository(private val url: String, private val _client: HttpClient) : IUserRepository {
    override suspend fun register(registerCommand: RegisterCommand): Result<Unit> {
        return runCatching {
            val request = this._client.post("$url/users/signup") {
                contentType(ContentType.Application.Json)
                setBody(registerCommand)
            }

            when (request.status) {
                HttpStatusCode.Created -> Unit
                HttpStatusCode.BadRequest -> throw Exception("Usuario o contraseña no validos")
                HttpStatusCode.Conflict -> throw Exception("Usuario o contraseña incorrecto")
                else -> throw Exception(request.status.description)
            }
        }
    }

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

    override suspend fun getUserById(getUserCommand: GetUserCommand): Result<User> {
        return runCatching {
            val request = this._client.get("$url/users/${getUserCommand.id}")

            val item = request.body<GetUserByIdResponse>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            User(
                id = item.id,
                name = item.name,
                username = item.username,
                email = item.email,
                money = item.money,
                openBoosted = item.opened_boosters,
                exchanges = item.exchanges,
                isAdmin = item.is_admin
            )
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

    override suspend fun getUserCards(listUserCardCommand: ListUserCardCommand): Result<Map<Card, List<UserCard>>> {
        return runCatching {
            val request = this._client.get(
                "$url/users/${listUserCardCommand.idUser}/inventory" +
                    "?expansion=${listUserCardCommand.idExpansion}" +
                    "&limit=${listUserCardCommand.limit}" +
                    "&offset=${listUserCardCommand.offset}"
            )

            val item = request.body<List<GetUserCardResponse>>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.associate { it ->
                val card = Card(
                    id = it.card.id,
                    idExpansion = it.card.id_expansion,
                    name = it.card.name,
                    rarity = it.card.rarity,
                    price = it.card.price,
                    cardNumber = it.card.card_number,
                    frontcard = it.card.frontcard,
                    backcard = it.card.backcard
                )
                val userCards = it.user_cards.map { it ->
                    UserCard(
                        id = it.id,
                        idUser = it.id_user,
                        idCard = it.id_card,
                        price = it.price,
                        psa = it.psa,
                        sold = it.sold
                    )
                }
                card to userCards
            }
        }
    }

    override suspend fun getUserCollection(listUserCardCollectionCommand: ListUserCardCollectionCommand): Result<List<UserCardCollectionDTO>> {
        return runCatching {
            val request = this._client.get(
                "$url/users/${listUserCardCollectionCommand.idUser}/collection" +
                        "?expansion=${listUserCardCollectionCommand.idExpansion}"
            )

            val responseString = request.body<String>()
            println("JSON REAL RECIBIDO: $responseString")
            val item = request.body<List<GetUserCollectionResponse>>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.map { it ->
                UserCardCollectionDTO(
                    idCard = it.id_card,
                    cardNumber = it.card_number,
                    cardName = it.card_name,
                    quantity = it.quantity,
                    frontcard = it.frontcard
                )
            }
        }
    }
}