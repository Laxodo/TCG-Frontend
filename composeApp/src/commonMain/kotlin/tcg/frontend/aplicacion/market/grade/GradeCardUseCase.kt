package tcg.frontend.aplicacion.market.grade

import tcg.frontend.dominio.IMarketRepository

class GradeCardUseCase(private val repository: IMarketRepository) {
    suspend fun invoke(command: GradeCardCommand): Result<Int>{
        return repository.gradeCard(command)
    }
}