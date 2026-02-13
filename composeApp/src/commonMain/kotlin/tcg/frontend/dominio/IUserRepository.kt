package tcg.frontend.dominio

import tcg.frontend.aplicacion.login.LoginCommand

interface IUserRepository {
    suspend fun login(loginCommand: LoginCommand): Result<Boolean>
}