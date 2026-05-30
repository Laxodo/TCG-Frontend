package tcg.frontend.aplicacion.usercard.listCollection

data class UserCardCollectionDTO(
    val idCard: Int,
    val cardNumber: Int,
    val cardName: String,
    val quantity: Int,
    val frontcard: String
)