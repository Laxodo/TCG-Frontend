package tcg.frontend.application.register
import tcg.frontend.infraestructura.entities.user.RegisterResponse
import tcg.frontend.dominio.IUserRepository

class RegisterUserCase(private val repository: IUserRepository) {
    suspend fun invoke(registerCommand: RegisterCommand): Result<Unit> {
        return repository.register(registerCommand)
    }
}