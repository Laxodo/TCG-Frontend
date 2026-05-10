package tcg.frontend.aplicacion.delete

import tcg.frontend.dominio.IUserRepository

class DeleteUserUseCase(private val repository: IUserRepository) {
    suspend fun invoke(deleteUserCommand: DeleteUserCommand): Result<Boolean>{
        return repository.deleteUser(deleteUserCommand)
    }
}