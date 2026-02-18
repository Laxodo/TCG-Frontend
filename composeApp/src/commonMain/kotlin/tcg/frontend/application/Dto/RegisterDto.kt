package tcg.frontend.application.Dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    val id: String,
    val username: String,
    val name: String,
    val password: String,
    val image: String? = null
)