package tcg.frontend.aplicacion

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tcg.frontend.dominio.UserSession
import tcg.frontend.infraestructura.TokenJwt
import tcg.frontend.infraestructura.TokenStorage

class UserSessionManager(private val tokenStorage: TokenStorage) {

    private val _currentUser = MutableStateFlow<UserSession?>(null)
    val currentUser: StateFlow<UserSession?> = _currentUser.asStateFlow()

    suspend fun session() {
        val accessToken = tokenStorage.getAccessToken()

        if (!accessToken.isNullOrBlank()) {
            try {
                val jwt = TokenJwt(accessToken)

                if (jwt.isSessionValid()) {
                    val user = userFromToken(jwt)
                    _currentUser.update { user }
                } else {
                    logout()
                }
            } catch (e: Exception) {
                logout()
            }
        }
    }

    fun saveSession(access: String) {
        tokenStorage.saveTokens(access)
        try {
            val user = userFromToken(TokenJwt(access))
            _currentUser.update { user }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout() {
        tokenStorage.clear()
        _currentUser.update { null }
    }

    private fun userFromToken(jwt: TokenJwt): UserSession {
        val payload = jwt.payload
        return UserSession(
            id = payload.id ?: payload.get("id") ?: 0,
            username = payload.username ?: payload.get("username") ?: "unknown",
            isAdmin = payload.isAdmin ?: false
        )
    }
}