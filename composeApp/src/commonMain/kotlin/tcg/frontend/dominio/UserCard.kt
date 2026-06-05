package tcg.frontend.dominio

data class UserCard(
    val id: Int,
    val idUser: Int,
    val idCard: Int,
    val price: Float,
    val psa: Int?,
    val sold: Boolean
)