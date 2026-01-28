package tcg.frontend.modelo

interface IUserRepository {
    suspend fun add(item: User): Unit
    suspend fun update(item: User): Boolean
    suspend fun remove(item: User): Boolean
    suspend fun remove(id: String): Boolean
    suspend fun findById(id: String): User?
    suspend fun findAll(): List<User>
}