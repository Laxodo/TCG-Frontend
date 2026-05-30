package tcg.frontend.aplicacion.users.getuser

import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User

class GetUserUseCase(private val repository: IUserRepository) {
    suspend fun invoke(command: GetUserCommand): Result<User>{
        return repository.getUserById(command)
    }
}