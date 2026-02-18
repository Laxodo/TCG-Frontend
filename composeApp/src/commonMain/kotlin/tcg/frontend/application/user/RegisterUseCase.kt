package tcg.frontend.application.user

import tcg.frontend.application.user.command.RegisterCommand
import tcg.frontend.application.user.dto.UserDTO
import tcg.frontend.application.user.dto.toDTO
import tcg.frontend.modelo.User
import tcg.frontend.modelo.IUserRepository

class RegisterUseCase(private val repository: IUserRepository) {
    suspend fun invoke(command: RegisterCommand): UserDTO {
        val item = User(
            id =  0,
            name = command.name,
            password = command.password,
            username = command.username,
            email = command.email,
            money = 0.0f,
            address = "",
            exchanges = 0
        )
        repository.add(item)
        return item.toDTO()
    }
}