package tcg.frontend.aplicacion

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tcg.frontend.dominio.User

class UserManager() {
    private val _user= MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun setUser(u: User){
        _user.value = u
    }

}