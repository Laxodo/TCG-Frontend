package tcg.frontend.aplicacion.usercard.listar

import tcg.frontend.dominio.Card
import tcg.frontend.dominio.UserCard

fun UserCard.toDTO(card: Card) = UserCardDTO(
    card = card,
    userCard = this
)