package tcg.frontend.dominio

data class LogActivity(
    val idUser: Int,
    val card: LogActivityCard?,
    val idLogHistory: Int,
    val action: String,
    val price: Float,
    val psa: Int?
)

data class LogActivityCard(
    val id: Int,
    val name: String,
    val price: Float,
    val frontcard: String
)