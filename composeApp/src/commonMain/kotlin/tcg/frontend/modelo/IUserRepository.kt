package tcg.frontend.modelo

import tcg.frontend.application.register.RegisterCommand
import tcg.frontend.infraestructura.RegisterResponse

interface IUserRepository {
    suspend fun register(registerCommand: RegisterCommand): Result<RegisterResponse>
    suspend fun update(item: User): Boolean
    suspend fun remove(item: User): Boolean
    suspend fun remove(id: String): Boolean
    suspend fun findById(id: String): User?
    suspend fun findAll(): List<User>
}