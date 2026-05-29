package tcg.frontend.dominio

import tcg.frontend.aplicacion.usuarios.delete.DeleteUserCommand
import tcg.frontend.aplicacion.login.LoginCommand
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionCommand
import tcg.frontend.aplicacion.usercard.listCollection.UserCardCollectionDTO
import tcg.frontend.aplicacion.usercard.listar.ListUserCardCommand
import tcg.frontend.infraestructura.entities.user.LoginResponse

interface IUserRepository {
    suspend fun login(loginCommand: LoginCommand): Result<LoginResponse>
    suspend fun getUsers(): Result<List<User>>
    suspend fun deleteUser(deleteCommand: DeleteUserCommand): Result<Boolean>
    suspend fun getUserCards(listUserCardCommand: ListUserCardCommand): Result<Map<Card, List<UserCard>>>
    suspend fun getUserCollection(listUserCardCollectionCommand: ListUserCardCollectionCommand): Result<List<UserCardCollectionDTO>>
}