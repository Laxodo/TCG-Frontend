package tcg.frontend.aplicacion.usercard.listCollection

import tcg.frontend.dominio.IUserRepository

class ListUserCardCollectionUseCase(private val repository: IUserRepository) {
    suspend fun invoke(listUserCardCollectionCommand: ListUserCardCollectionCommand): Result<List<UserCardCollectionDTO>>{
        return repository.getUserCollection(listUserCardCollectionCommand)
    }
}