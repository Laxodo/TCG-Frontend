package tcg.frontend.dominio

import tcg.frontend.infraestructura.entities.user.LogHistoryResponse

data class LogHistoryPagination(
    val items: List<LogHistory>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

data class LogHistory(
    val user: LogHistoryUser,
    val userInteracted: LogHistoryUser?,
    val descripcion: String,
    val type: String,
    val moneyExchange: Float,
    val date: String,
    val activities: List<LogActivity>
)

data class LogHistoryUser(
    val id: Int,
    val username: String,
    val isAdmin: Boolean
)