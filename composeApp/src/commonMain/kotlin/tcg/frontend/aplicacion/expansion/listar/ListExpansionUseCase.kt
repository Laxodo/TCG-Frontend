package tcg.frontend.aplicacion.expansion.listar

import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository

class ListExpansionUseCase(private val repository: IExpansionRepository) {
    suspend operator fun invoke(generationId: Int): Result<List<Expansion>> {
        return repository.getExpansionByGeneration(generationId)
    }
}