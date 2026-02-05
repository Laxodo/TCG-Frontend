package tcg.frontend.application.user.dto

data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val money: Float,
    val address: String,
    val exchanges: Int
)