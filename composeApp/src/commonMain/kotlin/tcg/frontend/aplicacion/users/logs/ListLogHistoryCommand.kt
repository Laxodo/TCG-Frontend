package tcg.frontend.aplicacion.users.logs

data class ListLogHistoryCommand(
    val id: Int,
    val page: Int,
    val size: Int
)