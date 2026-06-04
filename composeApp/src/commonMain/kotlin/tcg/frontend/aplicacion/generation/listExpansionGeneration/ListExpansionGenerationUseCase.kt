package tcg.frontend.aplicacion.generation.listExpansionGeneration

import tcg.frontend.dominio.Expansion
import tcg.frontend.dominio.IExpansionRepository

class ListExpansionGenerationUseCase(private val repository: IExpansionRepository) {
    suspend operator fun invoke(command: ListExpansionGenerationCommand): Result<List<Expansion>> {
        return repository.getExpansionByGeneration(command)
    }
}