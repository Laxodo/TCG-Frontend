package tcg.frontend.dominio

interface IGenerationRepository {
    suspend fun getGeneration(): Result<List<Generation>>
}