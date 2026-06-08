package tcg.frontend.aplicacion.users.update

data class UpdateUserCommand(
    val id: Int,
    val name: String?,
    val username: String?,
    val email: String?,
    val money: Float?,
    val openBoosted: Int?,
    val exchanges: Int?,
    val isAdmin: Boolean?
)