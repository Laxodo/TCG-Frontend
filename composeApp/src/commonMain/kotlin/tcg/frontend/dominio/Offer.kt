package tcg.frontend.dominio

data class Offer(
    val id: Int,
    val idCard: Int?,
    val idUserCard: Int,
    val exchangeType: String,
    val imagecardOffer: String,
    val imageCardDemanded: String?,
    val expansionName: String,
    val price: Float?,
    val psa: Int?,
)