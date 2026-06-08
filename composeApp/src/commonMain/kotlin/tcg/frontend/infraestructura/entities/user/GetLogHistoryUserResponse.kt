package tcg.frontend.infraestructura.entities.user

import kotlinx.serialization.Serializable
import tcg.frontend.dominio.LogActivity
import tcg.frontend.dominio.LogActivityCard
import tcg.frontend.dominio.LogHistory
import tcg.frontend.dominio.LogHistoryUser

@Serializable
data class GetLogHistoryUserResponse (
    val items: List<LogHistoryResponse>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

@Serializable
data class LogHistoryResponse(
    val user: LogHistoryUserResponse,
    val user_interacted: LogHistoryUserResponse?,
    val description: String,
    val type: String,
    val money_exchange: Float,
    val date: String,
    val activities: List<LogActivityResponse>
)

@Serializable
data class LogHistoryUserResponse(
    val id: Int,
    val username: String,
    val is_admin: Boolean
)

@Serializable
data class LogActivityResponse(
    val id_user: Int,
    val card: LogActivityCardResponse?,
    val id_log_history: Int,
    val action: String,
    val price: Float,
    val psa: Int?
)

@Serializable
data class LogActivityCardResponse(
    val id: Int,
    val name: String,
    val price: Float,
    val frontcard: String
)

fun LogHistoryUserResponse.toDomain() = LogHistoryUser(
    id = id,
    username = username,
    isAdmin = is_admin
)

fun LogActivityCardResponse.toDomain(url: String) = LogActivityCard(
    id = id,
    name = name,
    price = price,
    frontcard = "$url$frontcard"
)

fun LogActivityResponse.toDomain(url: String) = LogActivity(
    idUser = id_user,
    card = card?.toDomain(url),
    idLogHistory = id_log_history,
    action = action,
    price = price,
    psa = psa
)

fun LogHistoryResponse.toDomain(url: String) = LogHistory(
    user = user.toDomain(),
    userInteracted = user_interacted?.toDomain(),
    descripcion = description,
    type = type,
    moneyExchange = money_exchange,
    date = date,
    activities = activities.map { it.toDomain(url) }
)