package tcg.frontend.aplicacion.market.offers.exchange

data class ExchangeCardCommand(
    val id: Int,
    val idCard: Int,
    val psa: Int?
)