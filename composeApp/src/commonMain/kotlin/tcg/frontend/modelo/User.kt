package tcg.frontend.modelo

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val username: String, val password: String, val email: String, val money: Float, val address: String, val exchanges: Int) {
}