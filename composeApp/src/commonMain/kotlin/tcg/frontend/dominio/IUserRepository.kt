package tcg.frontend.dominio

import tcg.frontend.aplicacion.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.infraestructura.entities.LoginResponse

interface IUserRepository {
    suspend fun login(loginCommand: LoginCommand): Result<LoginResponse>
    suspend fun getUsers(): Result<List<User>>
    suspend fun deleteUser(deleteCommand: DeleteUserCommand): Result<Boolean>
}