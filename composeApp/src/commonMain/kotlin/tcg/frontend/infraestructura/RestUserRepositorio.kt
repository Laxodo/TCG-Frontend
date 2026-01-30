package tcg.frontend.infraestructura

import tcg.frontend.modelo.User
import tcg.frontend.modelo.IUserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class RestUserRepositorio(private val url: String, private val _client: HttpClient): IUserRepository {
    override suspend fun add(item: User) {
        this._client.post(url) {
            setBody(item)
        }
    }

    override suspend fun update(item: User): Boolean {
        try {
            this._client.put("$url/${item.id}") {
                setBody(item)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun remove(id: String): Boolean {
        this._client.delete("$url/$id")
        return true
    }

    override suspend fun remove(item: User): Boolean {
        try {
            this._client.delete(url) {
                setBody(item)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun findAll(): List<User> {
        val request = this._client.get(url)
        val items: List<User> = request.body()
        return items
    }

    override suspend fun findById(id: String): User? {
        try {
            return this._client.get("$url/$id").body()
        } catch (e: Exception) {
            return null
        }
    }

}