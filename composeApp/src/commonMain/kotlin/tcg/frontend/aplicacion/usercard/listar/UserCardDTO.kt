package tcg.frontend.aplicacion.usercard.listar

import tcg.frontend.dominio.Card
import tcg.frontend.dominio.UserCard

data class UserCardDTO(
    val card: Card,
    val userCard: UserCard
)