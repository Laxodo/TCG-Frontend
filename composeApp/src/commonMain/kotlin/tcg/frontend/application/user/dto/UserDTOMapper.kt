package tcg.frontend.application.user.dto

import tcg.frontend.modelo.User

fun User.toDTO() = UserDTO(
    id = id,
    name = name,
    username = username,
    email = email,
    money = money,
    address = address,
    exchanges = exchanges
)
fun UserDTO.toUser()= User(
    id = id,
    name = name,
    username = username,
    password = "",
    email = email,
    money = money,
    address = address,
    exchanges = exchanges
)