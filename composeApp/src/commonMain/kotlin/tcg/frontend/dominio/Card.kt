package tcg.frontend.dominio

data class Card(
    val id: Int,
    val idExpansion: Int,
    val name: String,
    val rarity: String,
    val price: Float,
    val cardNumber: Int,
    val frontcard: String,
    val backcard: String
)