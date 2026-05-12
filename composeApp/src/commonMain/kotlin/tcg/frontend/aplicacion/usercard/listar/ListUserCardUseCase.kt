package tcg.frontend.aplicacion.usercard.listar

import tcg.frontend.dominio.IUserRepository

class ListUserCardUseCase(private val repository: IUserRepository) {
    suspend fun invoke(listUserCardCommand: ListUserCardCommand): Result<List<UserCardDTO>>{
        return repository.getUserCards(listUserCardCommand).map {
            it.flatMap { (key, value) ->
                value.map { item ->
                    item.toDTO(key)
                }
            }
        }
    }
}