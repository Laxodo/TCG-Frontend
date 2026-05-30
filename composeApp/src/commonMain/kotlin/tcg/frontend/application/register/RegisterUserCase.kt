package tcg.frontend.application.register
import tcg.frontend.infraestructura.RegisterResponse
import tcg.frontend.modelo.IUserRepository

class RegisterUserCase(private val repository: IUserRepository) {
    suspend fun invoke(registerCommand: RegisterCommand): Result<RegisterResponse> {
        return repository.register(registerCommand)
    }
}