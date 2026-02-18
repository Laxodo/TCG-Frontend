package tcg.frontend.application.user

import tcg.frontend.application.user.command.RegisterCommand
import tcg.frontend.application.user.dto.UserDTO
import tcg.frontend.application.user.dto.toDTO
import tcg.frontend.modelo.User
import tcg.frontend.modelo.IUserRepository

class RegisterUseCase(private val repository: IUserRepository) {
    suspend operator fun invoke(registerCommand: RegisterCommand): Result<RegisterDto> {
        return repository.register(registerCommand)
    }
}