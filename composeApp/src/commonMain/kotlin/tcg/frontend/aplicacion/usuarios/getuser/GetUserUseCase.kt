package tcg.frontend.aplicacion.usuarios.getuser

import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User

class GetUserUseCase(private val repository: IUserRepository) {
    suspend fun invoke(command: GetUserCommand): Result<User>{
        return repository.getUserById(command)
    }
}