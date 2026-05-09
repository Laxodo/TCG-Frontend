package tcg.frontend.dominio

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val money: Int,
    val openBoosted: Int,
    val exchanges: Int,
    val isAdmin: Boolean
)