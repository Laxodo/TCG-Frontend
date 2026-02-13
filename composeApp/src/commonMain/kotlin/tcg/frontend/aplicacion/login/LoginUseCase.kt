package tcg.frontend.aplicacion.login

import tcg.frontend.dominio.IUserRepository

class LoginUseCase(private val repository: IUserRepository) {
    suspend fun invoke(loginCommand: LoginCommand): Result<Boolean>{
        return repository.login(loginCommand)
    }
}