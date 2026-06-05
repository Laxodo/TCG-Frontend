package tcg.frontend.aplicacion.users.logs

import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.LogHistory
import tcg.frontend.dominio.LogHistoryPagination

class ListLogHistoryUseCase(private val repository: IUserRepository) {
    suspend fun invoke(command: ListLogHistoryCommand): Result<LogHistoryPagination>{
        return repository.getLogHistoryUser(command)
    }
}