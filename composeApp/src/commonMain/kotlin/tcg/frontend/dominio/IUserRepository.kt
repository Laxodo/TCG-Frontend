package tcg.frontend.dominio

import tcg.frontend.aplicacion.usuarios.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionCommand
import tcg.frontend.aplicacion.usercard.listCollection.UserCardCollectionDTO
import tcg.frontend.aplicacion.usercard.listar.ListUserCardCommand
import tcg.frontend.aplicacion.usuarios.getuser.GetUserCommand
import tcg.frontend.application.register.RegisterCommand
import tcg.frontend.infraestructura.entities.user.RegisterResponse
import tcg.frontend.infraestructura.entities.user.LoginResponse

interface IUserRepository {
    suspend fun register(registerCommand: RegisterCommand): Result<Unit>
    suspend fun login(loginCommand: LoginCommand): Result<LoginResponse>
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserById(getUserCommand: GetUserCommand): Result<User>
    suspend fun deleteUser(deleteCommand: DeleteUserCommand): Result<Boolean>
    suspend fun getUserCards(listUserCardCommand: ListUserCardCommand): Result<Map<Card, List<UserCard>>>
    suspend fun getUserCollection(listUserCardCollectionCommand: ListUserCardCollectionCommand): Result<List<UserCardCollectionDTO>>
}