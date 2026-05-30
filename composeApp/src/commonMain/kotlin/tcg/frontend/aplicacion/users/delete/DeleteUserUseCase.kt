package tcg.frontend.aplicacion.users.delete

import tcg.frontend.dominio.IUserRepository

class DeleteUserUseCase(private val repository: IUserRepository) {
    suspend fun invoke(deleteUserCommand: DeleteUserCommand): Result<Boolean>{
        return repository.deleteUser(deleteUserCommand)
    }
}