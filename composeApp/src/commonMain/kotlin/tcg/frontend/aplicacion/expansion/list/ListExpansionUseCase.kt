package tcg.frontend.aplicacion.expansion.list

import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository

class ListExpansionUseCase(private val repository: IExpansionRepository) {
    suspend fun invoke(): Result<List<Expansion>> {
        return repository.getExpansion()
    }
}