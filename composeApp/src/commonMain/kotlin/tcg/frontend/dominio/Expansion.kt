package tcg.frontend.dominio

data class Expansion(
    val id: Int,
    val idGeneration: Int,
    val name: String,
    val price: Float,
    val year: Int
)