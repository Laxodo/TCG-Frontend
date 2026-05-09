package tcg.frontend.aplicacion.login

import tcg.frontend.aplicacion.UserSessionManager
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.infraestructura.entities.LoginResponse

class LoginUseCase(private val repository: IUserRepository, private val userSessionManager: UserSessionManager) {
    suspend fun invoke(loginCommand: LoginCommand): Result<LoginResponse>{
        val result = repository.login(loginCommand)

        result.onSuccess{
            item -> userSessionManager.saveSession(item.access_token)
        }
        return result
    }
}