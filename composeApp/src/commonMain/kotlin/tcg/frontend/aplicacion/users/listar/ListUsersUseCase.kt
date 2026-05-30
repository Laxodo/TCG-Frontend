package tcg.frontend.aplicacion.users.listar

import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User

class ListUsersUseCase(private val repository: IUserRepository) {
    suspend fun invoke(): Result<List<User>> {
        return repository.getUsers()
    }
}