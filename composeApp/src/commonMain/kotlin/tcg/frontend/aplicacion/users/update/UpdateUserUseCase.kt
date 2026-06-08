package tcg.frontend.aplicacion.users.update

import tcg.frontend.dominio.IUserRepository

class UpdateUserUseCase(private val repository: IUserRepository) {
    suspend fun invoke(command: UpdateUserCommand): Result<Boolean>{
        return repository.updateUser(command)
    }
}