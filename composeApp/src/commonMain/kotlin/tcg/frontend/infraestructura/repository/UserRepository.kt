package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import tcg.frontend.aplicacion.users.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionCommand
import tcg.frontend.aplicacion.usercard.listCollection.UserCardCollectionDTO
import tcg.frontend.aplicacion.usercard.listar.ListUserCardCommand
import tcg.frontend.aplicacion.users.getuser.GetUserCommand
import tcg.frontend.aplicacion.users.logs.ListLogHistoryCommand
import tcg.frontend.aplicacion.users.update.UpdateUserCommand
import tcg.frontend.dominio.Card
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.LogHistory
import tcg.frontend.dominio.LogHistoryPagination
import tcg.frontend.dominio.User
import tcg.frontend.dominio.UserCard
import tcg.frontend.infraestructura.entities.user.GetLogHistoryUserResponse
import tcg.frontend.infraestructura.entities.user.GetUserByIdResponse
import tcg.frontend.infraestructura.entities.user.GetUserCardListResponse
import tcg.frontend.infraestructura.entities.user.GetUserCardResponse
import tcg.frontend.infraestructura.entities.user.GetUserCollectionListResponse
import tcg.frontend.infraestructura.entities.user.GetUserCollectionResponse
import tcg.frontend.infraestructura.entities.user.GetUserListResponse
import tcg.frontend.infraestructura.entities.user.GetUserResponse
import tcg.frontend.infraestructura.entities.user.LoginResponse
import tcg.frontend.infraestructura.entities.user.UpdateUserRequest
import tcg.frontend.infraestructura.entities.user.toDomain
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

            val item = request.body<GetUserListResponse>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.users.map { it ->
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

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            val item = request.body<GetUserByIdResponse>()

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

    override suspend fun updateUser(updateUserCommand: UpdateUserCommand): Result<Boolean> {
        return runCatching {
            val request = this._client.patch("$url/users/${updateUserCommand.id}"){
                setBody(UpdateUserRequest(
                    name = updateUserCommand.name,
                    username = updateUserCommand.username,
                    email = updateUserCommand.email,
                    money = updateUserCommand.money,
                    opened_boosters = updateUserCommand.openBoosted,
                    exchanges = updateUserCommand.exchanges,
                    is_admin = updateUserCommand.isAdmin
                ))
            }

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

            val item = request.body<GetUserCardListResponse>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.cards.associate { it ->
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

            val item = request.body<GetUserCollectionListResponse>()

            if(request.status.value !in 200..<300)
                throw Exception("${request.status.value}-${request.status.description}")

            item.collection.map { it ->
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

    override suspend fun getLogHistoryUser(listLogHistoryCommand: ListLogHistoryCommand): Result<LogHistoryPagination> {
        return runCatching {
            val request = _client.get("$url/users/${listLogHistoryCommand.id}/logs")

            val item = request.body<GetLogHistoryUserResponse>()

            val responseString = request.body<String>()
            println("JSON REAL RECIBIDO: $responseString")

            LogHistoryPagination(
                items = item.items.map { it.toDomain() },
                total = item.total,
                page = item.page,
                size = item.size,
                pages = item.pages
            )
        }
    }
}




















